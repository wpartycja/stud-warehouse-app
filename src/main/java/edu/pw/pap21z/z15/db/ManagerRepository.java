package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Location;
import edu.pw.pap21z.z15.db.model.Order;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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
}
