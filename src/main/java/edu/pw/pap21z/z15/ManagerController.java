package edu.pw.pap21z.z15;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Random;

public class ManagerController {

    @FXML
    private TableView<JobEntry> queueList;

    @FXML
    private TableView<WorkerEntry> workersList;

    @FXML
    private TreeView<String> contentsTree;

    private static final Random rng = new Random();

    private static TreeItem<String> buildContentsTree() {
        var rootItem = new TreeItem<String>();

        var warehouseItem = new TreeItem<>("Warehouse");
        for (int k = 0; k < 4; k++) {
            var aisleItem = new TreeItem<>("Aisle " + (char) ('A' + k));
            for (int j = 0; j < 4; j++) {
                var rackItem = new TreeItem<>("Rack " + j);
                for (int i = 0; i < 4; i++) {
                    var shelfItem = new TreeItem<String>();
                    if (rng.nextBoolean()) {
                        shelfItem.setValue(String.format("Shelf %d", i));
                        shelfItem.setExpanded(true);
                        shelfItem.getChildren().add(new TreeItem<>(String.format("Item #%d", rng.nextInt(1000))));
                    } else {
                        shelfItem.setValue(String.format("Shelf %d (empty)", i));
                    }
                    rackItem.getChildren().add(shelfItem);
                }
                aisleItem.getChildren().add(rackItem);
            }
            warehouseItem.getChildren().add(aisleItem);
        }

        rootItem.getChildren().add(warehouseItem);
        rootItem.getChildren().add(buildRampsItem("Unloading ramps", 4));
        rootItem.getChildren().add(buildRampsItem("Loading ramps", 2));

        return rootItem;
    }

    private static TreeItem<String> buildRampsItem(String name, int ramps) {
        var outRampsItem = new TreeItem<>(name);
        for (int i = 0; i < ramps; i++) {
            var rampItem = new TreeItem<>("Ramp " + i);
            for (int j = 0; j < 4; j++) {
                rampItem.getChildren().add(new TreeItem<>(String.format("Item #%d", rng.nextInt(1000))));
            }
            outRampsItem.getChildren().add(rampItem);
        }
        return outRampsItem;
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

    ObservableList<JobEntry> jobs = FXCollections.observableArrayList(
            new JobEntry("Item #332", "Loading ramp 3", "Shelf A/1/3"),
            new JobEntry("Item #23", "Shelf A/2/1", "Unloading ramp 1"),
            new JobEntry("Item #929", "Shelf C/4/4", "Shelf A/1/1")
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

        var itemCol = new TableColumn<JobEntry, String>("Item");
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        queueList.getColumns().add(itemCol);
        var fromCol = new TableColumn<JobEntry, String>("From");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        queueList.getColumns().add(fromCol);
        var toCol = new TableColumn<JobEntry, String>("To");
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        queueList.getColumns().add(toCol);

        queueList.setItems(jobs);

    }

}