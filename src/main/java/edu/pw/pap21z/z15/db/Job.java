package edu.pw.pap21z.z15.db;

import java.util.Objects;

public class Job {

    int jobId;
    int itemId;
    int locationId;

    public Job(int jobId, int itemId, int locationId) {
        this.jobId = jobId;
        this.itemId = itemId;
        this.locationId = locationId;
    }

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
