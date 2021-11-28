package edu.pw.pap21z.z15.db;

import java.util.Objects;

public class Item {
    int itemId;
    int locationId;
    String description;

    public Item(int itemId, int locationId, String description) {
        this.itemId = itemId;
        this.locationId = locationId;
        this.description = description;
    }

    public int getItemId() {
        return itemId;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
