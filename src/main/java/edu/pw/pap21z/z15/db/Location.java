package edu.pw.pap21z.z15.db;

import java.util.Objects;

public class Location {
    enum LocationType {IN_RAMP, OUT_RAMP, SHELF}

    int locationId;
    LocationType type;
    String path;

    public Location(int locationId, LocationType type, String path) {
        this.locationId = locationId;
        this.type = type;
        this.path = path;
    }

    public int getLocationId() {
        return locationId;
    }

    public LocationType getType() {
        return type;
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