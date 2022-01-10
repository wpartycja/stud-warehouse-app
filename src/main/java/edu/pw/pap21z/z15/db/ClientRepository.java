package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.Location;
import edu.pw.pap21z.z15.db.model.Order;
import edu.pw.pap21z.z15.db.model.Pallet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ClientRepository {

    private final EntityManager session;

    public ClientRepository(EntityManager session) {
        this.session = session;
    }

    private <T> List<T> getAll(Class<T> cls) {
        CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(cls);
        criteria.from(cls);
        return session.createQuery(criteria).getResultList();
    }

    public void insertInJob(String clientUsername, String type, String description, String status) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();

        insertOrder(clientUsername, type);

        TypedQuery<Location> getIN_RAMPLocation = session.createQuery("SELECT l from Location l where l.type = edu.pw.pap21z.z15.db.model.LocationType.IN_RAMP", Location.class);
        Location destination = getIN_RAMPLocation.getSingleResult();
        TypedQuery<Order> getOrderId = session.createQuery("SELECT o from Order o WHERE o.id = (SELECT MAX(o2.id) from Order o2)", Order.class);
        Order order = getOrderId.getSingleResult();

        insertPallet(description, clientUsername, destination.getId());

        TypedQuery<Pallet> getPalletId = session.createQuery("SELECT p from Pallet p WHERE p.id = (SELECT MAX(p2.id) from Pallet p2)", Pallet.class);
        Pallet pallet = getPalletId.getSingleResult();

        insertJob(pallet.getId(), destination.getId(), order.getId(), status);
        transaction.commit();
    }

    public void insertOutJob(String clientUsername, String type, long palletId, String status) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();

        insertOrder(clientUsername, type);

        TypedQuery<Order> getOrderId = session.createQuery("SELECT o from Order o WHERE o.id = (SELECT MAX(o2.id) from Order o2)", Order.class);
        Order order = getOrderId.getSingleResult();
        TypedQuery<Location> getOUT_RAMPLocation = session.createQuery("SELECT l from Location l where l.type = edu.pw.pap21z.z15.db.model.LocationType.OUT_RAMP", Location.class);
        Location destination = getOUT_RAMPLocation.getSingleResult();

        insertJob(palletId, destination.getId(), order.getId(), status);
        transaction.commit();
    }

    public void insertOrder(String clientUsername, String type) {
        session.createNativeQuery(String.format("INSERT INTO z15.orders (client_username, type) " +
                "VALUES ('%s', '%s')", clientUsername, type)).executeUpdate();
    }

    public void insertPallet(String description, String ownerUsername, long locationId) {
        session.createNativeQuery(String.format("INSERT INTO z15.pallets (description, owner_username, location_id) " +
                "VALUES ('%s', '%s', %d)", description, ownerUsername, locationId)).executeUpdate();
    }

    public void insertJob(long palletId, long destinationId, long orderId, String status) {
        session.createNativeQuery(String.format("INSERT INTO z15.jobs (destination_id, pallet_id, " +
                        "order_id, status, assigned_worker_username) VALUES (%d, %d, %d, '%s', NULL)",
                destinationId, palletId, orderId, status)).executeUpdate();
    }

    public List<Pallet> getPallets() {
        return getAll(Pallet.class);
    }

    public List<Job> getJobs() {
        return getAll(Job.class);
    }
}