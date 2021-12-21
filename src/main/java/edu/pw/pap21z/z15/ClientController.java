package edu.pw.pap21z.z15;

import edu.pw.pap21z.z15.db.model.Pallet;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    @FXML
    private ListView<String> warehouseContentsList;

    @FXML
    private void initialize() {

        List<String> palletItems = new ArrayList<>();
        for (Pallet pallet : App.db.getPallets()) {
            palletItems.add(pallet.getDescription());
        }
        var contents = FXCollections.observableArrayList(palletItems);
        warehouseContentsList.setItems(contents);
    }
    // wszystko trzeba zobic
    @FXML
    private void logOut() throws IOException {
        App.setRoot("login");
    }
}
