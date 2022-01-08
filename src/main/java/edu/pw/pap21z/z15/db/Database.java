package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

// todo abstract into DAOs and repositories for each entity type
public class Database {

    private final EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("oracledb");
    public final EntityManager session = sessionFactory.createEntityManager();

    private <T> List<T> getAll(Class<T> cls) {
        CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(cls);
        criteria.from(cls);
        return session.createQuery(criteria).getResultList();
    }

    private <T> T getById(Class<T> cls, Object id) {
        return session.find(cls, id);
    }

    public Account getAccountByUsername(String username) {
        return getById(Account.class, username);
    }

    public List<Pallet> getPallets() {
        return getAll(Pallet.class);
    }

    public List<Location> getLocations() {
        return getAll(Location.class);
    }

    public List<Job> getJobs() {
        return getAll(Job.class); // todo shouldn't probably return completed jobs by default
    }

    public Job getJobById(Long id) {
        return getById(Job.class, id);
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
