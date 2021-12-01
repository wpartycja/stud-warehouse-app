package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.DataBaseClient;
import edu.pw.pap21z.z15.db.Item;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.util.*;

public class ClientController {

    private final DataBaseClient dbClient = new DataBaseClient();

    public class Item {

        private List<ItemAttribute> attributeList;

        public Item(List<ItemAttribute> attributeList) {
            this.attributeList = attributeList;
        }

        public Item() {
            this.attributeList = new ArrayList<ItemAttribute>();
        }

        public List<ItemAttribute> getAttributeList() {
            return attributeList;
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
            return new SimpleStringProperty(name);
        }

        public SimpleStringProperty valueProperty() {
            return new SimpleStringProperty(value);
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

        public List<Item> getItemList() {
            return itemList;
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
    private ListView<String> warehouseContentsList;

    private void initializeOrderItemTable() {
        TableColumn itemNameColumn = new TableColumn<Item, String>("Item Name");
        itemNameColumn.setMinWidth(100);
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn ItemQuantityColumn = new TableColumn("Item Quantity");
        ItemQuantityColumn.setMinWidth(100);
        ItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("quantity"));
        orderItemTable.getColumns().addAll(itemNameColumn, ItemQuantityColumn);

        MenuItem mi1 = new MenuItem("Show");
        mi1.setOnAction((ActionEvent event) -> {
            Item item = orderItemTable.getSelectionModel().getSelectedItem();
//             update item attribute table on click
            updateItemAttributeTable(item);
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        orderItemTable.setContextMenu(menu);

    }

    private void updateOrderItemTable(Order order) {
        orderItemTable.setItems(FXCollections.observableArrayList(order.getItemList()));
    }

    @FXML
    private TableView<ItemAttribute> itemAttributeTable;

    private void initializeItemAttributeTable() {
        TableColumn attributeNameColumn = new TableColumn<ItemAttribute, String>("Attribute Name");
        attributeNameColumn.setMinWidth(100);
        attributeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn attributeValueColumn = new TableColumn("Attribute Value");
        attributeValueColumn.setMinWidth(100);
        attributeValueColumn.setCellValueFactory(new PropertyValueFactory<ItemAttribute, String>("value"));
        itemAttributeTable.getColumns().addAll(attributeNameColumn, attributeValueColumn);
        itemAttributeTable.setEditable(true);
        attributeValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void updateItemAttributeTable(Item item) {
        itemAttributeTable.setItems(FXCollections.observableArrayList(item.getAttributeList()));
    }

    @FXML
    private void initialize() {

//        Order order = new Order();
//        order.addItem(new Item(Arrays.asList(new ItemAttribute("NAME", "testName1"),
//                new ItemAttribute("QUANTITY", "testQuantity1"),
//                new ItemAttribute("SIZE", "testSize1"))));
//        order.addItem(new Item(Arrays.asList(new ItemAttribute("NAME", "testName2"),
//                new ItemAttribute("QUANTITY", "testQuantity2"),
//                new ItemAttribute("SIZE", "testSize2"),
//                new ItemAttribute("WEIGHT", "testWeight2"),
//                new ItemAttribute("COLOR", "testColor2"))));
//        order.addItem(new Item(Arrays.asList(new ItemAttribute("NAME", "testName3"),
//                new ItemAttribute("QUANTITY", "testQuantity3"),
//                new ItemAttribute("SIZE", "testSize3"),
//                new ItemAttribute("WEIGHT", "testWeight3"))));
//
//        initializeOrderItemTable();
//        updateOrderItemTable(order);
//
//        Item item = new Item(Arrays.asList(new ItemAttribute("NAME", "testName"),
//                new ItemAttribute("QUANTITY", "testQuantity"),
//                new ItemAttribute("SIZE", "testSize")));
//
//        initializeItemAttributeTable();
//        updateItemAttributeTable(item);

        List<String> items = new ArrayList<>();
        for (edu.pw.pap21z.z15.db.Item i : dbClient.getItemData()) {
            items.add(i.getDescription());
        }
        var contents = FXCollections.observableArrayList(items);
        warehouseContentsList.setItems(contents);
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
