package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerRepository {

    private final EntityManager session;

    public ManagerRepository(EntityManager session) {
        this.session = session;
    }

    private <T> List<T> getAll(Class<T> cls) {
        CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(cls);
        criteria.from(cls);
        return session.createQuery(criteria).getResultList();
    }

    public List<Location> getLocations() {
        return getAll(Location.class);
    }

    public List<Order> getOrders() {
        return getAll(Order.class);
    }

    public List<Job> getJobs() {
        return getAll(Job.class);
    }

    public List<Account> getWorkers() {
        TypedQuery<Account> query = session.createQuery("SELECT a from Account a where a.type = edu.pw.pap21z.z15.db.model.AccountType.WORKER", Account.class);
        return query.getResultList();
    }

    /**
     * @param job Planned job
     * @return List of locations given job can be assigned to
     */
    public List<Location> getAvailableDestinations(Job job) {
        if (job.getOrder().getType() == OrderType.IN) {

            Query query = session.createNativeQuery("SELECT * from LOCATIONS l " +
                            // 1. must be a shelf
                            "where l.TYPE = 'SHELF' " +
                            // 2. can't currently have pallets on it
                            "and not exists(SELECT * from PALLETS p where p.LOCATION_ID = l.LOCATION_ID)" +
                            // 3. there can't be any jobs that are currently assigned to it
                            "and not exists(SELECT * from JOBS j where l.LOCATION_ID = j.DESTINATION_ID and j.STATUS IN ('PENDING','IN_PROGRESS'))",
                    Location.class);
            // noinspection unchecked
            return query.getResultList();
        } else {
            // out job can go to any out ramp
            TypedQuery<Location> query = session.createQuery("SELECT l from Location l where l.type = edu.pw.pap21z.z15.db.model.LocationType.OUT_RAMP", Location.class);
            return query.getResultList();
        }
    }

    /**
     * @return List of workers that can be assigned to a job
     */
    public List<Account> getIdleWorkers() {
        return getWorkers().stream().filter(worker -> worker.getCurrentJob() == null).collect(Collectors.toList());
    }

    public void scheduleJob(Job job, Location dest) {
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            job.setStatus(JobStatus.PENDING);
            job.setDestination(dest);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    public void unscheduleJob(Job job) {
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            job.setStatus(JobStatus.PLANNED);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }


    public void assignJobToWorker(Job job, Account worker) {
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            job.setStatus(JobStatus.IN_PROGRESS);
            worker.setCurrentJob(job);
            job.setAssignedWorker(worker);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    public void unassignWorker(Job job) {
        EntityTransaction transaction = session.getTransaction();
        try {
            transaction.begin();
            job.setStatus(JobStatus.PENDING);
            job.getAssignedWorker().setCurrentJob(null);
            job.setAssignedWorker(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

}