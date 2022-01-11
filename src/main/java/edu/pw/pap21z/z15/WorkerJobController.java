package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.WorkerRepository;
import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.Job;
import edu.pw.pap21z.z15.db.model.JobStatus;
import edu.pw.pap21z.z15.db.model.LocationType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class WorkerJobController{

    @FXML
    Label jobIdLabel;

    @FXML
    Label firstLabel;

    @FXML
    Label secondLabel;

    @FXML
    Label thirdLabel;

    private final WorkerRepository repo = new WorkerRepository(App.dbSession);

    @FXML
    private void initialize(){
        App.stage.setFullScreen(true);
        Account currentWorker = App.account;

        Job currentJob = currentWorker.getCurrentJob();

        jobIdLabel.setText("#" + currentJob.getId());
        String[] path = currentJob.getDestination().getPath().split("/");
        firstLabel.setText(path[0]);
        secondLabel.setText(path[1]);
        if (currentJob.getDestination().getType() == LocationType.SHELF){
            thirdLabel.setText(path[2]);
        }
    }

    @FXML
    private void confirmJob() throws IOException {
        // TODO: changes in database (change JobStatus)
        App.setRoot("worker");
        App.stage.setFullScreen(false);
    }

    @FXML
    private void cancelJob() throws IOException {
        // TODO: changes in database (change JobStatus)
        App.setRoot("worker");
        App.stage.setFullScreen(false);
    }
}