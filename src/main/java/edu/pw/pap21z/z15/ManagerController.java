package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ManagerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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
        for (Account worker : repo.getWorkers()) {
            String status;
            if (worker.getCurrentJob() == null) {
                status = "Idle";
            } else {
                Job job = worker.getCurrentJob();
                status = String.format("#%d: %s -> %s", job.getId(), job.getPallet().getDescription(), job.getDestination().getPath());
            }
            workers.add(new WorkerEntry(worker.getName() + " " + worker.getSurname(), status));
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
            var incompleteJobs = order.getJobs().stream()
                    .filter(job -> job.getStatus() != JobStatus.COMPLETED)
                    .collect(Collectors.toList());
            if (!incompleteJobs.isEmpty()) {
                var orderItem = new TreeItem<Object>(order);
                ordersRoot.getChildren().add(orderItem);
                orderItem.setExpanded(true);
                for (Job job : order.getJobs()) {
                    if (job.getStatus() != JobStatus.COMPLETED) {
                        TreeItem<Object> jobItem = new TreeItem<>(job);
                        orderItem.getChildren().add(jobItem);
                        jobItem.setExpanded(true);
                    }
                }
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
                    switch (job.getStatus()) {

                        case PLANNED:

                            for (Location dest : repo.getAvailableDestinations(job)) {
                                MenuItem menuItem = new MenuItem("To " + dest.getPath());
                                menuItem.setOnAction(actionEvent -> {
                                            repo.scheduleJob(job, dest);
                                            sessionRefresh();
                                        }
                                );
                                menu.getItems().add(menuItem);
                            }

                            setText(String.format("Move pallet #%s to ???", job.getPallet().getId()));
                            setContextMenu(menu);
                            setStyle("-fx-background-color: LightPink");
                            break;
                        case PENDING:
                            MenuItem resetMenuItem = new MenuItem("Reset destination");
                            resetMenuItem.setOnAction(actionEvent -> {
                                        repo.unscheduleJob(job);
                                        sessionRefresh();
                                    }
                            );

                            if (job.getAssignedWorker() == null) {
                                menu.getItems().add(resetMenuItem);
                                for (Account worker : repo.getIdleWorkers()) {
                                    MenuItem workerItem = new MenuItem("Assign to " + worker.getName() + " " + worker.getSurname());
                                    workerItem.setOnAction(actionEvent -> {
                                        repo.assignJobToWorker(job, worker);
                                        sessionRefresh();
                                    });
                                    menu.getItems().add(workerItem);
                                }
                            }

                            setStyle("-fx-background-color: PaleGoldenRod");
                            setContextMenu(menu);
                            setText(String.format("Move pallet #%s to %s", job.getPallet().getId(), job.getDestination().getPath()));
                            break;
                        case IN_PROGRESS:
                            MenuItem unassignMenuItem = new MenuItem("Unassign worker");
                            unassignMenuItem.setOnAction(actionEvent -> {
                                        repo.unassignWorker(job);
                                        sessionRefresh();
                                    }
                            );
                            menu.getItems().add(unassignMenuItem);
                            setContextMenu(menu);
                            setText(String.format("Move pallet #%s to %s", job.getPallet().getId(), job.getDestination().getPath()));
                            break;
                        case COMPLETED:
                            break;
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
    private void showHistory() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order history");


        TableColumn<Job, Long> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setMinWidth(50);
        orderIdColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getId()));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(80);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Job, String> orderColumn = new TableColumn<>("Type");
        orderColumn.setMinWidth(50);
        orderColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getType().toString()));

        TableColumn<Job, String> clientColumn = new TableColumn<>("Client ID");
        clientColumn.setMinWidth(50);
        clientColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getClient().getId().toString()));

        TableColumn<Job, String> palletColumn = new TableColumn<>("Pallet ID");
        palletColumn.setMinWidth(50);
        palletColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPallet().getId().toString()));

        TableColumn<Job, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(70);
        descriptionColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPallet().getDescription()));

        TableColumn<Job, Long> idColumn = new TableColumn<>("Job ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, String> locationColumn = new TableColumn<>("Destination");
        locationColumn.setMinWidth(200);
        locationColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDestination().getPath()));

        var orderHistory = new TableView<Job>();
        orderHistory.setItems(getOrdersHistory());
        orderHistory.getColumns().clear();
        orderHistory.getColumns().addAll(orderIdColumn, statusColumn, orderColumn, clientColumn, palletColumn, descriptionColumn, idColumn, locationColumn);
        orderHistory.setMaxWidth(650);

        Scene scene = new Scene(orderHistory);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private ObservableList<Job> getOrdersHistory() {
        ObservableList<Job> jobsObservable = FXCollections.observableArrayList();
        jobsObservable.addAll(repo.getJobs());
        return jobsObservable;
    }

    @FXML
    private void sessionRefresh() {
        repo.clear();
        initialize();
    }

    @FXML
    private void sessionLogOut() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void sessionExit() {
        App.closeProgram();
    }

    @FXML
    private void accountInfo() {
        App.infoAccount();
    }

    @FXML
    private void accountEdit() {
        App.editAccount();
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