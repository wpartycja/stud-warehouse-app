package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ManagerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ManagerController {

    private final ManagerRepository repo = new ManagerRepository(App.db.session);

    @FXML
    private TreeView<String> ordersList;

    @FXML
    private TableView<WorkerEntry> workersList;

    @FXML
    private TreeView<String> contentsTree;

    private final Image incomingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("down-right-arrow.png")));

    private final Image outgoingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("up-right-arrow.png")));

    private TreeItem<String> getOrCreateChild(TreeItem<String> node, String childValue) {
        var existingNode = node.getChildren().stream().filter(i -> Objects.equals(i.getValue(), childValue)).findAny();
        if (existingNode.isPresent()) return existingNode.get();

        var childItem = new TreeItem<>(childValue);
        node.getChildren().add(childItem);
        return childItem;
    }

    private TreeItem<String> buildContentsTree() {
        TreeItem<String> rootItem = new TreeItem<>();
        var locations = repo.getLocations();
        for (var location : locations) {
            var node = rootItem;
            var path = location.getPath().split("/");
            for (String s : path) node = getOrCreateChild(node, s);
            for (Pallet i : location.getPallets()) {
                node.getChildren().add(new TreeItem<>(i.getDescription()));
            }
        }
        return rootItem;
    }

    public ObservableList<WorkerEntry> getWorkersList() {
        ArrayList<WorkerEntry> workers = new ArrayList<>();
        for (Account emp : repo.getWorkers()) {
            String status = emp.getCurrentJob() == null ? "Idle" : emp.getCurrentJob().getId().toString();
            workers.add(new WorkerEntry(emp.getName(), status));
        }
        return FXCollections.observableArrayList(workers);
    }

    @FXML
    private void initialize() {
        contentsTree.setShowRoot(false);
        contentsTree.setRoot(buildContentsTree());


        var nameCol = new TableColumn<WorkerEntry, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        workersList.getColumns().add(nameCol);
        var statusCol = new TableColumn<WorkerEntry, String>("Job");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        workersList.getColumns().add(statusCol);

        workersList.setItems(getWorkersList());


        var ordersRoot = new TreeItem<String>();
        for (Order order : repo.getOrders()) {
            ImageView icon = new ImageView(order.getType() == OrderType.IN ? incomingIcon : outgoingIcon);
            icon.setFitHeight(10);
            icon.setFitWidth(10);
            var orderItem = new TreeItem<>(String.format("Order #%s", order.getId().toString()), icon);
            for (Job job : order.getJobs()) {
                String jobDescription = String.format("Move pallet #%s to %s",
                        job.getPallet().getId(),
                        job.getDestination().getPath());
                orderItem.getChildren().add(new TreeItem<>(jobDescription));
            }
            ordersRoot.getChildren().add(orderItem);
        }
        ordersList.setShowRoot(false);
        ordersList.setRoot(ordersRoot);

    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
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
}