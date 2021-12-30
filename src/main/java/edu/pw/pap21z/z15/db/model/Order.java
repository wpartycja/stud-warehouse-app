package edu.pw.pap21z.z15.db.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @Column(name = "ORDER_ID", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENT_USERNAME", nullable = false)
    private Account client;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    private OrderType type;

    @OneToMany(mappedBy = "order")
    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType orderType) {
        this.type = orderType;
    }

    public Account getClient() {
        return client;
    }

    public void setClient(Account clientUsername) {
        this.client = clientUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}