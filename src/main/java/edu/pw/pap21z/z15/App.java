package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.AccountType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    public static Account account = null;
    private static final EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("oracledb");
    public static final EntityManager dbSession = sessionFactory.createEntityManager();

    @Override
    public void start(Stage primaryStage) throws IOException {

        // set starting scene
        scene = new Scene(loadFXML("login"), 640, 480);
        // configure stage
        stage = primaryStage;
        stage.setScene(scene);
        stage.setTitle("PAP21Z-Z15");

        // confirmation of X button
        stage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        stage.show();
    }

    /**
     * Change fxml root of current scene
     *
     * @param fxml file name without .fxml
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Close program with confirmation box [yes/no]
     */
    public static void closeProgram() {
        boolean answer = LoginController.yesOrNoBox("Exit", "Sure you want to exit?");
        if (answer) {
            System.out.println("The program is closing");
            stage.close();
        }
    }

    /**
     * @param fxml file name without .fxml
     * @return Loaded parent node
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setAccount(Account newAccount) throws IOException {
        account = newAccount;

        if (account == null) setRoot("login");
        else if (account.getType() == AccountType.MANAGER) setRoot("manager");
        else if (account.getType() == AccountType.CLIENT) setRoot("client");
        else if (account.getType() == AccountType.WORKER) setRoot("worker");
        else throw new IllegalArgumentException("Invalid account type");
    }

    public static void main(String[] args) {
        launch();
    }

}