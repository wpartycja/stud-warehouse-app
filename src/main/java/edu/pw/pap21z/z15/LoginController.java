package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.LoginRepository;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    private final LoginRepository repo = new LoginRepository(App.dbSession);

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

    public static boolean yesOrNoBox(String title, String question) {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(question);

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
        var account = repo.getAccountByUsername(username.getText());

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
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create New Account");

        TextField usr = new TextField();
        usr.setPromptText("username / login name");
        PasswordField psw1 = new PasswordField();
        psw1.setPromptText("password");
        PasswordField psw2 = new PasswordField();
        psw2.setPromptText("repeat password");
        TextField name = new TextField();
        name.setPromptText("name");
        TextField surname = new TextField();
        surname.setPromptText("surname");

        Button createAccount = new Button("Create Account");
        createAccount.setOnAction(e -> {
            if (usr.getText() == null) {
                okBox("Account error", "Login can not be empty!");
            } else if (repo.getAccountByUsername(usr.getText()) != null) {
                okBox("Account error", "Chosen username is already taken!");
            } else if (psw1.getText() == null) {
                okBox("Account error", "Password can not be empty!");
            } else if (name.getText() == null) {
                okBox("Account error", "Name can not be empty!");
            } else if (surname.getText() == null) {
                okBox("Account error", "Surname can not be empty!");
            } else if (!psw1.getText().equals(psw2.getText())) {
                okBox("Account error", "Password must be the same in both fields");
            } else {
                repo.insertAccount(usr.getText(), psw1.getText(), "CLIENT", name.getText(), surname.getText());
                stage.close();
            }
        });
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(usr, psw1, psw2, name, surname, createAccount);

        Scene scene = new Scene(layout, 350, 250);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void infoAccount() {
        LoginRepository repository = new LoginRepository(App.dbSession);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Account information");

        Label usr = new Label(App.account.getId());
        TextField name = new TextField(App.account.getName());
        TextField surname = new TextField(App.account.getSurname());

        Button saveInfo = new Button("Save");
        saveInfo.setOnAction(e -> {
            if (name.getText() == null) {
                okBox("Account error", "Name can not be empty!");
            } else if (surname.getText() == null) {
                okBox("Account error", "Surname can not be empty!");
            } else {
                repository.updateAccount(name.getText(), surname.getText(), App.account.getId());
                stage.close();
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(usr, name, surname, saveInfo);

        Scene scene = new Scene(layout, 350, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void quit() {
        App.closeProgram();
    }
}