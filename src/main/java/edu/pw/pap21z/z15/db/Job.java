package edu.pw.pap21z.z15.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name="JOB")
public class Job {
    @Id
    @Column(name = "JOB_ID")
    private int jobId;

    @Column(name = "ITEM_ID")
    private int itemId;

    @Column(name = "DEST")
    private int locationId;


    public Job(){}

    public int getJobId() {
        return jobId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getLocationId() {
        return locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return jobId == job.jobId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId);
    }
}
