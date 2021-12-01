package edu.pw.pap21z.z15.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "ID_ACCOUNT")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "TYPE")
    private String type;

    public int getId() {return id;}

    public String getName() {return name;}

    public String getSurname() {return surname;}

    public String getLogin() {return login;}

    public String getPassword() {return password;}

    public String getType() {return type;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account acc = (Account) o;
        return id == acc.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}