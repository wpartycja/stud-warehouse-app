package edu.pw.pap21z.z15.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "EMP")
public class Employee {
    @Id
    @Column(name = "EMPNO")
    private int employeeId;

    @Column(name="ENAME")
    private String name;

    @Column(name="JOB")
    private String job;

    @Column(name="HIREDATE")
    private String hireDate;

    @Column(name="SAL")
    private int salary;

    public Employee(){}

    public String getName() {return name;}

    public int getId() {return employeeId;}

    public String getJob() {return job;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee item = (Employee) o;
        return employeeId == item.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}
