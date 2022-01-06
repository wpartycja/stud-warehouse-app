package edu.pw.pap21z.z15.db;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;


public class ClientRepository {

    private final EntityManager session;

    public ClientRepository(EntityManager session) {
        this.session = session;
    }

    public long  insertOrder(String clientUsername, String type) {
        Query q = session.createNativeQuery("INSERT INTO z15.orders (client_username, type)" +
                "VALUES (?, ?);").setParameter(1, clientUsername).setParameter(2, type);
        BigInteger biid = (BigInteger) q.getSingleResult();
        long id = biid.longValue();
        return id;
    }

    public long insertPallet(String description, String ownerUsername, long locationId) {
        Query q = session.createNativeQuery("INSERT INTO z15.pallets (description, owner_username, location_id)" +
                "VALUES (?, ?, ?);").setParameter(1, description)
                .setParameter(2, ownerUsername).setParameter(3, locationId);
        BigInteger biid = (BigInteger) q.getSingleResult();
        long id = biid.longValue();
        return id;
    }

    public void insertJob( long palletId, long destinationId, long orderId, String status) {
        session.createNativeQuery(
                "INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, " +
                        "assigned_worker_username) VALUES (?, ?, ?, ?, NULL);")
                .setParameter(1, palletId)
                .setParameter(2, destinationId)
                .setParameter(3, orderId)
                .setParameter(4,status);
    }
}