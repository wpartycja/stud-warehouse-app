package edu.pw.pap21z.z15.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class LoginRepository {

    private final EntityManager session;

    public LoginRepository(EntityManager session) {
        this.session = session;
    }

    public void insertAccount(String account_username, String password, String type, String name, String surname) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        session.createNativeQuery(String.format(
                "INSERT INTO z15.accounts (account_username, password, type, name, surname) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s')",
                account_username, password, type, name, surname)).executeUpdate();
        transaction.commit();
    }
}