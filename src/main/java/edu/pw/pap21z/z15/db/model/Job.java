package edu.pw.pap21z.z15.db.model;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "JOBS")
public class Job {
    @Id
    @Column(name = "JOB_ID", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "DESTINATION_ID", nullable = false)
    private Location destination;

    @OneToOne(optional = false)
    @JoinColumn(name = "PALLET_ID", nullable = false)
    private Pallet pallet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private JobStatus status;

    @OneToOne
    @JoinColumn(name = "ASSIGNED_WORKER_USERNAME")
    private Account assignedWorker;

    public Account getAssignedWorker() {
        return assignedWorker;
    }

    public void setAssignedWorker(Account assignedWorker) {
        this.assignedWorker = assignedWorker;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Pallet getPallet() {
        return pallet;
    }

    public void setPallet(Pallet pallet) {
        this.pallet = pallet;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}