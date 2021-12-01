package edu.pw.pap21z.z15.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DEPT")
public class Dept {
    @Id
    @Column(name = "DEPTNO")
    private int deptNo;

    @Column(name = "DNAME")
    private String name;

    @Column(name = "LOC")
    private String location;

    public Dept() {
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDeptNo() {
        return deptNo;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
