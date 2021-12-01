package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.DataBaseClient;
import edu.pw.pap21z.z15.db.Job;
import edu.pw.pap21z.z15.db.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkerController {

    @FXML
    protected ListView<String> jobsListView;

    @FXML
    protected Text jobInfo;

    String currentJobName;
    Job currentJob;

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    private DataBaseClient dbClient = new DataBaseClient();



    public void switchToWorkerTaskScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("workerTask.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void displayInfo() {
        jobsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentJobName = jobsListView.getSelectionModel().getSelectedItem();
                for (Job job : dbClient.getJobData()) {
                    if (job.getJobName() == currentJobName) {
                        currentJob = job;
                        break;
                    }
                }

                for (Location loc : dbClient.getLocationData()) {
                    if (loc.getLocationId() == currentJob.getDestinationLocationId()) {
                        String path = loc.getPath();
                        jobInfo.setText(path);
                    }
                }
            }
        });
    }

    public ArrayList<String> getJobNameList (){
        List<Job> JobList = dbClient.getJobData();
        ArrayList<String> jobNameList = new ArrayList<String>();
        for (var job : JobList){
            jobNameList.add(job.getJobName());
        }
        return jobNameList;
    }


    public void initialize() {
        jobsListView.getItems().addAll(this.getJobNameList());
        this.displayInfo();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
