package edu.pw.pap21z.z15;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ManagerController {

    WarehouseNode rootNode = new WarehouseNode("Magazyn",
            new WarehouseNode("Alejka 1",
                    new WarehouseNode("Regał 1",
                            new WarehouseNode("Półka 1"),
                            new WarehouseNode("Półka 2")),
                    new WarehouseNode("Regał 2",
                            new WarehouseNode("Półka 1"),
                            new WarehouseNode("Półka 2"))),
            new WarehouseNode("Alejka 2",
                    new WarehouseNode("Regał 1",
                            new WarehouseNode("Półka 1"),
                            new WarehouseNode("Półka 2")),
                    new WarehouseNode("Regał 2",
                            new WarehouseNode("Półka 1"),
                            new WarehouseNode("Półka 2"))));

    @FXML
    private TreeView<WarehouseNode> warehouseTree;

    @FXML
    private void initialize() {

        var rootItem = new WarehouseTreeItem(rootNode);
        warehouseTree.setShowRoot(false);
        warehouseTree.setRoot(rootItem);

    }

    static class WarehouseNode {
        String name;
        WarehouseNode[] children;

        public WarehouseNode(String name, WarehouseNode... children) {
            this.name = name;
            this.children = children;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class WarehouseTreeItem extends TreeItem<WarehouseNode> {
        boolean loaded = false;

        public WarehouseTreeItem(WarehouseNode warehouseNode) {
            super(warehouseNode);
        }

        @Override
        public ObservableList<TreeItem<WarehouseNode>> getChildren() {
            if (!loaded) {
                System.out.println("Loading node " + getValue().name);
                for (var node : getValue().children) {
                    super.getChildren().add(new WarehouseTreeItem(node));
                }
                loaded = true;
            }
            return super.getChildren();
        }

        @Override
        public boolean isLeaf() {
            return getValue().children.length == 0;
        }
    }
}