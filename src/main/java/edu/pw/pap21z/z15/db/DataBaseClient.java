package edu.pw.pap21z.z15.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DataBaseClient {

    private EntityManagerFactory session = Persistence.createEntityManagerFactory("oracledb");
    private EntityManager sessionManager = session.createEntityManager();

    public void startSession() {
        sessionManager.getTransaction().begin();
        }
    public void endSession() {
        sessionManager.close();
    }

    public void commit() {
        sessionManager.getTransaction().commit();
    }

    public List<Employee> getEmployeeData() {
        return sessionManager.createQuery("FROM Employee", Employee.class).getResultList();
    }
    public List<Location> getLocationData() {
        return sessionManager.createQuery("FROM Location", Location.class).getResultList();
    }
    public List<Account> getAccountData() {
        return sessionManager.createQuery("FROM Account", Account.class).getResultList();
    }
    public List<Job> getJobData() {
        return sessionManager.createQuery("FROM Job", Job.class).getResultList();
    }
    public List<Item> getItemData() {
        return sessionManager.createQuery("FROM Item", Item.class).getResultList();
    }
}
