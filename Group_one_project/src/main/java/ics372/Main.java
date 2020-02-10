package ics372;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Date;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        primaryStage.setTitle("Project Assignment 1");
        GridPane mainPane = new GridPane();
        mainPane.setHgap(4);
        mainPane.setVgap(4);
        mainPane.setPadding(new Insets(5));

        ColumnConstraints colConstraint1 = new ColumnConstraints();
        colConstraint1.setHgrow(Priority.NEVER);

        ColumnConstraints colConstraint2 = new ColumnConstraints();
        colConstraint2.setHgrow(Priority.ALWAYS);

        mainPane.getColumnConstraints().addAll(colConstraint1, colConstraint2);

        RowConstraints rowConstraint1 = new RowConstraints();
        rowConstraint1.setVgrow(Priority.NEVER);

        RowConstraints rowConstraint2 = new RowConstraints();
        rowConstraint2.setVgrow(Priority.ALWAYS);

        mainPane.getRowConstraints().addAll(rowConstraint1, rowConstraint1, rowConstraint1, rowConstraint1, rowConstraint2);

        Button fileChooserButton = new Button("Choose File");
        TextArea primaryArea = new TextArea();
        primaryArea.setEditable(false);
        primaryArea.setScrollTop(Double.MAX_VALUE);
        Button printAllWarehouseShipmentsButton = new Button("Display All Shipments");
        Button closeButton = new Button("Close");
        ComboBox warehouseComboBox = new ComboBox();
        warehouseComboBox.setDisable(true);
        Label warehouseLabel = new Label("Select Warehouse from Dropdown Menu: ");
        Button disableEnableFreightButton = new Button("Disable Freight for Selected Warehouse");
        disableEnableFreightButton.setDisable(true);
        Button exportToJsonButton = new Button("Export All Shipments for Selected Warehouse To Json File");
        exportToJsonButton.setDisable(true);
        GridPane.setHalignment(printAllWarehouseShipmentsButton, HPos.RIGHT);

        mainPane.add(fileChooserButton, 0, 0);
        mainPane.add(warehouseLabel, 0, 1);
        mainPane.add(warehouseComboBox, 1, 1);
        mainPane.add(disableEnableFreightButton, 0, 2);
        mainPane.add(exportToJsonButton, 0, 3);
        mainPane.add(primaryArea, 0, 4, 4, 2);
        mainPane.add(printAllWarehouseShipmentsButton, 2, 6);
        mainPane.add(closeButton, 3, 6);
        primaryStage.setScene(new Scene(mainPane, 1000, 600));
        primaryStage.show();

        //Button handlers
        fileChooserButton.setOnAction(a -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON files", "*.json"),
                    new FileChooser.ExtensionFilter("All files", "*"));
            File file = fileChooser.showOpenDialog(primaryStage);

            if (primaryArea.getText().equals("")) {
                primaryArea.setText(controller.processJsonInputFile(file));
            } else {
                primaryArea.setText(String.format("%s%n%s",
                        primaryArea.getText(),
                        controller.processJsonInputFile(file)));
            }

            if (warehouseComboBox.getItems().size() > 0) {
                warehouseComboBox.getSelectionModel().clearSelection();
                warehouseComboBox.getItems().clear();
            }
            warehouseComboBox.setItems(FXCollections.observableArrayList(controller.getWarehouseList()));
            warehouseComboBox.getSelectionModel().selectFirst();

            //enable the controls
            warehouseComboBox.setDisable(false);
            disableEnableFreightButton.setDisable(false);
            exportToJsonButton.setDisable(false);
            primaryArea.setScrollTop(Double.MAX_VALUE);
        });

        warehouseComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue v, Object o, Object n) -> {
            if (warehouseComboBox.getValue() != null) {
                Warehouse warehouse = controller
                        .getWarehouseList()
                        .stream()
                        .filter(w -> w.getWarehouseId().equals(((Warehouse) warehouseComboBox.getValue()).getWarehouseId()))
                        .findFirst()
                        .get();

                if (warehouse.isFreightReceiptEnabled())
                    disableEnableFreightButton.setText("Disable Freight for Selected Warehouse");
                else
                    disableEnableFreightButton.setText("Enable Freight for Selected Warehouse");
            }
        });

        exportToJsonButton.setOnAction(a -> {
            String location = System.getProperty("user.dir");
            Alert alert = new Alert(Alert.AlertType.NONE);
            String fileString = "";
            if (Files.exists(Paths.get(location))) {
                Warehouse warehouse = (Warehouse) warehouseComboBox.getValue();
                try {
                    fileString = MessageFormat.format("{0}/{1}_{2}.json",
                            location,
                            warehouse.getWarehouseId(),
                            new Date().getTime());
                    Files.write(Paths.get(fileString), warehouse.exportAllShipmentsToJsonString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("JSON file has been exported to %s has been generated for warehouse: %s", fileString, warehouse.getWarehouseId()));
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText(String.format("Cannot access location: %s", fileString));
            }
            alert.show();
        });

        disableEnableFreightButton.setOnAction(a -> {
            Warehouse warehouse = controller
                    .getWarehouseList()
                    .stream()
                    .filter(w -> w.getWarehouseId().equals(((Warehouse) warehouseComboBox.getValue()).getWarehouseId()))
                    .findFirst()
                    .get();

            if (warehouse.isFreightReceiptEnabled()) {
                warehouse.disableFreightReceipt();
                disableEnableFreightButton.setText("Enable Freight for Selected Warehouse");
            } else {
                warehouse.enableFreightReceipt();
                disableEnableFreightButton.setText("Disable Freight for Selected Warehouse");
            }
        });

        printAllWarehouseShipmentsButton.setOnAction(a -> {
            primaryArea.setText(String.format("%s%n%s",
                    primaryArea.getText(),
                    controller.printAllWarehousesWithShipments()));
            primaryArea.setScrollTop(Double.MAX_VALUE);
        });

        closeButton.setOnAction(a -> {
            primaryStage.close();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
