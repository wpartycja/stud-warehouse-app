package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.LoginRepository;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @FXML
    private void logIn() throws IOException {
        var account = repo.getAccountByUsername(username.getText());

        // wrong username
        if (account == null) {
            App.okBox("Login Error", "Wrong username!");
            return;
        }

        // wrong password
        if (!Objects.equals(account.getPassword(), password.getText())) {
            App.okBox("Login Error", "Wrong password for username: " + username.getText());
            return;
        }

        // correct credentials
        App.okBox("Login Status", "You have logged in as: " + username.getText());
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
                App.okBox("Account error", "Login can not be empty!");
            } else if (repo.getAccountByUsername(usr.getText()) != null) {
                App.okBox("Account error", "Chosen username is already taken!");
            } else if (psw1.getText() == null) {
                App.okBox("Account error", "Password can not be empty!");
            } else if (name.getText() == null) {
                App.okBox("Account error", "Name can not be empty!");
            } else if (surname.getText() == null) {
                App.okBox("Account error", "Surname can not be empty!");
            } else if (!psw1.getText().equals(psw2.getText())) {
                App.okBox("Account error", "Password must be the same in both fields");
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

    @FXML
    private void quit() {
        App.closeProgram();
    }
}