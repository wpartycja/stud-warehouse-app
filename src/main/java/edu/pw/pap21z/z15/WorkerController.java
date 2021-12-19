package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.model.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WorkerController {

    @FXML
    protected ListView<String> jobsListView;

    @FXML
    protected Text jobInfo;

    protected Stage stage;
    protected Scene scene;

    public void switchToWorkerTaskScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("workerTask.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void displayInfo() {
        jobsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Long currentJobId = Long.valueOf(jobsListView.getSelectionModel().getSelectedItem());
            Job currentJob = App.db.getJobById(currentJobId);
            jobInfo.setText(currentJob.getDestination().getPath());
        });
    }

    public void initialize() {
        for (Job job : App.db.getJobs()) {
            jobsListView.getItems().add(job.getId().toString());
        }
        this.displayInfo();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
