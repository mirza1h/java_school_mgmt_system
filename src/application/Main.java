package application;

import java.awt.Color;
import java.awt.Polygon;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sun.javafx.scene.control.skin.Utils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import models.Korisnik.tipKorisnika;
import models.Predmet;
import models.Profesor;
import models.Termin;

public class Main extends Application {

	private static final String PERSISTENCE_UNIT_NAME = "RazvojSoftvera";
	private static EntityManagerFactory factory;
	public static EntityManagerFactory getFactory() {
		return factory;
	}
	private static void setDBSystemDir() {
		// Decide on the db system directory: <userhome>/.addressbook/
		String userHomeDir = System.getProperty("user.home", ".");
		String systemDir = userHomeDir + "/.addressbook";
		// Set the db system directory.
		System.setProperty("derby.system.home", systemDir);
	}

	public List<String> getDates() {

		List<String> weekDates = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		weekDates.add(df.format(c.getTime()));
		for (int i = 0; i < 6; i++) {
			c.add(Calendar.DATE, 1);
			weekDates.add(df.format(c.getTime()));
		}

		return weekDates;
	}

	public AnchorPane newBlock(AnchorPane block) {
		AnchorPane novi = new AnchorPane();

		novi.setStyle(block.getStyle());
		novi.setLayoutY(105);
		novi.setPrefHeight(block.getPrefHeight());
		novi.setPrefWidth(block.getPrefWidth());

		List<Label> info = new ArrayList<>();
		info.add(new Label("Pisp"));
		info.add(new Label("RI"));
		info.add(new Label("Stelekt"));

		for (int i = 0; i < 3; ++i) {
			info.get(i).getStyleClass().add("copyable-label");
			info.get(i).setPrefWidth(140);
			info.get(i).setLayoutX(0);
			info.get(i).setLayoutY(i*15);
			novi.getChildren().add(info.get(i));
		}
		return novi;
	}

	public void startLoginPage(Stage primaryStage) throws IOException {
		VBox login = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene loginScene = new Scene(login);

		loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);

		primaryStage.setScene(loginScene);
		primaryStage.show();

		AnchorPane mainPane = (AnchorPane) login.getChildren().get(0);
		AnchorPane duals = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane loginPane = (AnchorPane) duals.getChildren().get(1);

		Button bezPrijave = (Button) loginPane.getChildren().get(1);

		bezPrijave.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        try {
					startRasporedPage(primaryStage, false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
	}

	public void startRasporedPage(Stage primaryStage, boolean registrovan) throws IOException {

		VBox root = FXMLLoader.load(getClass().getResource("raspored.fxml"));
		Scene scene = new Scene(root);

		ScrollPane scrollPane = (ScrollPane) root.getChildren().get(0);
		AnchorPane mainPane = (AnchorPane) scrollPane.getContent();
		AnchorPane weekDaysPane = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane drawPane = (AnchorPane) mainPane.getChildren().get(3);
		AnchorPane defaultBlock = (AnchorPane) drawPane.getChildren().get(12);

		AnchorPane novi = newBlock(defaultBlock);

		defaultBlock.setVisible(true);

		/*
		 * factory =
		 * Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		 * EntityManager em = factory.createEntityManager();
		 * setDBSystemDir();
		 */

		drawPane.getChildren().add(novi);

		List<Node> allDays = weekDaysPane.getChildren();

		List<String> weekDates = getDates();
		for (int i = 0; i < 6; ++i) {
			AnchorPane day = (AnchorPane) allDays.get(i);
			Label dateLabel = (Label) day.getChildren().get(1);
			dateLabel.setText(weekDates.get(i));

		}
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) {

		try {

			startLoginPage(primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
