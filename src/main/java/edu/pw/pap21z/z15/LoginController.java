package edu.pw.pap21z.z15;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    static boolean answer;

    public static void okBox(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(message);

        Button ok = new Button("ok");

        ok.setOnAction(e -> stage.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, ok);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 80);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static boolean confirmExit() {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Exit");

        Label label = new Label("Sure you want to exit?");

        Button yes = new Button("yes");
        Button no = new Button("no");

        yes.setOnAction(e -> {
            answer = true;
            stage.close();
        });
        no.setOnAction(e -> {
            answer = false;
            stage.close();
        });

        VBox layoutTop = new VBox();
        layoutTop.getChildren().add(label);
        layoutTop.setAlignment(Pos.BOTTOM_CENTER);
        HBox layoutBot = new HBox(30);
        layoutBot.getChildren().addAll(yes, no);
        layoutBot.setAlignment(Pos.CENTER);

        BorderPane borderpane = new BorderPane();
        borderpane.setTop(layoutTop);
        borderpane.setCenter(layoutBot);

        Scene scene = new Scene(borderpane, 250, 80);
        stage.setScene(scene);
        stage.showAndWait();

        return answer;
    }

    @FXML
    private void logIn() throws IOException {
        var account = App.db.getAccountByUsername(username.getText());

        // wrong username
        if (account == null) {
            okBox("Login Error", "Wrong username!");
            return;
        }

        // wrong password
        if (!Objects.equals(account.getPassword(), password.getText())) {
            okBox("Login Error", "Wrong password for username: " + username.getText());
            return;
        }

        // correct credentials
        okBox("Login Status", "You have logged in as: " + username.getText());
        App.setAccount(account);

    }

    @FXML
    private void create() {
        System.out.println("TODO");
    }

    @FXML
    private void quit() {
        App.closeProgram();
    }
}