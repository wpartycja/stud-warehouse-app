package edu.pw.pap21z.z15;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.AttributeException;
import java.util.*;

public class ClientController {

    public class Item {

        private List<ItemAttribute> attributeList;

        public Item(List<ItemAttribute> attributeList) {
            this.attributeList = attributeList;
        }

        public Item() {
            this.attributeList = new ArrayList<ItemAttribute>();
        }

        public void addAttribute(ItemAttribute attribute) {
            attributeList.add(attribute);
        }

        public ItemAttribute getAttribute(int index) {
            return attributeList.get(index);
        }

        public void deleteAttribute(int index) {
            attributeList.remove(index);
        }

        private SimpleStringProperty getAttributeValueProperty(String attributeName) {
            Optional<ItemAttribute> matchingAttribute = this.attributeList.stream()
                    .filter(x -> x.getName().equals(attributeName))
                    .findAny();
            if (matchingAttribute.isPresent()) {
                return matchingAttribute.get().valueProperty();
            } else {
                return new SimpleStringProperty("undefined");
            }
        }

        public SimpleStringProperty nameProperty() {
            return getAttributeValueProperty("NAME");
        }
        public SimpleStringProperty quantityProperty() {
            return getAttributeValueProperty("QUANTITY");
        }

    }

    public class ItemAttribute {

        private String name;
        private String value;

        public ItemAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public SimpleStringProperty nameProperty() {
            return new SimpleStringProperty(this.name);
        }
        public SimpleStringProperty valueProperty() {
            return new SimpleStringProperty(this.value);
        }
    }

    public class Order {

        private List<Item> itemList;

        public Order(List<Item> itemList) {
            this.itemList = itemList;
        }

        public Order() {
            this.itemList = new ArrayList<>();
        }

        public void addItem(Item attribute) {
            itemList.add(attribute);
        }

        public Item getItem(int index) {
            return itemList.get(index);
        }

        public void deleteItem(int index) {
            itemList.remove(index);
        }
    }

    @FXML
    private TableView<Item> orderItemTable;


    @FXML
    private TableView<ItemAttribute> itemAttributeTable;

    @FXML
    private void initialize() {
        ObservableList<Item> orderItems = FXCollections.observableArrayList(
                new Item(Arrays.asList(new ItemAttribute("NAME", "testName"),
                                       new ItemAttribute("QUANTITY", "testQuantity"),
                                       new ItemAttribute("SIZE", "testSize")))
        );

        TableColumn itemNameColumn = new TableColumn<Item, String>("Item Name");
        itemNameColumn.setMinWidth(100);
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn ItemQuantityColumn = new TableColumn("Item Quantity");
        ItemQuantityColumn.setMinWidth(100);
        ItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("quantity"));

        orderItemTable.setItems(orderItems);
        orderItemTable.getColumns().addAll(itemNameColumn, ItemQuantityColumn);

        ObservableList<ItemAttribute> itemAttributes = FXCollections.observableArrayList(
                        new ItemAttribute("NAME", "testName"),
                        new ItemAttribute("QUANTITY", "testQuantity"),
                        new ItemAttribute("SIZE", "testSize"));

        TableColumn attributeNameColumn = new TableColumn<Item, String>("Attribute Name");
        attributeNameColumn.setMinWidth(100);
        attributeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn attributeValueColumn = new TableColumn("Attribute Value");
        attributeValueColumn.setMinWidth(100);
        attributeValueColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("value"));

        itemAttributeTable.setItems(itemAttributes);
        itemAttributeTable.getColumns().addAll(attributeNameColumn, attributeValueColumn);
    }

}
