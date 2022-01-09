package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.model.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jboss.jandex.Main;

import java.io.IOException;
import java.util.Objects;

public class WorkerController {

    @FXML
    private ListView<String> jobsListView;

    @FXML
    private TableView<Job> jobInfoTableView;

    private Main main;

    Stage stage = new Stage();

    public void displayJob() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WorkerController.class.getResource("./workerJob.fxml"));
        BorderPane jobDisplayPane = loader.load();

        Stage jobDisplayStage = new Stage();
        jobDisplayStage.setFullScreen(true);
        jobDisplayStage.setMaximized(true);
        jobDisplayStage.setResizable(false);
        jobDisplayStage.setTitle("Current job");
        jobDisplayStage.initModality(Modality.APPLICATION_MODAL);
        jobDisplayStage.initOwner(stage);
        Scene scene = new Scene(jobDisplayPane);
        jobDisplayStage.setScene(scene);
        jobDisplayStage.showAndWait();

        initialize();
    }


//    public void displayJob(){
//        //TODO: change JobStatus
//        Stage stage = new Stage();
//        stage.setMaximized(true);
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setTitle("Current Job"); // TODO: title ? of the job
//
//        Button doneButton = new Button("Done");
//        doneButton.setDefaultButton(true);
//        doneButton.setOnAction(e-> {
//            // TODO: changes in database (change JobStatus)
//            stage.close();
//        });
//
//        Button cancelButton= new Button("Cancel");
//        cancelButton.setOnAction(e -> {
//            // TODO: changes in database (change JobStatus)
//            stage.close();
//        });
//
//        VBox vbox = new VBox((40));
//        vbox.getChildren().addAll(doneButton, cancelButton);
//
//        Scene scene = new Scene (vbox);
//        stage.setScene(scene);
//        stage.showAndWait();
//
//        initialize();
//    }

//    public void displayInfo() {
//        jobsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            Long currentJobId = Long.valueOf(jobsListView.getSelectionModel().getSelectedItem());
////            Job currentJob = App.db.getJobById(currentJobId);
////            jobInfo.setText(currentJob.getDestination().getPath());
//            // FIXME: add WorkerRepository class
//        });
//    }

    public void initialize() {
//        for (Job job : App.db.getJobs()) {
//            jobsListView.getItems().add(job.getId().toString());
//        }
        // FIXME: add WorkerRepository class
        //this.displayInfo();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void confirmJob() {
        // TODO: changes in database (change JobStatus)
        stage.close();
    }

    @FXML
    private void cancelJob() {
        // TODO: changes in database (change JobStatus)
        stage.close();
    }
}
