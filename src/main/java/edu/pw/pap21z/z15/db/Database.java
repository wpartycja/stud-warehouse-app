package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.Location;
import edu.pw.pap21z.z15.db.model.Pallet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

// todo abstract into DAOs and repositories for each entity type
public class Database {

    private final EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("oracledb");
    private final EntityManager session = sessionFactory.createEntityManager();

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

    public List<Account> getWorkers() {
        return getAll(Account.class); // todo: should be just accounts with type worker
    }

    public List<Job> getJobs() {
        return getAll(Job.class); // todo shouldn't probably return completed jobs by default
    }

    public Job getJobById(Long id) {
        return getById(Job.class, id);
    }
}
