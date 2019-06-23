package application;

import java.awt.Color;
import java.awt.Polygon;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import models.Lokacija;
import models.Predmet;
import models.Profesor;
import models.Profesor.Usmjerenje;
import models.Termin;
import models.Termin.tipTermina;

public class Main extends Application {

	private static final String PERSISTENCE_UNIT_NAME = "RazvojSoftvera";
	private static EntityManagerFactory factory;

	private int offsetDatum = 0;

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

		c.add(Calendar.DATE, this.offsetDatum);

		weekDates.add(df.format(c.getTime()));
		for (int i = 0; i < 6; i++) {
			c.add(Calendar.DATE, 1);
			weekDates.add(df.format(c.getTime()));
		}

		return weekDates;
	}

	public AnchorPane newBlock(AnchorPane block, Termin termin, int velicina, int poRedu) {
		AnchorPane novi = new AnchorPane();

		String myStyle = "-fx-border-color: #c6c6c6; -fx-border-width: 0px 0px 1px 0px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 5, 0.3, 0.0, 0.0);";

		if (termin.getTip() == Termin.tipTermina.Predavanje) {
			novi.setStyle("-fx-background-color: #b7f78a; " + myStyle);
		} else if (termin.getTip() == Termin.tipTermina.Vjezbe) {
			novi.setStyle("-fx-background-color: #f79c8a; " + myStyle);
		} else if (termin.getTip() == Termin.tipTermina.Seminar) {
			novi.setStyle("-fx-background-color: #f2fc69; " + myStyle);
		} else if (termin.getTip() == Termin.tipTermina.Nadoknada) {
			novi.setStyle("-fx-background-color: #424240; " + myStyle);
		} else if (termin.getTip() == Termin.tipTermina.Laboratorija) {
			novi.setStyle("-fx-background-color: #6a95fc; " + myStyle);
		} else {
			novi.setStyle("-fx-background-color: #ed1ee2; " + myStyle);
		}

		double width = block.getPrefWidth() / velicina;
		width = width - ((velicina - 1) * 2);

		LocalDateTime tempDateTime = LocalDateTime.from(termin.getStartTime());
		long minuteTrajanje = tempDateTime.until(termin.getEndTime(), ChronoUnit.MINUTES);

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
		info.add(new Label(termin.getLokacija().getZgrada() + "-" + termin.getLokacija().getSala()));

		int razmakZaDan = 140 + 15;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime danDatum = termin.getStartTime();

		String dan = danDatum.format(formatter);
		List<String> sviDani = getDates();

		System.out.println("DAAAN " + dan);
		System.out.println("DANIII NEW BLOCK" + sviDani);
		int pomjerajX = 0;
		int pocetak = ((termin.getStartTime().getHour() - 8) * 60) + termin.getStartTime().getMinute();
		long pomjerajY = pocetak;

		for (int i = 0; i < 6; i++) {
			if (sviDani.get(i).equals(dan)) {
				pomjerajX = razmakZaDan * i;
			}
		}

		System.out.println(pomjerajY);
		System.out.println(pomjerajX);
		novi.setLayoutY(pomjerajY);

		novi.setLayoutX(pomjerajX + (poRedu * (width + ((velicina - 1) * 2))) + (poRedu * 2));

		for (int i = 0; i < 3; ++i) {
			info.get(i).getStyleClass().add("copyable-label");
			info.get(i).setPrefWidth(width);
			info.get(i).setMaxHeight(30);
			info.get(i).setWrapText(true);
			info.get(i).setLayoutX(0);
			if (i == 1) {
				info.get(i).setLayoutY(30);
			}
			if (i == 2) {
				info.get(i).setLayoutY(45);
			}
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
		// dodajKorisnike();

		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		Query q = em.createQuery("Select t from Termin t");
		List<Termin> res = q.getResultList();

		/*
		 * for (Termin t : res) { System.out.println(t); }
		 */

		/*
		 * em.getTransaction().begin(); for (Termin t : res) { em.persist(t); for (int i
		 * = 0; i < 14; ++i) { em.detach(t); t.setId(null);
		 * t.setStartTime(t.getStartTime().plusDays(7));
		 * t.setEndTime(t.getEndTime().plusDays(7)); em.persist(t); em.flush(); } }
		 * em.getTransaction().commit();
		 */

		// System.out.println(res.size());

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

					if ((uneseniUser == null) || (uneseniPass == null)) {
						greska.setVisible(true);
					} else if (uneseniUser.equals("") || uneseniPass.equals("")) {
						greska.setVisible(true);
					} else if (Korisnik.nadjiKorisnika(uneseniUser, uneseniPass) == null) {
						greska.setVisible(true);
					} else {
						greska.setVisible(false);

						if (Korisnik.nadjiKorisnika(uneseniUser, uneseniPass) == tipKorisnika.Nastavnik) {
							startFilterPage(primaryStage, true);
							System.out.println("radi");
						} else if (Korisnik.nadjiKorisnika(uneseniUser, uneseniPass) == tipKorisnika.Prodekan) {
//							startFilterPage(primaryStage, true);
							startProdekanPage(primaryStage);
							System.out.println("radi opet");
						} else {
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

		for (int i = 1; i <= 7; ++i) {
			filteri.add((CheckBox) secondPane.getChildren().get(i));
		}

		for (int i = 8; i <= 14; ++i) {
			unosi.add((TextField) secondPane.getChildren().get(i));
		}

		potvrdi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					for (int i = 0; i <= 6; ++i) {
						if (filteri.get(i).isSelected()) {
							if ((unosi.get(i).getText() == null) || unosi.get(i).getText().equals("")) {
								vrijednosti.add(null);
							} else {
								vrijednosti.add(unosi.get(i).getText());
							}
						} else {
							vrijednosti.add(null);
						}
					}
					List<String> trenutnaSedmica = getDates();
					vrijednosti.add(trenutnaSedmica.get(0));
					vrijednosti.add(trenutnaSedmica.get(5));
					System.out.println(vrijednosti);
					if ((vrijednosti.get(1) == null) && (vrijednosti.get(2) == null) && (vrijednosti.get(4) == null)
							&& (vrijednosti.get(5) == null)) {

						if ((vrijednosti.get(3) == null) || (vrijednosti.get(6) == null)) {
							if (vrijednosti.get(0) == null) {
								upozorenje1.setVisible(true);
								upozorenje2.setVisible(false);
							} else {
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
							startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
						}

					} else {
						upozorenje1.setVisible(false);
						upozorenje2.setVisible(false);
						Collection<Termin> termini = Termin.getTermini(vrijednosti);
						System.out.println(termini.size());
						startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
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

	public void startRasporedPage(Stage primaryStage, boolean registrovan, Collection<Termin> termini,
			List<String> vrijednosti) throws IOException {

		List<String> sledeceVrijednosti = new ArrayList<>();
		List<String> prethodneVrijednosti = new ArrayList<>();

		sledeceVrijednosti.addAll(vrijednosti);
		prethodneVrijednosti.addAll(vrijednosti);

		this.offsetDatum += 7;
		List<String> sledeciDatumi = getDates();
		this.offsetDatum -= 7;
		sledeceVrijednosti.set(7, sledeciDatumi.get(0));
		sledeceVrijednosti.set(8, sledeciDatumi.get(5));

		this.offsetDatum -= 7;
		List<String> prethodniDatumi = getDates();
		this.offsetDatum += 7;
		prethodneVrijednosti.set(7, prethodniDatumi.get(0));
		prethodneVrijednosti.set(8, prethodniDatumi.get(5));

		Collection<Termin> sledeciTermini = Termin.getTermini(sledeceVrijednosti);
		Collection<Termin> prethodniTermini = Termin.getTermini(prethodneVrijednosti);

		System.out.println("OVDJEE: " + prethodneVrijednosti);
		System.out.println("OVDJEE: " + sledeceVrijednosti);

		VBox root = FXMLLoader.load(getClass().getResource("raspored.fxml"));
		Scene scene = new Scene(root);

		ScrollPane scrollPane = (ScrollPane) root.getChildren().get(0);
		AnchorPane mainPane = (AnchorPane) scrollPane.getContent();
		AnchorPane weekDaysPane = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane drawPane = (AnchorPane) mainPane.getChildren().get(3);
		AnchorPane navigacija = (AnchorPane) mainPane.getChildren().get(4);
		ButtonBar uredjivanje = (ButtonBar) mainPane.getChildren().get(0);
		ObservableList<Node> sviButtoni = uredjivanje.getButtons();
		Button dodaj = (Button) sviButtoni.get(0);
		Button obrisi = (Button) sviButtoni.get(1);
		Button uredi = (Button) sviButtoni.get(2);

		if (registrovan) {
			uredjivanje.setVisible(true);
		} else {
			uredjivanje.setVisible(false);
		}

		AnchorPane defaultBlock = (AnchorPane) drawPane.getChildren().get(12);

		HashMap<LocalDateTime, List<Termin>> mapaTermina = new HashMap<>();

		for (Termin t : termini) {
			if (mapaTermina.get(t.getStartTime()) == null) {
				mapaTermina.put(t.getStartTime(), new ArrayList<>());
			}
			mapaTermina.get(t.getStartTime()).add(t);
		}

		Iterator it = mapaTermina.entrySet().iterator();
		List<AnchorPane> noviBlokovi = new ArrayList<>();

		LocalDateTime limitDatum = LocalDateTime.of(LocalDate.of(2019, 6, 24), LocalTime.of(0, 0, 0));

		LocalDateTime trenutniDatum = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		while (it.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry) it.next();
			ArrayList<Termin> trenutna = (ArrayList<Termin>) pair.getValue();

			for (int i = 0; i < trenutna.size(); ++i) {
				if ((trenutna.get(i).getStartTime().compareTo(limitDatum)) < 0) {
					continue;
				}
				AnchorPane noviDodavanje = newBlock(defaultBlock, trenutna.get(i), trenutna.size(), i);
				noviBlokovi.add(noviDodavanje);
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		for (int i = 0; i < noviBlokovi.size(); ++i) {
			drawPane.getChildren().add(noviBlokovi.get(i));
		}

		List<Node> allDays = weekDaysPane.getChildren();

		String trenutniDan = trenutniDatum.format(formatter);

		List<String> weekDates = getDates();
		for (int i = 0; i < 6; ++i) {
			AnchorPane day = (AnchorPane) allDays.get(i);
			Label dateLabel = (Label) day.getChildren().get(1);
			dateLabel.setText(weekDates.get(i));
			if (dateLabel.getText().equals(trenutniDan)) {
				day.setStyle("-fx-background-color: #2b6aff;");
			}

		}

		Button nazadFilter = (Button) navigacija.getChildren().get(6);
		Button sledeca = (Button) navigacija.getChildren().get(7);
		Button prethodna = (Button) navigacija.getChildren().get(8);

		nazadFilter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					startFilterPage(primaryStage, registrovan);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		sledeca.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Main.this.offsetDatum += 7;
				try {
					startRasporedPage(primaryStage, registrovan, sledeciTermini, sledeceVrijednosti);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		prethodna.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Main.this.offsetDatum -= 7;
				try {
					startRasporedPage(primaryStage, registrovan, prethodniTermini, prethodneVrijednosti);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startProdekanPage(Stage primaryStage) throws IOException {
		AnchorPane home = FXMLLoader.load(getClass().getResource("ProdekanHomePage.fxml"));
		Scene scene = new Scene(home);

		Button profesori = (Button) scene.lookup("#b01");
		Button predmeti = (Button) scene.lookup("#b02");
		Button prostorije = (Button) scene.lookup("#b03");
		Button rasporedi = (Button) scene.lookup("#b04");

		profesori.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProfesori(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		predmeti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startPredmeti(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		prostorije.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProstorije(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		rasporedi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startFilterPage(primaryStage, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Pocetna");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startProfesori(Stage primaryStage) throws IOException {
		AnchorPane showProfesori = FXMLLoader.load(getClass().getResource("ProfesoriEditPage.fxml"));
		Scene scene = new Scene(showProfesori);

		Button nazad = (Button) scene.lookup("#nazad");
		Button dodaj = (Button) scene.lookup("#dodaj");
		TableView<Profesor> tabela = (TableView<Profesor>) scene.lookup("#tabela");

		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("ime"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("usmjerenje"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("predmeti"));
		tabela.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("grupa"));

		Collection<Profesor> c = Profesor.getProfesori();
		tabela.getItems().addAll(c);

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					Profesor prof = tabela.getSelectionModel().getSelectedItem();
					startUrediProfesora(primaryStage, prof);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startDodajProfesoraPage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		nazad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProdekanPage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Profesori");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startPredmeti(Stage primaryStage) throws IOException {
		AnchorPane showPredmeti = FXMLLoader.load(getClass().getResource("PredmetiEditPage.fxml"));
		Scene scene = new Scene(showPredmeti);

		Button nazad = (Button) scene.lookup("#nazad");
		Button dodaj = (Button) scene.lookup("#dodaj");
		TableView<Predmet> tabela = (TableView<Predmet>) scene.lookup("#tabela");

		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("naziv"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("brojStudenata"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("usmjerenje"));
		tabela.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("semestar"));
		tabela.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("profesori"));

		Collection<Predmet> c = Predmet.getPredmeti();
		tabela.getItems().addAll(c);

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					startUrediPredmet(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startDodajPredmetPage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		nazad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProdekanPage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Predmeti");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startProstorije(Stage primaryStage) throws IOException {
		AnchorPane showProstorije = FXMLLoader.load(getClass().getResource("ProstorijeEditPage.fxml"));
		Scene scene = new Scene(showProstorije);

		Button nazad = (Button) scene.lookup("#nazad");
		Button dodaj = (Button) scene.lookup("#dodaj");
		TableView<Lokacija> tabela = (TableView<Lokacija>) scene.lookup("#tabela");

		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("zgrada"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("sala"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("kapacitet"));

		Collection<Lokacija> c = Lokacija.getLokacije();
		tabela.getItems().addAll(c);

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					startUrediProstoriju(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startDodajProstorijePage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		nazad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProdekanPage(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		primaryStage.setTitle("Prostorije");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startDodajProfesoraPage(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("FormaNastavnik.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProfesori(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Dodaj novi predmet");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startDodajPredmetPage(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("FormaPredmet.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startPredmeti(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Dodaj novi predmet");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startDodajProstorijePage(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("FormaProstorija.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProstorije(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Dodaj novu prostoriju");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void startUrediProfesora(Stage primaryStage, Profesor prof) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediProfesora.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");
		
		TextField id = (TextField) scene.lookup("#id");
		TextField ime = (TextField) scene.lookup("#ime");
		TextField prezime = (TextField) scene.lookup("#prezime");
		ChoiceBox<Profesor.Usmjerenje> usmjerenje = (ChoiceBox<Profesor.Usmjerenje>) scene.lookup("#usmjerenje");

		id.setText(prof.getId().toString());
		
		String[] profesor = prof.getIme().split(" ");
		ime.setText(profesor[0]);
		prezime.setText(profesor[1]);
		
		usmjerenje.getItems().addAll(Usmjerenje.AR, Usmjerenje.EEMS, Usmjerenje.ESKE, Usmjerenje.RI, Usmjerenje.TK);
		usmjerenje.setValue(prof.getUsmjerenje());


		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProfesori(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Uredi profesora");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startUrediPredmet(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediPredmet.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startPredmeti(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Uredi predmet");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void startUrediProstoriju(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediProstoriju.fxml"));
		Scene scene = new Scene(forma);

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startProstorije(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		primaryStage.setTitle("Uredi prostoriju");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			primaryStage.setResizable(false);
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
		vr.add(null);
		vr.add(null);
		vr.add(null);
		vr.add("2");
		vr.add(null);
		vr.add(null);
		vr.add("RI");
		vr.add("16/09/2019");
		vr.add("23/09/2019");
//		 DbFunctions.addProdekan();
		Korisnik.showKorisnici();

		// Termin.getTermini(vr);
		/*
		 * Termin.getTermini(vr); Predmet.showPredmeti(); Termin.showTermini();
		 */
		// Podaci.napuniBazu();
		// Termin.showTermini();
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