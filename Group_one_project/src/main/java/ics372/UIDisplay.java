package ics372;

import java.io.File;
import java.text.MessageFormat;

import javafx.application .Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;

public class UIDisplay extends Application {
    File fileInput;
    String fileName;

    public void start(Stage primaryStage) {
        // Primary pane layout is BorderPane.
        BorderPane primaryPane = new BorderPane();

        // Types of panes.
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        VBox center = new VBox();
        Object test = new HBox();

        // Primary Pane layout.
        primaryPane.setCenter(center);
        primaryPane.setTop(top);
        primaryPane.setBottom(bottom);
        primaryPane.setLeft(left);
        primaryPane.setRight(right);

        // settings
        // Max setting: 800width, 700height
        primaryPane.setStyle("-fx-background-color: cyan;");
        primaryPane.setPadding(new Insets(10, 10, 10, 10));

        top.setStyle("-fx-background-color: yellow;");
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setPrefSize(100, 100);

        bottom.setStyle("-fx-background-color: green;");
        bottom.setPadding(new Insets(10, 10, 10, 10));
        bottom.setPrefSize(100, 100);

        left.setStyle("-fx-background-color: red;");
        left.setPadding(new Insets(10, 10, 10, 10));
        left.setPrefSize(200, 100);

        right.setStyle("-fx-background-color: orange;");
        right.setPadding(new Insets(10, 10, 10, 10));
        right.setPrefSize(200, 100);

        center.setStyle("-fx-background-color: pink;");
        center.setPadding(new Insets(10, 10, 10, 10));
        center.setPrefSize(200, 100);

        // Labels
        Label centerLabel = new Label("Center TextArea");
        Label fileChooserLabel = new Label("File Chooser");
        Label Label3 = new Label("Label 3");
        Label Label4 = new Label("Label 4");
        Label Label5 = new Label("Label 5");
        Label Label6 = new Label("Label 6");

        // Textfields
        TextField TextField1 = new TextField();
        TextField TextField2 = new TextField();
        TextArea centerTA = new TextArea();
        TextArea TextArea2 = new TextArea();

        // Buttons
        Button Button1 = new Button("Button 1");
        Button fileChooserBTN = new Button("Choose File");
        Button addIncomingShipmentBTN = new Button("addIncomingShipment");
        Button enableFreightReceiptBTN = new Button("enableFreightReceipt");
        Button endFreightReceiptBTN = new Button("endFreightReceiptBTN");

        // Layout
        left.getChildren().add(fileChooserLabel);
        left.getChildren().add(fileChooserBTN);
        left.getChildren().add(TextField2);
        center.getChildren().add(centerLabel);
        center.getChildren().add(centerTA);
        bottom.getChildren().add(addIncomingShipmentBTN);
        bottom.getChildren().add(enableFreightReceiptBTN);
        bottom.getChildren().add(endFreightReceiptBTN);

        // Event Handling
        // Event Handling

        // ADD THE LOGIC TO ADD INCOMING SHIPMENT
        addIncomingShipmentBTN.setOnAction(e -> {
            //ENTER LOGIC HERE
            //Warehouse wh = new Warehouse();
            //wh.add(input);
            String input;
            input = JOptionPane.showInputDialog("Enter Warehouse ID:");
            String warehouse_id = input;
            input = JOptionPane.showInputDialog("Enter Shipment ID:");
            String shipment_id = input;
            input = JOptionPane.showInputDialog("Enter shipment method ID:");
            String shipment_method = input;
            input = JOptionPane.showInputDialog("Enter weight:");
            Double weight = Double.parseDouble(input);
            input = JOptionPane.showInputDialog("Enter receipt date ID:");
            Long receipt_date = Long.parseLong(input);

            Shipment shipment = new Shipment(warehouse_id,shipment_id,shipment_method,weight,receipt_date);
            Warehouse warehouse = new Warehouse(warehouse_id);
            warehouse.addShipment(shipment);

            //String ship_id = JOptionPane.showInputDialog("Enter Shipment ID:");
            centerTA.setText("addIncomingShipmentBTN " +
                    "\n" + warehouse_id + "\n" + shipment_id + "\n" + shipment_method + "\n" + weight + "\n" + receipt_date);
        });

        // ADD THE LOGIC TO ENABLE FREIGHT RECEIPT
        enableFreightReceiptBTN.setOnAction(e -> {
            //ENTER LOGIC HERE
            centerTA.setText("enableFreightReceiptBTN press");

        });

        // ADD END FREIGHT RECEIPT
        endFreightReceiptBTN.setOnAction(e -> {
            //ENTER LOGIC HERE
            centerTA.setText("endFreightReceiptBTN press");
        });


        fileChooserBTN.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            Controller controller = new Controller();

            //This is selecting the JSON file
            File fileToBeRead = fileChooser.showOpenDialog(primaryStage);

            //read in all json shipment files from "input" directory and move them to "processed" directory after processing;
//
            String msg = controller.processJsonInputFile(fileToBeRead.toString(),
                    System.getProperty("user.home") + "/Desktop");
//            System.out.println(msg);

            centerTA.appendText(msg);

            //get the first warehouse in the array and export its shipments to output directory
            if (controller.getWarehouseList().isEmpty())
                return;

            Warehouse warehouse = controller.getWarehouseList().get(0);
            warehouse.exportAllShipmentsToFile(MessageFormat.format("{0}/output", System.getProperty("user.dir")));

            //show the list of all shipments for each warehouse
            centerTA.appendText("\n\nShow the list of all shipments for each warehouse: ");
            centerTA.appendText(controller.printAllWarehousesWithShipments());
            System.out.println("\n\nShow the list of all shipments for each warehouse: ");
            System.out.println(controller.printAllWarehousesWithShipments());

            System.out.println("Button 2 press.");
            TextField2.setText("Button 2 pressed.");
        });

        Button1.setOnAction(e -> {
            System.out.println("Button 1 press.");
            TextField1.setText("Button1 pressed.");
        });
        // Logic


        // All three below are the 3 statement that has to be at the bottom of
        // the start method.
        Scene primaryScene = new Scene(primaryPane, 800, 700);
        primaryStage.setScene(primaryScene);

        // Prepare the stage
        primaryStage.setTitle("Program Assignment 1");
        primaryStage.show();

    }

    // public static void main(String[] args) {
    // launch(args);
    // }
}
