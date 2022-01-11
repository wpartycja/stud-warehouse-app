package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.WorkerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class WorkerController {

    @FXML
    private ListView<String> jobsListView;

    @FXML
    private TableView<WorkerJobInfo> jobInfoTableView;

    @FXML
    private TableColumn<WorkerJobInfo, String> propertiesCol;

    @FXML
    private TableColumn<WorkerJobInfo, String> infoCol;

    @FXML
    private Label loggedLabel;

    public Job selectedJob = null;

    private final WorkerRepository repo = new WorkerRepository(App.dbSession);

    public WorkerJobInfo tJob_id = new WorkerJobInfo("Job id: ", "");
    public WorkerJobInfo tPallet_id = new WorkerJobInfo("Pallet id: ", "");
    public WorkerJobInfo tDescription = new WorkerJobInfo("Description:", "");
    public WorkerJobInfo tOwner = new WorkerJobInfo("Owner: ", "");
    public WorkerJobInfo tLoc1 = new WorkerJobInfo("Location: ", "");
    public WorkerJobInfo tLoc2 = new WorkerJobInfo("", "");
    public WorkerJobInfo tLoc3 = new WorkerJobInfo("", "");

    private ObservableList<WorkerJobInfo> jobWorkerJobInfoList = FXCollections.observableArrayList(
            tJob_id, tPallet_id, tDescription, tOwner, tLoc1, tLoc2, tLoc3
    );

    private void emptyJobInfo(){
        propertiesCol.setCellValueFactory(new PropertyValueFactory<WorkerJobInfo, String>("property"));
        infoCol.setCellValueFactory(new PropertyValueFactory<WorkerJobInfo, String>("data"));

        jobInfoTableView.setItems(jobWorkerJobInfoList);

    }

    private void displayJobInfo(Job job){
        Pallet pallet= job.getPallet();
        Location location = job.getDestination();
        String[] path = location.getPath().split("/");

        tJob_id.setData("#"+job.getId().toString());
        tPallet_id.setData("#"+pallet.getId().toString());
        tDescription.setData(pallet.getDescription());
        tOwner.setData(pallet.getOwnerUsername().getName() + " "+ pallet.getOwnerUsername().getSurname());
        if (location.getType() == LocationType.SHELF){
            tLoc1.setData(path[0]);
            tLoc2.setData(path[1]);
            tLoc3.setData(path[2]);
        }
        else if (location.getType() == LocationType.IN_RAMP || location.getType() == LocationType.OUT_RAMP){
            tLoc1.setData(path[0]);
            tLoc2.setData(path[1]);
            tLoc3.setData("");
        }

        jobInfoTableView.refresh();
    }

    public void displayInfo() {
        jobsListView.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String currentJobStr = jobsListView.getSelectionModel().getSelectedItem();
                int job_id = Integer.parseInt(currentJobStr.substring(currentJobStr.lastIndexOf("#")+1));
                Job currentJob = repo.getJobById(job_id);
                selectedJob = currentJob;
                displayJobInfo(currentJob);
            }
        }));
    }

    @FXML
    private void displayJobView() throws IOException {
        if (selectedJob != null){
            repo.startJob(selectedJob, App.account.getId());
            App.setRoot("workerJob");
        }
    }


    private void fillJobsListView(){
        List<Job> jobsList = repo.getPendingJobs();
        for(Job job : jobsList) {
           jobsListView.getItems().add(String.format("Job #%s", job.getId().toString()));
        }
    }


    @FXML
    private void initialize() {
        loggedLabel.setText(App.account.getName() + " " + App.account.getSurname());
        fillJobsListView();
        emptyJobInfo();
        displayInfo();
    }

    @FXML
    private void sessionRefresh(){ initialize(); }

    @FXML
    private void sessionLogOut() throws IOException { App.setRoot("login"); }

    @FXML
    private void sessionExit() { App.closeProgram(); }

    @FXML
    private void accountInfo() { LoginController.infoAccount(); }

    @FXML
    private void accountEdit() { LoginController.editAccount(); }

    public class WorkerJobInfo {
        private String property;
        private String data;

        private WorkerJobInfo(String property, String data){
            this.property = property;
            this.data = data;
        }

        public String getProperty() {
            return property;
        }

        public String getData() {
            return data;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
