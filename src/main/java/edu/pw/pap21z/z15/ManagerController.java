package edu.pw.pap21z.z15;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class ManagerController {

    List<List<String>> aisle = List.of(
            List.of("Półka 1", "Półka 2", "Półka 3"),
            List.of("Półka 1", "Półka 2", "Półka 3"),
            List.of("Półka 1", "Półka 2", "Półka 3")
    );

    @FXML
    private TreeView<String> warehouseTree;

    @FXML
    private void initialize() {

        TreeItem<String> warehouseItem = new TreeItem<>("Magazyn");

        TreeItem<String> aisleItem = new TreeItem<>("Alejka");
        for (List<String> rack : aisle) {
            TreeItem<String> rackTreeItem = new TreeItem<>("Regał");
            for (String level : rack) {
                TreeItem<String> levelTreeItem = new TreeItem<>(level);
                rackTreeItem.getChildren().add(levelTreeItem);
            }
            aisleItem.getChildren().add(rackTreeItem);
        }

        warehouseItem.getChildren().add(aisleItem);
        warehouseTree.setShowRoot(false);
        warehouseTree.setRoot(warehouseItem);
    }
}