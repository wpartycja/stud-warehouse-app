package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.Item;
import edu.pw.pap21z.z15.db.MockDb;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.io.IOException;
import java.util.Objects;

public class ManagerController {

    @FXML
    private TreeTableView<JobEntry> ordersList;

    @FXML
    private TableView<WorkerEntry> workersList;

    @FXML
    private TreeView<String> contentsTree;

    private final MockDb db = MockDb.getInstance();

    private TreeItem<String> getOrCreateChild(TreeItem<String> node, String childValue) {
        var existingNode = node.getChildren().stream()
                .filter(i -> Objects.equals(i.getValue(), childValue))
                .findAny();
        if (existingNode.isPresent()) return existingNode.get();

        var childItem = new TreeItem<>(childValue);
        node.getChildren().add(childItem);
        return childItem;
    }

    private TreeItem<String> buildContentsTree() {
        TreeItem<String> rootItem = new TreeItem<>();
        var locations = db.getLocations();
        for (var location : locations) {
            var node = rootItem;
            var path = location.getPath().split("/");
            for (String s : path) node = getOrCreateChild(node, s);
            for (Item i : db.getItemsForLocation(location.getLocationId())) {
                node.getChildren().add(new TreeItem<>(i.getDescription()));
            }
        }
        return rootItem;
    }

    @SuppressWarnings("unused")
    public static class WorkerEntry {
        private final SimpleStringProperty name;
        private final SimpleStringProperty status;

        private WorkerEntry(String name, String status) {
            this.name = new SimpleStringProperty(name);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    @SuppressWarnings("unused")
    public static class JobEntry {

        private final SimpleStringProperty item;
        private final SimpleStringProperty from;
        private final SimpleStringProperty to;

        private JobEntry(String item, String from, String to) {
            this.item = new SimpleStringProperty(item);
            this.from = new SimpleStringProperty(from);
            this.to = new SimpleStringProperty(to);
        }

        public SimpleStringProperty itemProperty() {
            return item;
        }

        public SimpleStringProperty fromProperty() {
            return from;
        }

        public SimpleStringProperty toProperty() {
            return to;
        }
    }

    ObservableList<WorkerEntry> workers = FXCollections.observableArrayList(
            new WorkerEntry("Steve", "Idle"),
            new WorkerEntry("Andrzej", "Moving item #443"),
            new WorkerEntry("Rahim", "Moving item #10")
    );


    @FXML
    private void initialize() {
        contentsTree.setShowRoot(false);
        contentsTree.setRoot(buildContentsTree());

        var nameCol = new TableColumn<WorkerEntry, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        workersList.getColumns().add(nameCol);
        var statusCol = new TableColumn<WorkerEntry, String>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        workersList.getColumns().add(statusCol);

        workersList.setItems(workers);

        var itemCol = new TreeTableColumn<JobEntry, String>("Item");
        itemCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("item"));
        ordersList.getColumns().add(itemCol);
        var fromCol = new TreeTableColumn<JobEntry, String>("From");
        fromCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("from"));
        ordersList.getColumns().add(fromCol);
        var toCol = new TreeTableColumn<JobEntry, String>("To");
        toCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("to"));
        ordersList.getColumns().add(toCol);

        var ordersRoot = new TreeItem<JobEntry>();
        var orderItem1 = new TreeItem<>(new JobEntry("Order #323", "", ""));
        orderItem1.getChildren().add(new TreeItem<>(new JobEntry("Item #332", "Loading ramp 3", "Shelf A/1/3")));
        orderItem1.getChildren().add(new TreeItem<>(new JobEntry("Item #123", "Loading ramp 3", "Shelf A/1/4")));
        orderItem1.getChildren().add(new TreeItem<>(new JobEntry("Item #22", "Loading ramp 3", "Shelf A/1/5")));
        ordersRoot.getChildren().add(orderItem1);
        var orderItem2 = new TreeItem<>(new JobEntry("Order #111", "", ""));
        orderItem2.getChildren().add(new TreeItem<>(new JobEntry("Item #002", "Shelf B/5/4", "Unloading ramp 2")));
        orderItem2.getChildren().add(new TreeItem<>(new JobEntry("Item #004", "Shelf B/5/6", "Unloading ramp 2")));
        orderItem2.getChildren().add(new TreeItem<>(new JobEntry("Item #005", "Shelf B/5/9", "Unloading ramp 2")));
        ordersRoot.getChildren().add(orderItem2);
        ordersList.setShowRoot(false);
        ordersList.setRoot(ordersRoot);

    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}