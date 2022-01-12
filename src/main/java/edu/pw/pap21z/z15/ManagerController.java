package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ManagerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ManagerController {

    @FXML
    private TreeView<Object> ordersList;

    @FXML
    private TableView<WorkerEntry> workersList;

    @FXML
    private TreeView<String> contentsTree;

    @FXML
    private Label loggedLabel;

    private final Image incomingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("down-right-arrow.png")));

    private final Image outgoingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("up-right-arrow.png")));

    private final ManagerRepository repo = new ManagerRepository(App.dbSession);

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
        loggedLabel.setText(App.account.getName() + " " + App.account.getSurname());
        contentsTree.setShowRoot(false);
        contentsTree.setRoot(buildContentsTree());


        var nameCol = new TableColumn<WorkerEntry, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        var statusCol = new TableColumn<WorkerEntry, String>("Job");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        workersList.getColumns().clear();
        workersList.getColumns().add(nameCol);
        workersList.getColumns().add(statusCol);

        workersList.setItems(getWorkersList());


        var ordersRoot = new TreeItem<>();
        for (Order order : repo.getOrders()) {
            var orderItem = new TreeItem<Object>(order);
            ordersRoot.getChildren().add(orderItem);
            orderItem.setExpanded(true);
            for (Job job : order.getJobs()) {
                TreeItem<Object> jobItem = new TreeItem<>(job);
                orderItem.getChildren().add(jobItem);
                jobItem.setExpanded(true);
            }
        }

        ordersList.setCellFactory(d -> new TreeCell<>() {
            @Override
            protected void updateItem(Object data, boolean empty) {
                super.updateItem(data, empty);
                setText(null);
                setGraphic(null);
                setContextMenu(null);
                setStyle(null);
                if (!empty && data instanceof Order) {
                    Order order = (Order) data;
                    ImageView icon = new ImageView(order.getType() == OrderType.IN ? incomingIcon : outgoingIcon);
                    icon.setFitHeight(10);
                    icon.setFitWidth(10);

                    setText(String.format("Order #%s", order.getId().toString()));
                    setGraphic(icon);
                } else if (!empty && data instanceof Job) {
                    Job job = (Job) data;
                    ContextMenu menu = new ContextMenu();
                    if (job.getStatus() == JobStatus.PLANNED) {
                        for (Location dest : repo.getAvailableDestinations(job)) {
                            MenuItem menuItem = new MenuItem("To " + dest.getPath());
                            menuItem.setOnAction(actionEvent -> {
                                        repo.scheduleJob(job, dest);
                                        initialize();
                                    }
                            );
                            menu.getItems().add(menuItem);
                        }
                        setText(String.format("Move pallet #%s to ???", job.getPallet().getId()));
                        setContextMenu(menu);
                        setStyle("-fx-background-color: LightPink");
                    } else {
                        MenuItem menuItem = new MenuItem("Reset destination");
                        menu.getItems().add(menuItem);
                        menuItem.setOnAction(actionEvent -> {
                                    repo.unscheduleJob(job);
                                    initialize();
                                }
                        );
                        setContextMenu(menu);
                        setText(String.format("Move pallet #%s to %s", job.getPallet().getId(), job.getDestination().getPath()));
                    }
                } else if (!empty) {
                    throw new RuntimeException("Order list item of invalid type " + data);
                }
            }
        });
        ordersList.setShowRoot(false);
        ordersList.setRoot(ordersRoot);

    }


    @FXML
    private void sessionRefresh(){ initialize(); }

    @FXML
    private void sessionLogOut() throws IOException { App.setRoot("login"); }

    @FXML
    private void sessionExit() { App.closeProgram(); }

    @FXML
    private void accountInfo() { App.infoAccount(); }

    @FXML
    private void accountEdit() { App.editAccount(); }

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