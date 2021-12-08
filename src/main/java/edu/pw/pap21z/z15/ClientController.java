package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.DataBaseClient;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    private final DataBaseClient dbClient = new DataBaseClient();

    @FXML
    private ListView<String> warehouseContentsList;

    @FXML
    private void initialize() {

        List<String> items = new ArrayList<>();
        for (edu.pw.pap21z.z15.db.Item i : dbClient.getItemData()) {
            items.add(i.getDescription());
        }
        var contents = FXCollections.observableArrayList(items);
        warehouseContentsList.setItems(contents);
    }

    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
