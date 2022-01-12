package edu.pw.pap21z.z15.db.model;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "LOCATIONS")
public class Location {
    @Id
    @Column(name = "LOCATION_ID", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    private LocationType type;

    @Column(name = "PATH", nullable = false, length = 20)
    private String path;

    @OneToMany(mappedBy = "location")
    private List<Pallet> pallets;

    @OneToMany(mappedBy = "destination")
    private List<Job> jobs;

    public List<Job> getJobs() {
        return jobs;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Pallet> getPallets() {
        return pallets;
    }

    public void setPallets(List<Pallet> pallets) {
        this.pallets = pallets;
    }
}