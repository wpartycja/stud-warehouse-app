package edu.pw.pap21z.z15.db;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @Column(name = "ITEM_ID")
    private int itemId;

    @Column(name="LOCATION")
    private int locationId;

    @Column(name="DESCRIPTION")
    private String description;

    public Item(){}

    public int getItemId() { return itemId; }

    public int getLocationId() {return locationId;}

    public String getDescription() {return description;}

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
