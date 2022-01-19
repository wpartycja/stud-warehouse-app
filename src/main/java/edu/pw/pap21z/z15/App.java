package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.LoginRepository;
import edu.pw.pap21z.z15.db.model.Account;
import edu.pw.pap21z.z15.db.model.AccountType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private static Scene scene;
    public static Stage stage;

    public static Account account = null;
    private static final EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("oracledb");
    public static final EntityManager dbSession = sessionFactory.createEntityManager();

    private final Image warehouseIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("warehouse-icon.png")));

    @Override
    public void start(Stage primaryStage) throws IOException {

        // set starting scene
        scene = new Scene(loadFXML("login"), 800, 600);
        // configure stage
        stage = primaryStage;
        stage.setScene(scene);
        stage.setTitle("Warehouse management app");

        // confirmation of X button
        stage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        stage.getIcons().add(warehouseIcon);

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
        boolean answer = yesOrNoBox("Exit", "Sure you want to exit?");
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

    // utility functions used by controllers
    public static void okBox(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(message);

        Button ok = new Button("ok");
        ok.setDefaultButton(true);

        ok.setOnAction(e -> stage.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, ok);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 80);
        stage.setScene(scene);
        stage.showAndWait();
    }

    static boolean answer;

    public static boolean yesOrNoBox(String title, String question) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Label label = new Label(question);

        Button yes = new Button("yes");
        yes.setDefaultButton(true);
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
        layoutTop.setPadding(new Insets(10, 10, 10, 10));
        layoutTop.getChildren().add(label);
        layoutTop.setAlignment(Pos.BOTTOM_CENTER);
        HBox layoutBot = new HBox(30);
        layoutBot.getChildren().addAll(yes, no);
        layoutBot.setAlignment(Pos.CENTER);

        BorderPane borderpane = new BorderPane();
        borderpane.setTop(layoutTop);
        borderpane.setCenter(layoutBot);

        Scene scene = new Scene(borderpane, 300, 80);
        stage.setScene(scene);
        stage.showAndWait();

        return answer;
    }

    public static void infoAccount() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Account information");

        Label usr = new Label("Username:\t" + App.account.getId());
        Label name = new Label("Name:\t\t" + App.account.getName());
        Label surname = new Label("Surname:\t\t" + App.account.getSurname());

        Button close = new Button("Close");
        close.setOnAction(e -> stage.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(usr, name, surname, close);
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 300, 150);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void editAccount() {
        LoginRepository repository = new LoginRepository(App.dbSession);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit information");

        Label nameLabel = new Label("New name:");
        TextField name = new TextField(App.account.getName());
        Label surnameLabel = new Label("New surname:");
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
        layout.getChildren().addAll(nameLabel, name, surnameLabel, surname, saveInfo);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }


}