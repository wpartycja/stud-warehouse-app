package edu.pw.pap21z.z15;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Random;

public class ManagerController {

    @FXML
    private ListView<String> queueList;

    @FXML
    private ListView<String> workersList;

    @FXML
    private TreeView<String> contentsTree;

    private static TreeItem<String> buildContentsTree() {
        var rng = new Random();
        var rootItem = new TreeItem<String>();

        var warehouseItem = new TreeItem<>("Warehouse");
        for (int k = 0; k < 4; k++) {
            var aisleItem = new TreeItem<>("Aisle " + (char) ('A' + k));
            for (int j = 0; j < 4; j++) {
                var rackItem = new TreeItem<>("Rack " + j);
                for (int i = 0; i < 4; i++) {
                    var shelfItem = new TreeItem<String>();
                    if (rng.nextBoolean()) {
                        shelfItem.setValue(String.format("Shelf %d", i));
                        shelfItem.setExpanded(true);
                        shelfItem.getChildren().add(new TreeItem<>(String.format("Pallet #%d", rng.nextInt(1000))));
                    } else {
                        shelfItem.setValue(String.format("Shelf %d (empty)", i));
                    }
                    rackItem.getChildren().add(shelfItem);
                }
                aisleItem.getChildren().add(rackItem);
            }
            warehouseItem.getChildren().add(aisleItem);
        }

        rootItem.getChildren().add(warehouseItem);
        rootItem.getChildren().add(buildRampsItem(rng, "Unloading ramps", 4));
        rootItem.getChildren().add(buildRampsItem(rng, "Loading ramps", 2));

        return rootItem;
    }

    private static TreeItem<String> buildRampsItem(Random rng, String name, int ramps) {
        var outRampsItem = new TreeItem<>(name);
        for (int i = 0; i < ramps; i++) {
            var rampItem = new TreeItem<>("Ramp " + i);
            for (int j = 0; j < 4; j++) {
                rampItem.getChildren().add(new TreeItem<>(String.format("Pallet #%d", rng.nextInt(1000))));
            }
            outRampsItem.getChildren().add(rampItem);
        }
        return outRampsItem;
    }


    @FXML
    private void initialize() {
        contentsTree.setRoot(buildContentsTree());
        contentsTree.setShowRoot(false);
    }

}