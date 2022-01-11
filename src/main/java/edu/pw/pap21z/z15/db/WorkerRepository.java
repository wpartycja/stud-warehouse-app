package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.Location;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    public Job getCurrentJob(Account worker, JobStatus jobStatus){
        TypedQuery<Job> query = session.createQuery("SELECT j FROM Job j WHERE j.status = edu.pw.pap21z.z15.db.model.JobStatus.IN_PROGRESS", Job.class);
        List<Job> jobs = query.getResultList();
        Job currentJob = null;
        for (Job job : jobs){
            if(job.getAssignedWorker() == worker){
                currentJob = job;
            }
        }
        return currentJob;
    }
}
