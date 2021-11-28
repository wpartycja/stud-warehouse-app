package edu.pw.pap21z.z15;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class ClientController {

    @FXML
    private ListView<Item> orderItemList;

    @FXML
    private ListView<ItemAttribute> itemAttributeList;

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
    private void initialize() {
        ObservableList<Item> items = FXCollections.observableArrayList(
                new Item(Arrays.asList(new ItemAttribute("chleb", "2 bochny")))
        );
        orderItemList.setItems(items);

    }

}
