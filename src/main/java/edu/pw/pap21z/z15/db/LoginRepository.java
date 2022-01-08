package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class LoginRepository {

    private final EntityManager session;

    public LoginRepository(EntityManager session) {
        this.session = session;
    }

    public Account getAccountByUsername(String username) {
        return session.find(Account.class, username);
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

    public void updateAccount(String name, String surname, String account_username) {
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        session.createNativeQuery(String.format(
                "UPDATE z15.accounts SET name = '%s', surname =  '%s' " +
                        "WHERE account_username = '%s'",
                name, surname, account_username)).executeUpdate();
        transaction.commit();
    }
}