package edu.pw.pap21z.z15.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "LOCATION")
public class Location {
    enum LocationType {IN_RAMP, OUT_RAMP, SHELF}

    @Id
    @Column(name = "LOCATION_ID")
    private int locationId;

    @Column(name = "TYPE")
    private String temporaryType;

    @Column(name = "PATH")
    private String path;

    public Location() {}

    public int getLocationId() {
        return locationId;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId);
    }
}