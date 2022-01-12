package edu.pw.pap21z.z15.db.model;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "ACCOUNTS")
public class Account {
    @Id
    @Column(name = "ACCOUNT_USERNAME", nullable = false, length = 20)
    private String id;

    @Column(name = "PASSWORD", nullable = false, length = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    private AccountType type;

    @Column(name = "NAME", length = 20)
    private String name;

    @Column(name = "SURNAME", length = 20)
    private String surname;

    @OneToOne(mappedBy = "assignedWorker")
    private Job currentJob;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }
}