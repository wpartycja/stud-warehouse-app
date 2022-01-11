package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ClientRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientController {

    private final ClientRepository repo = new ClientRepository(App.dbSession);

    @FXML
    private Label logged;
    @FXML
    private TableView<Job> orderMenu;
    @FXML
    private TableView<Pallet> itemMenu;
    @FXML
    private ChoiceBox<Long> itemId;
    @FXML
    private ComboBox<String> palletDescription;
    @FXML
    private TableView<Job> orderHistory;

    private List<Job> jobs = new ArrayList<>();

    @FXML
    private void initialize() {
        logged.setText("Logged in as " + App.account.getName() + " "  + App.account.getSurname());
        setTables();
    }

    @FXML
    private void setTables() {
        TableColumn<Job, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(140);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Job, Long> orderIdColumn = new TableColumn<>("Order Id");
        orderIdColumn.setMinWidth(50);
        orderIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Job, Long> jobPalletCellDataFeatures) {
                return new ReadOnlyObjectWrapper(jobPalletCellDataFeatures.getValue().getOrder().getId());
            }
        });

        TableColumn<Job, String> orderColumn = new TableColumn<>("Order type");
        orderColumn.setMinWidth(50);
        orderColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Job, String> jobPalletCellDataFeatures) {
                return new ReadOnlyObjectWrapper(jobPalletCellDataFeatures.getValue().getOrder().getType());
            }
        });

        orderMenu.setItems(getOrders());
        orderMenu.getColumns().clear();
        orderMenu.getColumns().add(idColumn);
        orderMenu.getColumns().add(orderColumn);
        orderMenu.getColumns().add(orderIdColumn);
        orderMenu.getColumns().add(statusColumn);

        TableColumn<Pallet, Long> palletIdColumn = new TableColumn<>("ID");
        palletIdColumn.setMinWidth(100);
        palletIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(200);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        itemMenu.setItems(getPallets());
        itemMenu.getColumns().clear();
        itemMenu.getColumns().add(palletIdColumn);
        itemMenu.getColumns().add(descriptionColumn);
    }

    private ObservableList<Pallet> getPallets() {
        List<Long> palletIds = new ArrayList<>();
        Set<String> palletDescriptions = new HashSet<>();
        ObservableList<Pallet> pallets = FXCollections.observableArrayList();
        for (Pallet pallet : repo.getPallets()) {
            if (pallet.getOwnerUsername().getId().equals(App.account.getId()) && pallet.getLocation().getType() == LocationType.SHELF) {
                pallets.add(pallet);
                palletDescriptions.add(pallet.getDescription());
                List<Job> jobsForPallet = repo.getJobsForPallet(pallet.getId());
                if (jobsForPallet.isEmpty()) {palletIds.add(pallet.getId());}
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

    private ObservableList<Job> getOrders() {
        ObservableList<Job> jobsObservable = FXCollections.observableArrayList();
        ObservableList<Job> jobsHistory = FXCollections.observableArrayList();
        jobs.clear();
        jobs = repo.getJobs();
        for (Job job : jobs) {
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() != JobStatus.COMPLETED) {
                jobsObservable.add(job);
            }
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() == JobStatus.COMPLETED) {
                jobsHistory.add(job);
            }
        }
        return jobsObservable;
    }

    private ObservableList<Job> getOrdersHistory() {
        ObservableList<Job> jobsObservable = FXCollections.observableArrayList();
        for (Job job : repo.getJobs()) {
            if (job.getOrder().getClient().getId().equals(App.account.getId())) {
                jobsObservable.add(job);
            }
        }
        return jobsObservable;
    }

    private void createInOrder(String description) {
        repo.insertInJob(App.account.getId(), "IN", description, "PLANNED");
    }

    private void createOutOrder(Long palletId) {
        repo.insertOutJob(App.account.getId(), "OUT", palletId,  "PLANNED");
    }

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
                LoginController.okBox("Order Error", "Wrong order input.");
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

    @FXML
    private void takeOut() {
        if (itemId.getValue() == null) {
            LoginController.okBox("Take out error", "No pallet id selected to take out from warehouse.");
            return;
        }
        Long id = itemId.getValue();
        boolean ans = LoginController.yesOrNoBox("Take out confirmation", "You sure you want to take out pallet " + id + "?");
        if (ans) {
            createOutOrder(itemId.getValue());
        }
        initialize();
    }

    @FXML
    private void showHistory() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order history");

        TableColumn<Job, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMaxWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMaxWidth(140);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Job, Long> orderIdColumn = new TableColumn<>("Order Id");
        orderIdColumn.setMinWidth(50);
        orderIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, Long>, ObservableValue<Long>>() {
            @Override
            public ObservableValue<Long> call(TableColumn.CellDataFeatures<Job, Long> jobPalletCellDataFeatures) {
                return new ReadOnlyObjectWrapper(jobPalletCellDataFeatures.getValue().getOrder().getId());
            }
        });

        TableColumn<Job, String> orderColumn = new TableColumn<>("Order type");
        orderColumn.setMaxWidth(50);
        orderColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Job, String> jobPalletCellDataFeatures) {
                return new ReadOnlyObjectWrapper(jobPalletCellDataFeatures.getValue().getOrder().getType());
            }
        });

        TableColumn<Job, String> descriptionColumn = new TableColumn<>("Pallet");
        descriptionColumn.setMaxWidth(140);
        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Job, String> jobPalletCellDataFeatures) {
                return new ReadOnlyObjectWrapper(jobPalletCellDataFeatures.getValue().getPallet().getDescription());
            }
        });

        TableColumn<Job, String> locationColumn = new TableColumn<>("Destination");
        locationColumn.setMinWidth(180);
        locationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Job, String>, ObservableValue<String>>() {
               @Override
               public ObservableValue<String> call(TableColumn.CellDataFeatures<Job, String> jobStringCellDataFeatures) {
                   return new ReadOnlyObjectWrapper(jobStringCellDataFeatures.getValue().getDestination().getPath());
               }
           });

        orderHistory = new TableView<>();
        orderHistory.setItems(getOrdersHistory());
        orderHistory.getColumns().clear();
        orderHistory.getColumns().addAll(idColumn, statusColumn, orderColumn, orderIdColumn, descriptionColumn, locationColumn);

        Scene scene = new Scene(orderHistory);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void logOut() throws IOException { App.setRoot("login"); }
    @FXML
    private void refresh() { initialize(); }
    @FXML
    private void quit() { App.closeProgram(); }
    @FXML
    private void info() { LoginController.infoAccount(); }
    @FXML
    private void edit() { LoginController.editAccount(); }

}
