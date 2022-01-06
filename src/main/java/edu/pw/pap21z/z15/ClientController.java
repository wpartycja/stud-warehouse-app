package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ClientRepository;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.LocationType;
import edu.pw.pap21z.z15.db.model.Pallet;
import edu.pw.pap21z.z15.db.model.Job;
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

    private final ClientRepository repo = new ClientRepository(App.db.session);

    @FXML
    private TableView<Job> orderMenu;
    @FXML
    private TableView<Pallet> itemMenu;
    @FXML
    private ChoiceBox<Long> itemId;

    @FXML
    private void initialize() {

        List<Long> palletItems = new ArrayList<>();
        for (Pallet pallet : App.db.getPallets()) {
            palletItems.add(pallet.getId());
        }
        var contents = FXCollections.observableArrayList(palletItems);
        itemId = new ChoiceBox<>();
        itemId.getItems().addAll(contents);
        System.out.println("END OF GETTING CONTENT");
        setTables();
        System.out.println("END OF SETTING TABLES");
    }

    @FXML
    private void setTables() {
        var idColumn = new TableColumn<Job, Long>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Job, JobStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(200);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderMenu = new TableView<>();
        orderMenu.setItems(getOrders());
        orderMenu.getColumns().addAll(idColumn ,statusColumn);

        TableColumn<Pallet, Long> palletIdColumn = new TableColumn<>("ID");
        palletIdColumn.setMinWidth(100);
        palletIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(200);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        itemMenu = new TableView<>();
        itemMenu.setItems(getPallets());
        itemMenu.getColumns().addAll(palletIdColumn, descriptionColumn);
    }
    private ObservableList<Pallet> getPallets() {
        ObservableList<Pallet> pallets = FXCollections.observableArrayList();
        for (Pallet pallet : App.db.getPallets()) {
            if (pallet.getOwnerUsername().getId().equals(App.account.getId()) && pallet.getLocation().getType() == LocationType.SHELF) {
                pallets.add(pallet);}
        }
        return  pallets;
    }
    private ObservableList<Job> getOrders() {
        ObservableList<Job> jobs = FXCollections.observableArrayList();
        for (Job job : App.db.getJobs()) {
            if (job.getOrder().getClient().getId().equals(App.account.getId())) {
                 jobs.add(job); }
        }
        return  jobs;
    }
    private boolean checkInsert() { return true;}

    private void createOrder(String description) {
        long orderId = repo.insertOrder(App.account.getId(), "IN");
        long palletId = repo.insertPallet(description, App.account.getId(), 3);
        repo.insertJob(palletId, 3, orderId, "PLANNED");
    }
    private void createOutOrder(Long palletId) {
        long orderId = repo.insertOrder(App.account.getId(), "OUT");
        repo.insertJob(palletId, 4, orderId, "PLANNED");
    }

    @FXML
    private void newOrder() {
        System.out.println("TODO");
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New order");

        ChoiceBox <String> palletDescription = new ChoiceBox<>();
        palletDescription.getItems().addAll("");

        Button butConf = new Button("Confirm");
        butConf.setOnAction(e -> {
            if (checkInsert()) {
                createOrder(palletDescription.getValue());
                stage.close();
            }
            LoginController.okBox("Order Error", "Wrong order input.");
        });
        Button butCancel = new Button("Cancel");
        butCancel.setOnAction(e -> stage.close());

        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(palletDescription, butConf, butCancel);
        hbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hbox, 250, 80);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void takeOut() {
        System.out.println("TODO");
        if (itemId.getValue() == null) {
            LoginController.okBox("Take out error", "No pallet id selected to take out from warehouse.");
            return;
        }
        Long id = itemId.getValue();
        boolean ans = LoginController.yesOrNoBox("Take out confirmation", "You sure you want to take out pallet" + id + "?");
        if (ans) {
            createOutOrder(itemId.getValue());
        }
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
