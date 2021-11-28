package edu.pw.pap21z.z15.db;

import java.util.*;
import java.util.stream.Collectors;

import static edu.pw.pap21z.z15.db.Location.LocationType.*;

public final class MockDb {

    private static MockDb INSTANCE;

    public static MockDb getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDb();
        }

        return INSTANCE;
    }

    private Set<Location> locations = new HashSet<>();
    private Set<Item> items = new HashSet<>();
    private Set<Job> jobs = new HashSet<>();
    private int nextItemId = 0;
    private int nextJobId = 0;

    private MockDb() {
        int nextLocationId = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    var location = new Location(
                            nextLocationId++,
                            SHELF,
                            String.format("Alejka %d/Regał %d/Półka %d", i, j, k)
                    );
                    locations.add(location);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            locations.add(new Location(nextLocationId++, IN_RAMP, "Rampy rozładunkowe/Rampa " + i));
            locations.add(new Location(nextLocationId++, OUT_RAMP, "Rampy załadunkowe/Rampa " + i));
        }

        for (int i = 0; i < 30; i++) {
            insertItem("Item #" + i);
        }
        System.out.println(getItems());
    }

    public Set<Item> getItemsForLocation(int locationId) {
        return items.stream()
                .filter(item -> item.locationId == locationId)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public int insertItem(String description) {
        var inRamp = locations.stream().filter(l -> l.type == IN_RAMP).findFirst().get();
        var shelf = locations.stream()
                .filter(l -> l.type == SHELF && getItemsForLocation(l.locationId).isEmpty())
                .findFirst().get();
        var item = new Item(nextItemId++, inRamp.locationId, description);
        var job = new Job(nextJobId++, item.itemId, shelf.locationId);
        items.add(item);
        jobs.add(job);
        return item.itemId;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Set<Job> getJobs() {
        return jobs;
    }
}