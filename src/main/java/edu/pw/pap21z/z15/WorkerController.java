package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.WorkerRepository;
import edu.pw.pap21z.z15.db.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    private final Image incomingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("down-right-arrow.png")));

    private final Image outgoingIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("up-right-arrow.png")));

    public WorkerJobInfo tJob_id = new WorkerJobInfo("Job id: ", "");
    public WorkerJobInfo tPallet_id = new WorkerJobInfo("Pallet id: ", "");
    public WorkerJobInfo tDescription = new WorkerJobInfo("Description:", "");
    public WorkerJobInfo tOwner = new WorkerJobInfo("Owner: ", "");
    public WorkerJobInfo tLoc1 = new WorkerJobInfo("Location: ", "");
    public WorkerJobInfo tLoc2 = new WorkerJobInfo("", "");
    public WorkerJobInfo tLoc3 = new WorkerJobInfo("", "");

    private final ObservableList<WorkerJobInfo> jobWorkerJobInfoList = FXCollections.observableArrayList(
            tJob_id, tPallet_id, tDescription, tOwner, tLoc1, tLoc2, tLoc3
    );

    /**
     * sets starting items to TableView with jobs
     **/
    private void emptyJobInfo(){
        propertiesCol.setCellValueFactory(new PropertyValueFactory<>("property"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("data"));

        jobInfoTableView.setItems(jobWorkerJobInfoList);
    }

    /**
     * sets ListView with jobs + adds arrow images
     */
    private void fillJobsListView(){
        List<Job> jobsList = repo.getPendingJobs();
        for(Job job : jobsList) {
            jobsListView.getItems().add(String.format("Job #%s", job.getId().toString()));
        }
        jobsListView.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    int job_id = Integer.parseInt(name.split("#")[1]);
                    Job currJob = repo.getJobById(job_id);
                    if (currJob.getOrder().getType() == OrderType.IN) {
                        imageView.setImage(incomingIcon);
                        setText(name);
                        setGraphic(imageView);
                    } else if (currJob.getOrder().getType() == OrderType.OUT) {
                        imageView.setImage(outgoingIcon);
                        setText(name);
                        setGraphic(imageView);
                    }
                }
            }
        });
    }

    /**
     * sets properties of selected job into ObservableList<WorkerJobInfo>
     *
     * @param job - selected by user job (from the ListView)
     */
    private void setJobTableView(Job job){
        Pallet pallet= job.getPallet();
        Location location = job.getDestination();
        String[] path = location.getPath().split("/");

        tJob_id.setData("#"+job.getId().toString());
        tPallet_id.setData("#"+pallet.getId().toString());
        tDescription.setData(pallet.getDescription());
        if (pallet.getOwnerUsername().getName() == null){
            tOwner.setData(pallet.getOwnerUsername().getSurname());
        } else if (pallet.getOwnerUsername().getSurname() == null){
            tOwner.setData(pallet.getOwnerUsername().getName());
        } else{
            tOwner.setData(pallet.getOwnerUsername().getName() + " "+ pallet.getOwnerUsername().getSurname());
        }
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

    /**
     * displays job properties in TableView
     */
    public void displayJobInfo() {
        jobsListView.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String currentJobStr = jobsListView.getSelectionModel().getSelectedItem();
                if (currentJobStr != null) {
                    int job_id = Integer.parseInt(currentJobStr.substring(currentJobStr.lastIndexOf("#") + 1));
                    Job currentJob = repo.getJobById(job_id);
                    selectedJob = currentJob;
                    setJobTableView(currentJob);
                }
            }
        }));
    }

    /**
     * changes for the workerJob view
     */
    @FXML
    private void displayJobView() throws IOException {
        if (selectedJob != null){
            repo.startJob(selectedJob, App.account.getId());
            App.setRoot("workerJob");
        }
    }


    @FXML
    private void initialize() {
        loggedLabel.setText(App.account.getName() + " " + App.account.getSurname());
        fillJobsListView();
        emptyJobInfo();
        displayJobInfo();
    }

    @FXML
    private void sessionRefresh() throws IOException { App.setRoot("worker"); }

    @FXML
    private void sessionLogOut() throws IOException { App.setRoot("login"); }

    @FXML
    private void sessionExit() { App.closeProgram(); }

    @FXML
    private void accountInfo() { LoginController.infoAccount(); }

    @FXML
    private void accountEdit() { LoginController.editAccount(); }


    /**
     * class needed to add data to TableView
     */
    public static class WorkerJobInfo {
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
