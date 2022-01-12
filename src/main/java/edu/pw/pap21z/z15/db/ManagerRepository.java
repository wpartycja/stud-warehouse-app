package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

    public List<Account> getWorkers() {
        TypedQuery<Account> query = session.createQuery("SELECT a from Account a where a.type = edu.pw.pap21z.z15.db.model.AccountType.WORKER", Account.class);
        return query.getResultList();
    }

    public List<Location> getAvailableDestinations(Job job) {
        if (job.getOrder().getType() == OrderType.IN) {
            TypedQuery<Location> query = session.createQuery("SELECT l from Location l where l.type = edu.pw.pap21z.z15.db.model.LocationType.SHELF", Location.class);
            var shelves = query.getResultList();
            return shelves.stream()
                    .filter(s -> s.getPallets().isEmpty())
                    .collect(Collectors.toList());
        } else {
            TypedQuery<Location> query = session.createQuery("SELECT l from Location l where l.type = edu.pw.pap21z.z15.db.model.LocationType.OUT_RAMP", Location.class);
            return query.getResultList();
        }
    }

    public List<Account> getIdleWorkers() {
        return getWorkers().stream().filter(worker -> worker.getCurrentJob() == null).collect(Collectors.toList());
    }

    public void assignJobToWorker(Job job, Account worker) {
        try {
            session.getTransaction().begin();
            job.setStatus(JobStatus.IN_PROGRESS);
            worker.setCurrentJob(job);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void scheduleJob(Job job, Location dest) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        job.setStatus(JobStatus.PENDING);
        job.setDestination(dest);
        transaction.commit();
    }

    public void unscheduleJob(Job job) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        job.setStatus(JobStatus.PLANNED);
        transaction.commit();
    }
}
