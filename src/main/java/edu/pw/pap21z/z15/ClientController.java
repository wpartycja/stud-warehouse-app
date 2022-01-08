package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ClientRepository;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.LocationType;
import edu.pw.pap21z.z15.db.model.Pallet;
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
import java.util.List;

public class ClientController {

    private final ClientRepository repo = new ClientRepository(App.dbSession);

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

    private final List<Long> usedIds = new ArrayList();

    @FXML
    private void initialize() {
        setTables();
    }

    @FXML
    private void setTables() {
        TableColumn<Job, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(200);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderMenu.setItems(getOrders());
        orderMenu.getColumns().clear();
        orderMenu.getColumns().addAll(idColumn, statusColumn);

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

    private ObservableList<Pallet> getPallets() {
        List<Long> palletIds = new ArrayList<>();
        List<String> palletDescriptions = new ArrayList<>();
        ObservableList<Pallet> pallets = FXCollections.observableArrayList();
        for (Pallet pallet : repo.getPallets()) {
            if (pallet.getOwnerUsername().getId().equals(App.account.getId()) && pallet.getLocation().getType() == LocationType.SHELF) {
                pallets.add(pallet);
                palletIds.add(pallet.getId());
                palletDescriptions.add(pallet.getDescription());
            }
        }
        var palletIdsObservable = FXCollections.observableArrayList(palletIds);
        itemId.setItems(palletIdsObservable);
        itemId.getItems().removeAll(usedIds);

        var palletDescriptionsObservable = FXCollections.observableArrayList(palletDescriptions);
        palletDescription = new ComboBox<>();
        palletDescription.getItems().addAll(palletDescriptionsObservable);
        palletDescription.setEditable(true);
        return pallets;
    }

    private ObservableList<Job> getOrders() {
        ObservableList<Job> jobs = FXCollections.observableArrayList();
        ObservableList<Job> jobsHistory = FXCollections.observableArrayList();
        for (Job job : repo.getJobs()) {
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() != JobStatus.COMPLETED) {
                jobs.add(job);
            }
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() == JobStatus.COMPLETED) {
                jobsHistory.add(job);
            }
        }
        return jobs;
    }

    private ObservableList<Job> getOrdersHistory() {
        ObservableList<Job> jobs = FXCollections.observableArrayList();
        for (Job job : repo.getJobs()) {
            if (job.getOrder().getClient().getId().equals(App.account.getId()) && job.getStatus() == JobStatus.COMPLETED) {
                jobs.add(job);
            }
        }
        return jobs;
    }

    private boolean checkInsert() {
        return true;
    }

    private void createInOrder(String description) {
        repo.insertInJob(App.account.getId(), "IN", description, App.account.getId(),
                3, 9, "PLANNED");
    }

    private void createOutOrder(Long palletId) {
        repo.insertOutJob(App.account.getId(), "OUT", palletId, 10, "PLANNED");
    }

    @FXML
    private void newOrder() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New order");

        Button butConf = new Button("Confirm");
        butConf.setOnAction(e -> {
            if (checkInsert()) {
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
        boolean ans = LoginController.yesOrNoBox("Take out confirmation", "You sure you want to take out pallet" + id + "?");
        if (ans) {
            createOutOrder(itemId.getValue());
        }
        usedIds.add(id);
        initialize();
    }

    @FXML
    private void showHistory() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order history");

        TableColumn<Job, Long> idColumnHistory = new TableColumn<>("ID");
        idColumnHistory.setMinWidth(100);
        idColumnHistory.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumnHistory = new TableColumn<>("Status");
        statusColumnHistory.setMinWidth(200);
        statusColumnHistory.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderHistory = new TableView<>();
        orderHistory.setItems(getOrders());
        orderHistory.getColumns().clear();
        orderHistory.getColumns().addAll(idColumnHistory, statusColumnHistory);

        Scene scene = new Scene(orderHistory, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
