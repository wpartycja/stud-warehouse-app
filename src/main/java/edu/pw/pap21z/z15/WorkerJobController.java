package edu.pw.pap21z.z15;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WorkerJobController{


    @FXML
    private void initialize() throws IOException {
          App.stage.setFullScreen(true);
    }
//
//        jobDisplayScene = new Scene(workerJobView);
//
//        jobDisplayStage = new Stage();
//        jobDisplayStage.setFullScreen(true);
//        //jobDisplayStage.setMaximized(true);
//        //jobDisplayStage.setResizable(false);
//        jobDisplayStage.setTitle("Current job");
//
//        jobDisplayStage.setScene(jobDisplayScene);
//        jobDisplayStage.showAndWait();
//    }
//

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(WorkerJobController.class.getResource("./workerJob.fxml"));
//        BorderPane jobDisplayPane = loader.load();

//
//        Stage jobDisplayStage = new Stage();
//        jobDisplayStage.setFullScreen(true);
//        jobDisplayStage.setMaximized(true);
//        jobDisplayStage.setResizable(false);
//        jobDisplayStage.setTitle("Current job");
//        jobDisplayStage.initModality(Modality.APPLICATION_MODAL);
//        jobDisplayStage.initOwner(primaryStage);
//        Scene scene = new Scene(jobDisplayPane);
//        jobDisplayStage.setScene(scene);
//        jobDisplayStage.showAndWait();




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