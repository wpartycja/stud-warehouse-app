package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ManagerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ManagerController {

    @FXML
    private TreeView<Object> ordersList;

    @FXML
    private TableView<Account> workersList;

    @FXML
    private TreeView<String> contentsTree;

    @FXML
    private Label loggedLabel;

    private final Image incomingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("down-right-arrow.png")));

    private final Image outgoingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("up-right-arrow.png")));

    private final ManagerRepository repo = new ManagerRepository(App.dbSession);


    @FXML
    private void initialize() {
        // set up top bar
        loggedLabel.setText(App.account.getName() + " " + App.account.getSurname());

        // set up pallets view
        contentsTree.setShowRoot(false);

        // set up jobs view
        ordersList.setCellFactory(cell -> new JobListTreeCell());
        ordersList.setShowRoot(false);

        // set up workers view
        workersList.getColumns().setAll(
                createTableColumn("Name", 100, worker -> worker.getName() + " " + worker.getSurname()),
                createTableColumn("Job", 300, worker -> {
                    var job = worker.getCurrentJob();
                    if (job == null) return "Idle";
                    return String.format("#%d: %s -> %s", job.getId(), job.getPallet().getDescription(), job.getDestination().getPath());
                }));

        // populate views
        updatePalletsView();
        updateJobsView();
        updateWorkersView();
    }


    /**
     * @param name        Column header name
     * @param prefWidth   Prefered width of the column
     * @param contentFunc Function to extract value in this column for given row
     * @param <S>         Type of items in the table
     * @return Created column
     */
    private <S> TableColumn<S, String> createTableColumn(String name, int prefWidth, Function<S, Object> contentFunc) {
        var col = new TableColumn<S, String>(name);
        col.setPrefWidth(prefWidth);
        col.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(contentFunc.apply(cell.getValue()).toString()));
        return col;
    }

    /**
     * Get child of parent TreeItem with given value, or create it if it doesn't exist
     *
     * @param parentNode parent TreeItem
     * @param childValue value to search for
     * @return found or created child item
     */
    private TreeItem<String> getOrCreateChild(TreeItem<String> parentNode, String childValue) {
        // search for existing children
        var existingNode = parentNode.getChildren().stream()
                .filter(i -> Objects.equals(i.getValue(), childValue)).findAny();

        // child exists, return it
        if (existingNode.isPresent()) return existingNode.get();

        // crate new child, attach it to parent and return
        var childItem = new TreeItem<>(childValue);
        parentNode.getChildren().add(childItem);
        return childItem;
    }

    private void updatePalletsView() {

        TreeItem<String> rootItem = new TreeItem<>();
        for (var location : repo.getLocations()) {
            // traverse tree
            var path = location.getPath().split("/");
            var node = rootItem;
            for (String s : path) node = getOrCreateChild(node, s);

            // create pallet items and add to tree
            var palletItems = location.getPallets().stream()
                    .map(pallet -> new TreeItem<>(pallet.getDescription())).collect(Collectors.toList());
            node.getChildren().setAll(palletItems);
        }

        contentsTree.setRoot(rootItem);
    }

    private void updateWorkersView() {
        workersList.setItems(FXCollections.observableArrayList(repo.getWorkers()));
    }

    private void updateJobsView() {
        var ordersRoot = new TreeItem<>();
        for (Order order : repo.getOrders()) {
            // get all unfinished jobs for order
            var incompleteJobs = order.getJobs().stream()
                    .filter(job -> job.getStatus() != JobStatus.COMPLETED).collect(Collectors.toList());

            // if order is finished, don't show it. else, create order and job items
            if (!incompleteJobs.isEmpty()) {
                var orderItem = new TreeItem<Object>(order);
                ordersRoot.getChildren().add(orderItem);
                orderItem.setExpanded(true);
                incompleteJobs.forEach(job -> orderItem.getChildren().add(new TreeItem<>(job)));
            }
        }

        ordersList.setRoot(ordersRoot);
    }

    /**
     * Show a popup window with table of all past jobs
     */
    @FXML
    private void showHistory() {
        // create table
        var orderHistoryTable = new TableView<Job>();
        orderHistoryTable.getColumns().setAll(
                createTableColumn("Order ID", 50, job -> job.getOrder().getId()),
                createTableColumn("Status", 80, Job::getStatus),
                createTableColumn("Type", 50, job -> job.getOrder().getType()),
                createTableColumn("Client ID", 50, job -> job.getOrder().getClient().getId()),
                createTableColumn("Pallet ID", 50, job -> job.getPallet().getId()),
                createTableColumn("Description", 70, job -> job.getPallet().getDescription()),
                createTableColumn("Job ID", 50, Job::getId),
                createTableColumn("Destination", 200, job -> job.getDestination().getPath()));
        orderHistoryTable.setItems(FXCollections.observableArrayList(repo.getJobs()));

        // create pupup
        Scene scene = new Scene(orderHistoryTable);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order history");
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void sessionRefresh() {
        updateWorkersView();
        updateJobsView();
        updatePalletsView();
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

    private class JobListTreeCell extends TreeCell<Object> {
        @Override
        protected void updateItem(Object data, boolean empty) {
            super.updateItem(data, empty);
            // reset previously set values
            setText(null);
            setGraphic(null);
            setContextMenu(null);
            setStyle(null);
            // empty row, nothing to show
            if (empty || data == null) return;

            if (data instanceof Order) {
                // order row, show icon and order id
                Order order = (Order) data;

                // choose icon based on order type
                ImageView icon = new ImageView(order.getType() == OrderType.IN ? incomingIcon : outgoingIcon);
                icon.setFitHeight(10);
                icon.setFitWidth(10);

                setText(String.format("Order #%s", order.getId().toString()));
                setGraphic(icon);
            } else if (data instanceof Job) {
                // job row
                Job job = (Job) data;
                // all statuses will have a menu
                ContextMenu menu = new ContextMenu();
                setContextMenu(menu);

                // what's displayed depends on the status of the job
                if (job.getStatus() == JobStatus.PLANNED) {
                    // add menu item for each location that this job can be assigned to
                    for (Location dest : repo.getAvailableDestinations(job)) {
                        MenuItem menuItem = new MenuItem("To " + dest.getPath());
                        menuItem.setOnAction(actionEvent -> {
                            repo.scheduleJob(job, dest);
                            updateJobsView();
                        });
                        menu.getItems().add(menuItem);
                    }

                    setText(String.format("Move pallet #%s to ???", job.getPallet().getId()));
                    setStyle("-fx-background-color: LightPink");
                } else if (job.getStatus() == JobStatus.PENDING) {
                    // add menu item to reset this job's destination
                    MenuItem resetMenuItem = new MenuItem("Reset destination");
                    resetMenuItem.setOnAction(actionEvent -> {
                        repo.unscheduleJob(job);
                        updateJobsView();
                    });

                    // add menu item for each worker that this job can be assigned to
                    menu.getItems().add(resetMenuItem);
                    for (Account worker : repo.getIdleWorkers()) {
                        MenuItem workerItem = new MenuItem("Assign to " + worker.getName() + " " + worker.getSurname());
                        workerItem.setOnAction(actionEvent -> {
                            repo.assignJobToWorker(job, worker);
                            updateJobsView();
                            updateWorkersView();
                        });
                        menu.getItems().add(workerItem);
                    }

                    setStyle("-fx-background-color: PaleGoldenRod");
                    setText(String.format("Move pallet #%s to %s", job.getPallet().getId(), job.getDestination().getPath()));
                } else if (job.getStatus() == JobStatus.IN_PROGRESS) {
                    // add menu item to unassign worker
                    MenuItem unassignMenuItem = new MenuItem("Unassign worker");
                    unassignMenuItem.setOnAction(actionEvent -> {
                        repo.unassignWorker(job);
                        updateJobsView();
                        updateWorkersView();
                    });
                    menu.getItems().add(unassignMenuItem);

                    setText(String.format("Move pallet #%s to %s", job.getPallet().getId(), job.getDestination().getPath()));
                }
            } else {
                throw new RuntimeException("Order list item of invalid type " + data);
            }
        }
    }

}