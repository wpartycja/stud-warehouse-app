package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.Dept;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage primary) throws IOException {

        EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("oracledb");
        EntityManager entityManager = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Dept> depts = entityManager.createQuery("from Dept", Dept.class).getResultList();
        for (Dept dept : depts) {
            System.out.printf("%s, %s, %s%n", dept.getDeptNo(), dept.getName(), dept.getLocation());
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        // set starting scene
        scene = new Scene(loadFXML("login"), 640, 480);
        // configure stage
        stage = primary;
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
        boolean answer = LoginController.confirmExit();
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

    public static void main(String[] args) {
        launch();
    }

}