import edu.pw.pap21z.z15.App;
import edu.pw.pap21z.z15.WorkerController;
import edu.pw.pap21z.z15.db.model.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
class WorkerTest {
    
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) throws IOException {

        Account testAcccount = new Account();
        testAcccount.setName("testuser");
        testAcccount.setSurname("testpass");
        App.account = testAcccount;

        FXMLLoader loader = new FXMLLoader(App.class.getResource("worker.fxml"));
        Parent root = loader.load();
        WorkerController controller = loader.getController(); // todo mock controller here for other tests

        stage.setScene(new Scene(root, 0, 0));
        stage.show();
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void shouldDisplayUsername(FxRobot robot) {
        Assertions.assertThat(robot.lookup("#loggedLabel").queryAs(Label.class)).hasText("testuser testpass");
    }

}