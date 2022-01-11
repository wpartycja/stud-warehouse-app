package edu.pw.pap21z.z15.db;

import edu.pw.pap21z.z15.db.model.*;
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

    public List<Job> getPendingJobs(){
        TypedQuery<Job> query = session.createQuery("SELECT j from Job j where j.status = edu.pw.pap21z.z15.db.model.JobStatus.PENDING", Job.class);
        return query.getResultList();
    }

    public Job getJobById(long job_id) {
        Job job = (Job) session.find(Job.class, job_id);
        if (job == null) {
            throw new EntityNotFoundException("Can't find job Id: " + job_id);
        }
        return job;
    }

    public void startJob(Job job, String worker_id){
        Account worker = (Account) session.find(Account.class, worker_id);
        if (worker == null) {
            throw new EntityNotFoundException("Can't find worker Id: " + worker_id);
        }
        try {
            session.getTransaction().begin();
            job.setStatus(JobStatus.IN_PROGRESS);
            worker.setCurrentJob(job);
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    public void completeJob(Job job, Account worker){

        try {
            session.getTransaction().begin();
            job.setStatus(JobStatus.COMPLETED);
            worker.setCurrentJob(null);
            Location jobDest = job.getDestination();
            Pallet jobPallet = job.getPallet();
            jobPallet.setLocation(jobDest);
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void undoJob(Job job, Account worker){
        try {
            session.getTransaction().begin();
            job.setStatus(JobStatus.PENDING);
            worker.setCurrentJob(null);
            session.getTransaction().commit();
        } catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
