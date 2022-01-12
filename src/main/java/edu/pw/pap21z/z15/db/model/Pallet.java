package edu.pw.pap21z.z15.db.model;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "PALLETS")
public class Pallet {
    @Id
    @Column(name = "PALLET_ID", nullable = false)
    private Long id;

    @Column(name = "DESCRIPTION", length = 20)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OWNER_USERNAME", nullable = false)
    private Account ownerUsername;

    @ManyToOne(optional = false)
    @JoinColumn(name = "LOCATION_ID", nullable = false)
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Account getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(Account ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}