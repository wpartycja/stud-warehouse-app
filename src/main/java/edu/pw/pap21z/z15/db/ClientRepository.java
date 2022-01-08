package edu.pw.pap21z.z15.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ClientRepository {

    private final EntityManager session;

    public ClientRepository(EntityManager session) {
        this.session = session;
    }

    public void insertInJob(String clientUsername, String type,
                            String description, String ownerUsername, long locationId,
                            long destinationId , String status) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        long orderId = insertOrder( clientUsername, type);
        long palletId = insertPallet( description, ownerUsername, locationId);
        insertJob( palletId, destinationId, orderId, status);
        transaction.commit();
    }

    public void insertOutJob(String clientUsername, String type,
                             long palletId, long destinationId , String status) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        long orderId = insertOrder( clientUsername, type);
        insertJob( palletId, destinationId, orderId, status);
        transaction.commit();
    }

    public long  insertOrder(String clientUsername, String type) {
        return session.createNativeQuery(String.format("INSERT INTO z15.orders (client_username, type) " +
                    "VALUES ('%s', '%s')", clientUsername, type)).executeUpdate();
    }

    public long insertPallet(String description, String ownerUsername, long locationId) {
        return session.createNativeQuery(String.format("INSERT INTO z15.pallets (description, owner_username, location_id) " +
                "VALUES ('%s', '%s', %d)", description, ownerUsername, locationId)).executeUpdate();
    }

    public void insertJob( long palletId, long destinationId, long orderId, String status) {
        session.createNativeQuery(String.format("INSERT INTO z15.jobs (destination_id, pallet_id, " +
                "order_id, status, assigned_worker_username) VALUES (%d, %d, %d, '%s', NULL)",
                destinationId, palletId, orderId, status)).executeUpdate();
    }
}