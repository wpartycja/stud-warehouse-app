package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.Location;
import javafx.concurrent.Worker;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


public class WorkerRepository {

    private final EntityManager session;

    public WorkerRepository(EntityManager session) { this.session = session; }

    private <T> List<T> getAll(Class<T> cls) {
        CriteriaQuery<T> criteria = session.getCriteriaBuilder().createQuery(cls);
        criteria.from(cls);
        return session.createQuery(criteria).getResultList();
    }

    public List<Job> getJobs(){ return getAll(Job.class); }

    public Job getJobById(long job_id) {
        Job job = (Job) session.find(Job.class, job_id);
        if (job == null) {
            throw new EntityNotFoundException("Can't find job Id: " + job_id);
        }
        return job;
    }

    public void startJob(Job job, String worker_id){
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        job.setStatus(JobStatus.IN_PROGRESS);
        Account worker = (Account) session.find(Account.class, worker_id);
        if (worker == null) {
            throw new EntityNotFoundException("Can't find worker Id: " + worker_id);
        }
        worker.setCurrentJob(job);
        transaction.commit();
    }
}
