import java.io.File;
import javafx.application.Application;
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

public class UIDisplay extends Application {

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
		Label Label1 = new Label("Label 1");
		Label Label2 = new Label("File Chooser");
		Label Label3 = new Label("Label 3");
		Label Label4 = new Label("Label 4");
		Label Label5 = new Label("Label 5");
		Label Label6 = new Label("Label 6");

		// Textfields
		TextField TextField1 = new TextField();
		TextField TextField2 = new TextField();
		TextArea TextArea1 = new TextArea();
		TextArea TextArea12 = new TextArea();

		// Buttons
		Button Button1 = new Button("Button 1");
		Button Button2 = new Button("Choose File");
		Button Button3 = new Button("Button 3");
		Button Button4 = new Button("Button 4");
		Button Button5 = new Button("Button 5");
		Button Button6 = new Button("Button 6");

		// Layout
		center.getChildren().add(Label1);
		center.getChildren().add(TextField1);
		center.getChildren().add(Button1);
		left.getChildren().add(Label2);
		left.getChildren().add(Button2);
		left.getChildren().add(TextField2);

		// Event Handling
		Button1.setOnAction(e -> {
			System.out.println("Button 1 press.");
			TextField1.setText("Button1 pressed.");
		});
		FileChooser fileChooser = new FileChooser();
		Button2.setOnAction(e -> {
			File file1 = fileChooser.showOpenDialog(primaryStage);
			System.out.println("Button 2 press.");
			TextField2.setText("Button2 pressed.");
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
