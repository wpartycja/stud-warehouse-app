package edu.pw.pap21z.z15;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WorkerController{

    @FXML
    private ListView<String> tasksListView;

    @FXML
    private CheckBox taskCheckBox;

    @FXML
    private Text taskInfo;


    String currentTaskName;
    Task currentTask;

    private Stage stage;
    private Scene scene;
    private Parent root;


    private class Task{

        private final String name;
        private ArrayList<String>  itemList;
        private String startLocation;
        private String finalLocation;

        Task(String name, ArrayList<String> itemList, String startLocation, String finalLocation){
            this.name = name;
            this.itemList = itemList;
            this.startLocation = startLocation;
            this.finalLocation = finalLocation;
        }

        public String getName() {
            return name;
        }

        public ArrayList<String> getItemList() {
            return itemList;
        }

        public String getStartLocation() {
            return startLocation;
        }

        public String getFinalLocation() {
            return finalLocation;
        }

        public String showLocation(String loc){
                String[] locList = loc.split("/");
                return "Aisle: " + locList[0] + "\nRack: " + locList[1] + "\nShelf: " + locList[2] + "\n";
        }

        public String showItems(){
            String items = "Items:\n";
            for (var item: itemList){
                items += "*" + item + "\n";
            }
            return items;
        }

        public String showInfo(){
            String start_loc = "\nFrom:\n" + showLocation(this.startLocation);
            String final_loc = "\nTo:\n" + showLocation(finalLocation);
            return this.name + "\n" +  start_loc + final_loc;
        }
    }

    public void switchToWorkerScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("worker.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToWorkerTaskScene(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("workerTask.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void initialize(){
        //initializing everything
        ArrayList<String> itemList1 = new ArrayList<String>(Arrays.asList("Spinka", "Garnek", "Patelnia"));
        ArrayList<String> itemList2 = new ArrayList<String>(Arrays.asList("Suszarka", "lokówka", "lodówka"));
        ArrayList<String> itemList3 = new ArrayList<String>(Arrays.asList("Sok", "Myszka", "Siekiera"));

        Task task1 = new Task("task1", itemList1, "0/7/5", "2/3/7");
        Task task2 = new Task("task2", itemList2, "7/9/5", "5/3/1");
        Task task3 = new Task("task3", itemList2, "5/9/11", "2/3/1");

        ArrayList<String> taskNameList = new ArrayList<String>(Arrays.asList(task1.name, task2.name, task3.name));
        ArrayList<Task> taskList = new ArrayList<Task>(Arrays.asList(task1, task2, task3));


        // displaying details about task
        tasksListView.getItems().addAll(taskNameList);
        tasksListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentTaskName = tasksListView.getSelectionModel().getSelectedItem();

                for (Task task : taskList){
                    if (task.name == currentTaskName){
                        currentTask = task;
                        break;
                    }
                }

                taskInfo.setText(currentTask.showInfo());

            }
        });

    }
}
