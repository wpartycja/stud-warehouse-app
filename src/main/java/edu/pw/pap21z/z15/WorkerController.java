package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.ManagerRepository;
import edu.pw.pap21z.z15.db.WorkerRepository;
import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Job;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jboss.jandex.Main;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class WorkerController {

    @FXML
    private ListView<String> jobsListView;

    @FXML
    private TableView<Job> jobInfoTableView;

    private final Image incomingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("down-right-arrow.png")));

    private final Image outgoingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("up-right-arrow.png")));

    private final WorkerRepository repo = new WorkerRepository(App.dbSession);

    @FXML
    private void displayJobView() throws IOException {
        App.setRoot("workerJob");
    }

    private void fillJobsListView(){
        List<Job> jobsList = repo.getJobs();
        for(Job job : jobsList) {
            Account jobAssignedWorker= job.getAssignedWorker();
            try {
                String username = jobAssignedWorker.getId();
                if (username.equals("w1")) { // TODO: whos session is that
                    jobsListView.getItems().add(String.format("Job #%s", job.getId().toString()));
                }
            } catch (NullPointerException noAssignedWorker) {
                continue;
            }
        }
    }

    @FXML
    private void initialize() {
        fillJobsListView();
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}

//    public void displayInfo() {
//        jobsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            Long currentJobId = Long.valueOf(jobsListView.getSelectionModel().getSelectedItem());
////            Job currentJob = App.db.getJobById(currentJobId);
////            jobInfo.setText(currentJob.getDestination().getPath());
//            // FIXME: add WorkerRepository class
//        });
//    }