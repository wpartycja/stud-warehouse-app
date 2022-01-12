package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ClientRepository;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.LocationType;
import edu.pw.pap21z.z15.db.model.Pallet;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientController {

    private final ClientRepository repo = new ClientRepository(App.dbSession);

    @FXML
    private Label logged;                       // caption "Logged in as {name, surname}"
    @FXML
    private TableView<Job> orderMenu;           // Orders table (on the right)
    @FXML
    private TableView<Pallet> itemMenu;         // Stored items table (on the left)
    @FXML
    private ChoiceBox<Long> itemId;             // Ids of items that can be taken out
    @FXML
    private ComboBox<String> palletDescription; // In create new order window (provides description of new pallet)
    @FXML
    private TableView<Job> orderHistory;        // In show order history window (table of all jobs from client)

    private List<Job> jobs = new ArrayList<>(); // List of all jobs made by user

    @FXML
    private void initialize() {
        logged.setText("Logged in as " + App.account.getName() + " " + App.account.getSurname());
        setTables();
    }

    /*
     Sets tableview for orders and pallets. Uses getPallets() and getOrders() to load data from database
     */
    @FXML
    private void setTables() {
        // orderMenu setup
        TableColumn<Job, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(140);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Job, String> palletColumn = new TableColumn<>("Pallet ID");
        palletColumn.setMinWidth(50);
        palletColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPallet().getId().toString()));

        TableColumn<Job, String> orderColumn = new TableColumn<>("Type");
        orderColumn.setMinWidth(50);
        orderColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getType().toString()));

        orderMenu.setItems(getOrders());
        orderMenu.getColumns().clear();
        orderMenu.getColumns().addAll(idColumn, orderColumn, palletColumn, statusColumn);

        // itemMenu setup
        TableColumn<Pallet, Long> palletIdColumn = new TableColumn<>("ID");
        palletIdColumn.setMinWidth(100);
        palletIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(200);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        itemMenu.setItems(getPallets());
        itemMenu.getColumns().clear();
        itemMenu.getColumns().addAll(palletIdColumn, descriptionColumn);
    }

    /*
     Returns observable list of pallets in storage for itemMenu table.
     Sets itemId ChoiceBox values with ids of items that can be taken out.
     Sets palletDescription ComboBox with proposed descriptions that are already in stock
     */
    private ObservableList<Pallet> getPallets() {
        List<Long> palletIds = new ArrayList<>();
        Set<String> palletDescriptions = new HashSet<>();
        ObservableList<Pallet> pallets = FXCollections.observableArrayList();
        for (Pallet pallet : repo.getPallets()) {
            if (pallet.getOwnerUsername().getId().equals(App.account.getId()) && pallet.getLocation().getType() == LocationType.SHELF) {
                pallets.add(pallet);
                palletDescriptions.add(pallet.getDescription());
                List<Job> jobsForPallet = repo.getJobsForPallet(pallet.getId());
                if (jobsForPallet.isEmpty()) {
                    palletIds.add(pallet.getId());
                }
            }
        }
        var palletIdsObservable = FXCollections.observableArrayList(palletIds);
        itemId.setItems(palletIdsObservable);

        var palletDescriptionsObservable = FXCollections.observableArrayList(palletDescriptions);
        palletDescription = new ComboBox<>();
        palletDescription.getItems().addAll(palletDescriptionsObservable);
        palletDescription.setEditable(true);
        return pallets;
    }

    /*
     Returns observable list of not completed jobs for orderMenu table.
     Sets jobs list.
     */
    private ObservableList<Job> getOrders() {
        ObservableList<Job> jobsObservable = FXCollections.observableArrayList();
        jobs.clear();
        jobs = repo.getJobs();
        for (Job job : jobs) {
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() != JobStatus.COMPLETED) {
                jobsObservable.add(job);
            }
        }
        return jobsObservable;
    }

    /*
     Returns observable list of all jobs for orderMenu table. Sets jobs list.
     */
    private ObservableList<Job> getOrdersHistory() {
        ObservableList<Job> jobsObservable = FXCollections.observableArrayList();
        for (Job job : jobs) {
            if (job.getOrder().getClient().getId().equals(App.account.getId())) {
                jobsObservable.add(job);
            }
        }
        return jobsObservable;
    }

    /*
     Inserts new order, new pallet and new job to database.
     Used while putting into the warehouse.
     */
    private void createInOrder(String description) {
        repo.insertInJob(App.account.getId(), "IN", description, "PLANNED");
    }

    /*
     Inserts new order and new job to database.
     Used while taking out from the warehouse.
     */
    private void createOutOrder(Long palletId) {
        repo.insertOutJob(App.account.getId(), "OUT", palletId, "PLANNED");
    }

    /*
    Shows new window for creating order on top of previous window.
    User can create new order, put new item in warehouse.
     */
    @FXML
    private void newOrder() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New order");

        Button butConf = new Button("Confirm");
        butConf.setDefaultButton(true);
        butConf.setOnAction(e -> {
            if (palletDescription.getValue() != null) {
                createInOrder(palletDescription.getValue());
            } else {
                App.okBox("Order Error", "Wrong order input.");
            }
            stage.close();
        });

        Button butCancel = new Button("Cancel");
        butCancel.setOnAction(e -> stage.close());

        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(palletDescription, butConf, butCancel);
        hbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hbox, 350, 120);
        stage.setScene(scene);
        stage.showAndWait();

        initialize();
    }

    /*
    Creates new order, take out chosen item (in itemId box) from warehouse.
     */
    @FXML
    private void takeOut() {
        if (itemId.getValue() == null) {
            App.okBox("Take out error", "No pallet id selected to take out from warehouse.");
            return;
        }
        Long id = itemId.getValue();
        boolean ans = App.yesOrNoBox("Take out confirmation", "You sure you want to take out pallet " + id + "?");
        if (ans) {
            createOutOrder(itemId.getValue());
        }
        initialize();
    }

    /*
    Shows orderHistory TableView with information from all jobs in new window.
     */
    @FXML
    private void showHistory() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order history");

        TableColumn<Job, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(80);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Job, Long> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setMinWidth(50);
        orderIdColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getId()));

        TableColumn<Job, String> orderColumn = new TableColumn<>("Type");
        orderColumn.setMinWidth(50);
        orderColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getOrder().getType().toString()));

        TableColumn<Job, String> palletColumn = new TableColumn<>("Pallet ID");
        palletColumn.setMinWidth(50);
        palletColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPallet().getId().toString()));

        TableColumn<Job, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(70);
        descriptionColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPallet().getDescription()));

        TableColumn<Job, String> locationColumn = new TableColumn<>("Destination");
        locationColumn.setMinWidth(200);
        locationColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDestination().getPath()));

        orderHistory = new TableView<>();
        orderHistory.setItems(getOrdersHistory());
        orderHistory.getColumns().clear();
        orderHistory.getColumns().addAll(idColumn, statusColumn, orderColumn, orderIdColumn, palletColumn, descriptionColumn, locationColumn);
        orderHistory.setMaxWidth(610);

        Scene scene = new Scene(orderHistory);
        stage.setScene(scene);
        stage.showAndWait();
    }

    // Menu buttons
    @FXML
    private void refresh() {
        initialize();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void quit() {
        App.closeProgram();
    }

    @FXML
    private void info() {
        App.infoAccount();
    }

    @FXML
    private void edit() {
        App.editAccount();
    }
}
