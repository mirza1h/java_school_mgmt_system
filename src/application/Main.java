package application;

import java.awt.Color;
import java.awt.Polygon;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

import models.Korisnik;
import models.Korisnik.tipKorisnika;
import models.Predmet;
import models.Profesor;
import models.Termin;
import models.Termin.tipTermina;

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

	public AnchorPane newBlock(AnchorPane block, Termin termin, int velicina, int poRedu) {
		AnchorPane novi = new AnchorPane();

		if(termin.getTip() == Termin.tipTermina.Predavanje)
			novi.setStyle("-fx-background-color: #b7f78a");

		else if(termin.getTip() == Termin.tipTermina.Vjezbe)
			novi.setStyle("-fx-background-color: #f79c8a");

		else if(termin.getTip() == Termin.tipTermina.Seminar)
			novi.setStyle("-fx-background-color: #6a95fc");

		else if(termin.getTip() == Termin.tipTermina.Nadoknada)
			novi.setStyle("-fx-background-color: #424240");

		else
			novi.setStyle("-fx-background-color: #f2fc69");
	
		double width = block.getPrefWidth()/velicina;
		width = width-(velicina-1)*2;
		
		LocalDateTime tempDateTime = LocalDateTime.from( termin.getStartTime() );
		long minuteTrajanje = tempDateTime.until( termin.getEndTime(), ChronoUnit.MINUTES);
		
		novi.setPrefHeight(minuteTrajanje);
		novi.setMaxHeight(minuteTrajanje);
		
		if (velicina > 1) {
			novi.setPrefWidth(width);
			novi.setMaxWidth(width);
		}
			
		else {
			novi.setPrefWidth(width);
			novi.setMaxWidth(width);
		}

		List<Label> info = new ArrayList<>();
		
		info.add(new Label(termin.getPredmet().getNaziv()));
		info.add(new Label(termin.getGrupa()));
		info.add(new Label(termin.getZgrada()+"-"+termin.getSala()));
		
		int razmakZaDan = 140+15;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime danDatum = termin.getStartTime();
		
        String dan = danDatum.format(formatter);
		List<String> sviDani = getDates();
		int pomjerajX = 0;
		int pocetak = (termin.getStartTime().getHour()-8) * 60 + termin.getStartTime().getMinute();
		long pomjerajY = pocetak;
		
		for (int i = 0; i < 6; i++) {
			if (sviDani.get(i).equals(dan)) {
				pomjerajX = razmakZaDan * i;
			}
		}

		System.out.println(pomjerajY);
		System.out.println(pomjerajX);
		novi.setLayoutY(pomjerajY);

		novi.setLayoutX(pomjerajX+poRedu*width+poRedu*2*2);

		for (int i = 0; i < 3; ++i) {
			info.get(i).getStyleClass().add("copyable-label");
			info.get(i).setPrefWidth(width);
			info.get(i).setLayoutX(0);
			info.get(i).setLayoutY(i * 15);
			novi.getChildren().add(info.get(i));
		}
		return novi;
	}

	private void dodajKorisnike() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		setDBSystemDir();
		Korisnik prodekan = new Korisnik();
		prodekan.setUsername("mesko");
		prodekan.setPassword("mesko");
		prodekan.setTip(tipKorisnika.Prodekan);

		Korisnik nastavnik = new Korisnik();
		nastavnik.setUsername("amer");
		nastavnik.setPassword("amer");
		nastavnik.setTip(tipKorisnika.Nastavnik);

		em.getTransaction().begin();
		em.persist(prodekan);
		em.persist(nastavnik);
		em.getTransaction().commit();
		em.close();
	}

	public void startLoginPage(Stage primaryStage) throws IOException {
		// Ubacena 2 korisnika, ne pozivati vise
		// dodajKorisnike();

		// Test korisnika
		/*
		 * factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		 * EntityManager em = factory.createEntityManager(); Query q =
		 * em.createNamedQuery("sviKorisnici"); List<Korisnik> rezultat =
		 * q.getResultList(); for (Korisnik k : rezultat) { System.out.println(k); }
		 */

		VBox login = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene loginScene = new Scene(login);

		loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);

		primaryStage.setScene(loginScene);
		primaryStage.show();

		AnchorPane mainPane = (AnchorPane) login.getChildren().get(0);
		AnchorPane duals = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane loginPane = (AnchorPane) duals.getChildren().get(1);

		Button loginButton = (Button) loginPane.getChildren().get(0);
		Button bezPrijave = (Button) loginPane.getChildren().get(1);
		
		TextField username = (TextField) loginPane.getChildren().get(2);
		PasswordField password = (PasswordField) loginPane.getChildren().get(3);
		
		Label greska = (Label) loginPane.getChildren().get(4);
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					String uneseniUser = username.getText();
					String uneseniPass = password.getText();
					
					if (uneseniUser == null || uneseniPass == null)
						greska.setVisible(true);
					
					else if (uneseniUser.equals("") || uneseniPass.equals(""))
						greska.setVisible(true);	
					
					else if(Korisnik.nadjiKorisnika(uneseniUser, uneseniPass)==null) {
						greska.setVisible(true);
					}
					else {
						greska.setVisible(false);
						
						if(Korisnik.nadjiKorisnika(uneseniUser, uneseniPass)==tipKorisnika.Nastavnik) {
							startFilterPage(primaryStage, true);
							System.out.println("radi");
						}
						else if(Korisnik.nadjiKorisnika(uneseniUser, uneseniPass)==tipKorisnika.Prodekan) {
							startFilterPage(primaryStage, true);
							System.out.println("radi opet");
						}
						else {
							startFilterPage(primaryStage, false);
						}
						
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		bezPrijave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					startFilterPage(primaryStage, false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	public void startFilterPage(Stage primaryStage, boolean registrovan) throws IOException {
		VBox filter = FXMLLoader.load(getClass().getResource("rasporedFilter.fxml"));

		AnchorPane mainPane = (AnchorPane) filter.getChildren().get(0);
		AnchorPane secondPane = (AnchorPane) mainPane.getChildren().get(1);
		
		List<CheckBox> filteri = new ArrayList<>();
		List<TextField> unosi = new ArrayList<>();
		List<String> vrijednosti = new ArrayList<>();
		Button potvrdi = (Button) secondPane.getChildren().get(15);
		Label upozorenje1 = (Label) secondPane.getChildren().get(16);
		Label upozorenje2 = (Label) secondPane.getChildren().get(17);
		
		for (int i=1; i<=7; ++i) {
			filteri.add((CheckBox) secondPane.getChildren().get(i));
		}
		
		for (int i=8; i<=14; ++i) {
			unosi.add((TextField) secondPane.getChildren().get(i));
		}
		
		potvrdi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					for (int i=0; i<=6; ++i) {
						if (filteri.get(i).isSelected()) {
							if (unosi.get(i).getText() == null || unosi.get(i).getText().equals(""))
								vrijednosti.add(null);
							else
								vrijednosti.add(unosi.get(i).getText());
						}
						else
							vrijednosti.add(null);
					}
					
					System.out.println(vrijednosti);
					if (vrijednosti.get(1) == null &&
						vrijednosti.get(2) == null &&
						vrijednosti.get(4) == null &&
						vrijednosti.get(5) == null) {
						
						if (vrijednosti.get(3) == null || vrijednosti.get(6) == null) {
							if (vrijednosti.get(0) == null) {
								upozorenje1.setVisible(true);
								upozorenje2.setVisible(false);
							}
							else {
								upozorenje1.setVisible(false);
								upozorenje2.setVisible(true);
							}
							vrijednosti.clear();
						}
							
						else {
							upozorenje1.setVisible(false);
							upozorenje2.setVisible(false);
							Collection<Termin> termini = Termin.getTermini(vrijednosti);
							System.out.println(termini.size());
							startRasporedPage(primaryStage, registrovan, termini);
						}
						
					}
					else {
						upozorenje1.setVisible(false);
						upozorenje2.setVisible(false);
						Collection<Termin> termini = Termin.getTermini(vrijednosti);
						System.out.println(termini.size());
						startRasporedPage(primaryStage, registrovan, termini);
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		Scene filterScene = new Scene(filter);
		filterScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);
		primaryStage.setScene(filterScene);
		primaryStage.show();
		
	}

	public void startRasporedPage(Stage primaryStage, boolean registrovan, Collection<Termin> termini) throws IOException {

		VBox root = FXMLLoader.load(getClass().getResource("raspored.fxml"));
		Scene scene = new Scene(root);

		ScrollPane scrollPane = (ScrollPane) root.getChildren().get(0);
		AnchorPane mainPane = (AnchorPane) scrollPane.getContent();
		AnchorPane weekDaysPane = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane drawPane = (AnchorPane) mainPane.getChildren().get(3);
		AnchorPane navigacija = (AnchorPane) mainPane.getChildren().get(4);
		
		AnchorPane defaultBlock = (AnchorPane) drawPane.getChildren().get(12);

		HashMap<LocalDateTime, List<Termin>> mapaTermina = new HashMap<>();
		
		for (Termin t : termini) {
			if (mapaTermina.get(t.getStartTime()) == null)
				mapaTermina.put(t.getStartTime(), new ArrayList<>());
			mapaTermina.get(t.getStartTime()).add(t);	
			mapaTermina.get(t.getStartTime()).add(t); // OVAJ OBRISATI!!!!
		}
		
		Iterator it = mapaTermina.entrySet().iterator();
		List<AnchorPane> noviBlokovi = new ArrayList<>();
		
	    while (it.hasNext()) {
	        HashMap.Entry pair = (HashMap.Entry)it.next();
	        ArrayList<Termin> trenutna = (ArrayList<Termin>) pair.getValue();
	        
	        for (int i=0; i<trenutna.size(); ++i) {
				AnchorPane noviDodavanje = newBlock(defaultBlock, trenutna.get(i), trenutna.size(), i);
				noviBlokovi.add(noviDodavanje);
			}
	        it.remove(); // avoids a ConcurrentModificationException
	    }

		for (int i=0; i<noviBlokovi.size(); ++i)
			drawPane.getChildren().add(noviBlokovi.get(i));

		List<Node> allDays = weekDaysPane.getChildren();

		List<String> weekDates = getDates();
		for (int i = 0; i < 6; ++i) {
			AnchorPane day = (AnchorPane) allDays.get(i);
			Label dateLabel = (Label) day.getChildren().get(1);
			dateLabel.setText(weekDates.get(i));

		}
		
		Button nazadFilter = (Button) navigacija.getChildren().get(6);
		Button sledeca = (Button) navigacija.getChildren().get(7);
		Button prethodna = (Button) navigacija.getChildren().get(8);
		
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
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		// DbFunctions.createPredmet();
		// DbFunctions.createTermini();
		List<String> vr = new ArrayList<String>();
		vr.add("FE");
		vr.add("101");
		vr.add(null);
		vr.add("0");
		vr.add("Osnovi elektronike");
		vr.add(null);
		vr.add("TK");

		/*
		 * Termin.getTermini(vr); Predmet.showPredmeti(); Termin.showTermini();
		 */
		// Podaci.dodajProfesore();
		if (Korisnik.nadjiKorisnika("amer", "amer") == tipKorisnika.Nastavnik) {
			System.out.println("radi");
		}
		if (Korisnik.nadjiKorisnika("mesko", "mesko") == tipKorisnika.Prodekan) {
			System.out.println("radi opet");
		}
		// Pogresan PASS primjer
		if (Korisnik.nadjiKorisnika("amer", "aer") == null) {
			System.out.println("radi i null");
		}
		launch(args);

		factory.close();
	}
}
