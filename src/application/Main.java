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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.ComboBox;
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

	private String trenutniKorisnik = null;

	public static EntityManagerFactory getFactory() {
		return factory;
	}

	private static void setDBSystemDir() {
		String userHomeDir = System.getProperty("user.home", ".");
		String systemDir = userHomeDir + "/.addressbook";
		System.setProperty("derby.system.home", systemDir);
	}

	public List<String> getDates() {

		List<String> weekDates = new ArrayList<>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		c.add(Calendar.DATE, offsetDatum);

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

		// System.out.println("DAAAN " + dan);
		// System.out.println("DANIII NEW BLOCK" + sviDani);
		int pomjerajX = 0;
		int pocetak = ((termin.getStartTime().getHour() - 8) * 60) + termin.getStartTime().getMinute();
		long pomjerajY = pocetak;

		for (int i = 0; i < 6; i++) {
			if (sviDani.get(i).equals(dan)) {
				pomjerajX = razmakZaDan * i;
			}
		}

		// System.out.println(pomjerajY);
		// System.out.println(pomjerajX);
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

	public void startLoginPage(Stage primaryStage) throws IOException {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();

		VBox login = FXMLLoader.load(getClass().getResource("login.fxml"));
		Scene loginScene = new Scene(login);

		loginScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setResizable(false);

		primaryStage.setScene(loginScene);
		primaryStage.show();
		
		Stage otvoriFilter = new Stage();

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
					String uneseniUser = trenutniKorisnik = username.getText();
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
							startFilterPage(otvoriFilter, true);
						} else if (Korisnik.nadjiKorisnika(uneseniUser, uneseniPass) == tipKorisnika.Prodekan) {
							startProdekanPage(primaryStage);
						} else {
							startFilterPage(otvoriFilter, false);
						}

					}

				} catch (IOException e1) {
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
					e1.printStackTrace();
				}
			}
		});
	}

	public void startFilterPage(Stage primaryStage, boolean registrovan) throws IOException {
		VBox filter = FXMLLoader.load(getClass().getResource("rasporedFilter.fxml"));

		Scene filterScene = new Scene(filter);
		filterScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage secondStage = new Stage();
		secondStage.setTitle("Filter");
		secondStage.setScene(filterScene);
		secondStage.show();

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
					vrijednosti.add(trenutnaSedmica.get(6));
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
							
							secondStage.close();
							startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
						}

					} else {
						upozorenje1.setVisible(false);
						upozorenje2.setVisible(false);
						Collection<Termin> termini = Termin.getTermini(vrijednosti);
						secondStage.close();
						startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
					}

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void startRasporedPage(Stage primaryStage, boolean registrovan, Collection<Termin> termini,
			List<String> vrijednosti) throws IOException {

		List<String> sledeceVrijednosti = new ArrayList<>();
		List<String> prethodneVrijednosti = new ArrayList<>();

		sledeceVrijednosti.addAll(vrijednosti);
		prethodneVrijednosti.addAll(vrijednosti);

		offsetDatum += 7;
		List<String> sledeciDatumi = getDates();
		offsetDatum -= 7;
		sledeceVrijednosti.set(7, sledeciDatumi.get(0));
		sledeceVrijednosti.set(8, sledeciDatumi.get(6));

		offsetDatum -= 7;
		List<String> prethodniDatumi = getDates();
		offsetDatum += 7;
		prethodneVrijednosti.set(7, prethodniDatumi.get(0));
		prethodneVrijednosti.set(8, prethodniDatumi.get(6));

		Collection<Termin> sledeciTermini = Termin.getTermini(sledeceVrijednosti);
		Collection<Termin> prethodniTermini = Termin.getTermini(prethodneVrijednosti);

		VBox root = FXMLLoader.load(getClass().getResource("raspored.fxml"));
		Scene scene = new Scene(root);

		ScrollPane scrollPane = (ScrollPane) root.getChildren().get(0);
		AnchorPane mainPane = (AnchorPane) scrollPane.getContent();
		AnchorPane weekDaysPane = (AnchorPane) mainPane.getChildren().get(1);
		AnchorPane drawPane = (AnchorPane) mainPane.getChildren().get(3);
		AnchorPane navigacija = (AnchorPane) mainPane.getChildren().get(4);
		Button dodaj = (Button) mainPane.getChildren().get(0);
		Button izvjestaj = (Button) mainPane.getChildren().get(5);

		if (registrovan) {
			dodaj.setVisible(true);
			izvjestaj.setVisible(true);
		} else {
			dodaj.setVisible(false);
			izvjestaj.setVisible(false);
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
				AnchorPane noviDodavanje = newBlock(defaultBlock, trenutna.get(i), trenutna.size(), i);
				Termin temp = trenutna.get(i);
				noviDodavanje.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2) {
						if (!registrovan) {
							System.out.println("Ne mozete uredjivati!");
						} else if (trenutniKorisnik.equals(temp.getProfesor().getIme())
								|| trenutniKorisnik.equals("Emir Me??kovi??")) {
							try {
								startTerminObrisiPage(primaryStage, registrovan, termini, vrijednosti, temp);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}

						else {
							System.out.println("Ne mozete uredjivati!");
						}
					}
				});
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
				offsetDatum += 7;
				try {
					startRasporedPage(primaryStage, registrovan, sledeciTermini, sledeceVrijednosti);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		prethodna.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				offsetDatum -= 7;
				try {
					startRasporedPage(primaryStage, registrovan, prethodniTermini, prethodneVrijednosti);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		dodaj.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					startTerminPage(primaryStage, registrovan, termini, vrijednosti);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		izvjestaj.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					startIzvjestajPage(primaryStage, trenutniDatum.getMonth().name(), trenutniDan);
				} catch (IOException e1) {
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
		
		Stage otvoriFilter = new Stage();

		Button profesori = (Button) scene.lookup("#b01");
		Button predmeti = (Button) scene.lookup("#b02");
		Button prostorije = (Button) scene.lookup("#b03");
		Button rasporedi = (Button) scene.lookup("#b04");
		Button odjava = (Button) scene.lookup("#odjava");

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
					startFilterPage(otvoriFilter, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		odjava.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					startLoginPage(primaryStage);
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
		ObservableList<ProfesoriIspis> masterData = FXCollections.observableArrayList();
		Collection<ProfesoriIspis> c = ProfesoriGet.getTableProfesor();
		masterData.addAll(c);
		AnchorPane showProfesori = FXMLLoader.load(getClass().getResource("ProfesoriEditPage.fxml"));
		Scene scene = new Scene(showProfesori);
		TextField pretraga = (TextField) scene.lookup("#pretraga");
		Button nazad = (Button) scene.lookup("#nazad");
		Button dodaj = (Button) scene.lookup("#dodaj");
		TableView<ProfesoriIspis> tabela = (TableView<ProfesoriIspis>) scene.lookup("#tabela");
		FilteredList<ProfesoriIspis> filteredData = new FilteredList<>(masterData, p -> true);
		pretraga.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				if ((newValue == null) || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (person.getImePrezime().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				if (person.getUsmjerenje().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
		SortedList<ProfesoriIspis> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tabela.comparatorProperty());
		tabela.setItems(sortedData);
		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("imePrezime"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("usmjerenje"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("predmeti"));

		EntityManager em = Main.getFactory().createEntityManager();

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					String id = tabela.getSelectionModel().getSelectedItem().getId();
					Profesor prof = em.getReference(Profesor.class, Long.parseLong(id));
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
		ObservableList<PredmetiIspis> masterData = FXCollections.observableArrayList();
		Collection<PredmetiIspis> c = PredmetiGet.getTablePredmeti();
		masterData.addAll(c);
		Button nazad = (Button) scene.lookup("#nazad");
		Button dodaj = (Button) scene.lookup("#dodaj");
		TableView<PredmetiIspis> tabela = (TableView<PredmetiIspis>) scene.lookup("#tabela");
		TextField pretraga = (TextField) scene.lookup("#pretraga");

		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("naziv"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("brStud"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("usmjerenje"));
		tabela.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("semestar"));
		tabela.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("profesori"));

		EntityManager em = Main.getFactory().createEntityManager();
		FilteredList<PredmetiIspis> filteredData = new FilteredList<>(masterData, p -> true);
		pretraga.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				if ((newValue == null) || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (person.getNaziv().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
		SortedList<PredmetiIspis> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tabela.comparatorProperty());
		tabela.setItems(sortedData);

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					String id = tabela.getSelectionModel().getSelectedItem().getId();
					Predmet pred = em.getReference(Predmet.class, Long.parseLong(id));
					startUrediPredmet(primaryStage, pred);
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
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("sala"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("zgrada"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("kapacitet"));

		Collection<Lokacija> c = Lokacija.getLokacije();
		tabela.getItems().addAll(c);

		tabela.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				try {
					Lokacija pros = tabela.getSelectionModel().getSelectedItem();
					startUrediProstoriju(primaryStage, pros);
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

		Stage secondStage = new Stage();
		secondStage.setTitle("Dodaj novog profesora");
		secondStage.setScene(scene);
		secondStage.show();

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		TextField ime = (TextField) scene.lookup("#ime");
		TextField prezime = (TextField) scene.lookup("#prezime");
		ChoiceBox<Profesor.Usmjerenje> usmjerenje = (ChoiceBox<Profesor.Usmjerenje>) scene.lookup("#usmjerenje");
		usmjerenje.getItems().addAll(Usmjerenje.AR, Usmjerenje.EEMS, Usmjerenje.ESKE, Usmjerenje.RI, Usmjerenje.TK);

		ime.requestFocus();

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				if (ime.getText().trim().equals("") || prezime.getText().trim().equals("")
						|| usmjerenje.getValue().toString().equals("")) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
					return;
				}

				List<String> profesor = new ArrayList<String>();
				profesor.add(ime.getText());
				profesor.add(prezime.getText());
				profesor.add(usmjerenje.getValue().toString());

				int code = Profesor.unesiProfesor(profesor);
				if (code == 1) {
					try {
						greska.setVisible(false);
						secondStage.close();
						startProfesori(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					greska.setText("Korisnik vec postoji u bazi!");
					greska.setVisible(true);
				}
			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startProfesori(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startDodajPredmetPage(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("FormaPredmet.fxml"));
		Scene scene = new Scene(forma);

		Stage secondStage = new Stage();
		secondStage.setTitle("Dodaj novi predmet");
		secondStage.setScene(scene);
		secondStage.show();

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		TextField naziv = (TextField) scene.lookup("#naziv");
		TextField semestar = (TextField) scene.lookup("#semestar");
		TextField brojStudenata = (TextField) scene.lookup("#brojStudenata");
		CheckBox ar = (CheckBox) scene.lookup("#ar");
		CheckBox eems = (CheckBox) scene.lookup("#eems");
		CheckBox eske = (CheckBox) scene.lookup("#eske");
		CheckBox ri = (CheckBox) scene.lookup("#ri");
		CheckBox tk = (CheckBox) scene.lookup("#tk");
		TextField profesori = (TextField) scene.lookup("#profesori");

		naziv.requestFocus();

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				if (naziv.getText().trim().equals("") || semestar.getText().trim().equals("")
						|| brojStudenata.getText().trim().equals("") || profesori.getText().trim().equals("")
						|| (!ar.isSelected() && !eems.isSelected() && !eske.isSelected() && !ri.isSelected()
								&& !tk.isSelected())) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
					return;
				}

				List<String> predmet = new ArrayList<String>();
				predmet.add(naziv.getText());
				predmet.add(semestar.getText());
				predmet.add(brojStudenata.getText());
				predmet.add("");
				String[] profesoriNiz = profesori.getText().split(", ");
				for (int i = 0; i < profesoriNiz.length; ++i) {
					predmet.add(4 + i, profesoriNiz[i]);
				}
				if (ar.isSelected()) {
					predmet.set(3, "AR");
					dodajPredmet(predmet, greska, primaryStage, secondStage);
				}
				if (eems.isSelected()) {
					predmet.set(3, "EEMS");
					dodajPredmet(predmet, greska, primaryStage, secondStage);
				}
				if (eske.isSelected()) {
					predmet.set(3, "ESKE");
					dodajPredmet(predmet, greska, primaryStage, secondStage);
				}
				if (ri.isSelected()) {
					predmet.set(3, "RI");
					dodajPredmet(predmet, greska, primaryStage, secondStage);
				}
				if (tk.isSelected()) {
					predmet.set(3, "TK");
					dodajPredmet(predmet, greska, primaryStage, secondStage);
				}

			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startPredmeti(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void dodajPredmet(List<String> predmet, Label greska, Stage primaryStage, Stage secondStage) {
		System.out.println(predmet.toString());
		int code = Predmet.unesiPredmet(predmet);
		if (code == 1) {
			try {
				greska.setVisible(false);
				secondStage.close();
				startPredmeti(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (code == -2) {
			greska.setText("Predmet vec postoji u bazi!");
			greska.setVisible(true);
		} else {
			greska.setText("Nema profesora u bazi!");
			greska.setVisible(true);
		}
	}

	public void startDodajProstorijePage(Stage primaryStage) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("FormaProstorija.fxml"));
		Scene scene = new Scene(forma);

		Stage secondStage = new Stage();
		secondStage.setTitle("Dodaj novu prostoriju");
		secondStage.setScene(scene);
		secondStage.show();

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");

		TextField sala = (TextField) scene.lookup("#sala");
		TextField zgrada = (TextField) scene.lookup("#zgrada");
		TextField kapacitet = (TextField) scene.lookup("#kapacitet");

		sala.requestFocus();

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				List<String> prostorija = new ArrayList<String>();
				if (sala.getText().trim().equals("") || zgrada.getText().trim().equals("")
						|| kapacitet.getText().toString().equals("")) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
				}

				else {

					prostorija.add(sala.getText());
					prostorija.add(zgrada.getText());
					prostorija.add(kapacitet.getText());

					try {
						int temp = Integer.parseInt(kapacitet.getText());
						int code = Lokacija.unesiLokaciju(prostorija);
						if (code == 1) {
							try {
								greska.setVisible(false);
								secondStage.close();
								startProstorije(primaryStage);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							greska.setText("Prostorija vec postoji u bazi!");
							greska.setVisible(true);
						}
					} catch (Exception e) {
						greska.setVisible(true);
						greska.setText("Pogresan kapacitet!");
					}
				}
			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startProstorije(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startUrediProfesora(Stage primaryStage, Profesor prof) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediProfesora.fxml"));
		Scene scene = new Scene(forma);

		Stage secondStage = new Stage();
		secondStage.setTitle("Uredi profesora");
		secondStage.setScene(scene);
		secondStage.show();

		Button uredi = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");
		Button obrisi = (Button) scene.lookup("#obrisi");

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

		ime.requestFocus();

		uredi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				if (ime.getText().trim().equals("") || prezime.getText().trim().equals("")
						|| usmjerenje.getValue().toString().equals("")) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
					return;
				}

				List<String> profesor = new ArrayList<String>();
				profesor.add(id.getText());
				profesor.add(ime.getText() + " " + prezime.getText());
				profesor.add(usmjerenje.getValue().toString());

				if (Profesor.updateProfesor(profesor) == true) {
					try {
						greska.setVisible(false);
						secondStage.close();
						startProfesori(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					greska.setText("Unesite ispravne podatke!");
					greska.setVisible(true);
				}
			}
		});

		obrisi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (Profesor.deleteProfesor(prof.getId()) == true) {
					try {
//						greska.setVisible(false);
						secondStage.close();
						startProfesori(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
//					greska.setVisible(true);
				}
			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startProfesori(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startUrediPredmet(Stage primaryStage, Predmet pred) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediPredmet.fxml"));
		Scene scene = new Scene(forma);

		Stage secondStage = new Stage();
		secondStage.setTitle("Uredi predmet");
		secondStage.setScene(scene);
		secondStage.show();

		Button uredi = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");
		Button obrisi = (Button) scene.lookup("#obrisi");

		TextField id = (TextField) scene.lookup("#id");
		TextField naziv = (TextField) scene.lookup("#naziv");
		TextField semestar = (TextField) scene.lookup("#semestar");
		TextField brojStudenata = (TextField) scene.lookup("#brojStudenata");
		CheckBox ar = (CheckBox) scene.lookup("#ar");
		CheckBox eems = (CheckBox) scene.lookup("#eems");
		CheckBox eske = (CheckBox) scene.lookup("#eske");
		CheckBox ri = (CheckBox) scene.lookup("#ri");
		CheckBox tk = (CheckBox) scene.lookup("#tk");
		TextField profesori = (TextField) scene.lookup("#profesori");

		id.setText(pred.getId().toString());
		naziv.setText(pred.getNaziv());
		Integer s = (Integer) pred.getSemestar();
		semestar.setText(s.toString());
		Integer b = (Integer) pred.getBrojStudenata();
		brojStudenata.setText(b.toString());
		profesori.setText(pred.getProfString());
		ar.setDisable(true);
		eems.setDisable(true);
		eske.setDisable(true);
		ri.setDisable(true);
		tk.setDisable(true);
		switch (pred.getUsmjerenje()) {
		case AR: {
			ar.setSelected(true);
			break;
		}
		case EEMS: {
			eems.setSelected(true);
			break;
		}
		case ESKE: {
			eske.setSelected(true);
			break;
		}
		case RI: {
			ri.setSelected(true);
			break;
		}
		case TK: {
			tk.setSelected(true);
			break;
		}
		}

		naziv.requestFocus();

		uredi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				if (naziv.getText().trim().equals("") || semestar.getText().trim().equals("")
						|| brojStudenata.getText().trim().equals("") || profesori.getText().trim().equals("")
						|| (!ar.isSelected() && !eems.isSelected() && !eske.isSelected() && !ri.isSelected()
								&& !tk.isSelected())) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
					return;
				}

				System.out.println("uredi");

				List<String> predmet = new ArrayList<String>();
				predmet.add(id.getText());
				predmet.add(naziv.getText());
				predmet.add(semestar.getText());
				predmet.add(brojStudenata.getText());
				predmet.add(pred.getUsmjerenje().toString());
				String[] profesoriNiz = profesori.getText().split(", ");
				for (int i = 0; i < profesoriNiz.length; ++i) {
					predmet.add(5 + i, profesoriNiz[i]);
				}

				System.out.println(predmet);

				if (Predmet.updatePredmet(predmet) == true) {
					try {
						greska.setVisible(false);
						secondStage.close();
						startPredmeti(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					greska.setText("Unesite ispravne podatke!");
					greska.setVisible(true);
				}

			}
		});

		obrisi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (Predmet.deletePredmet(pred.getId()) == true) {
					try {
//						greska.setVisible(false);
						secondStage.close();
						startPredmeti(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
//					greska.setVisible(true);
				}
			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startPredmeti(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startUrediProstoriju(Stage primaryStage, Lokacija prostorija) throws IOException {
		AnchorPane forma = FXMLLoader.load(getClass().getResource("UrediProstoriju.fxml"));
		Scene scene = new Scene(forma);

		Stage secondStage = new Stage();
		secondStage.setTitle("Uredi prostoriju");
		secondStage.setScene(scene);
		secondStage.show();

		Button uredi = (Button) scene.lookup("#dodaj");
		Button ponisti = (Button) scene.lookup("#ponisti");
		Button obrisi = (Button) scene.lookup("#obrisi");

		TextField id = (TextField) scene.lookup("#id");
		TextField sala = (TextField) scene.lookup("#sala");
		TextField zgrada = (TextField) scene.lookup("#zgrada");
		TextField kapacitet = (TextField) scene.lookup("#kapacitet");

		id.setText(prostorija.getId().toString());
		sala.setText(prostorija.getSala());
		zgrada.setText(prostorija.getZgrada());
		Integer k = (Integer) prostorija.getKapacitet();
		kapacitet.setText(k.toString());

		sala.requestFocus();

		uredi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Label greska = (Label) scene.lookup("#greska");

				if (sala.getText().trim().equals("") || zgrada.getText().trim().equals("")
						|| kapacitet.getText().toString().trim().equals("")) {
					greska.setText("Niste unijeli sve podatke!");
					greska.setVisible(true);
					return;
				}

				List<String> prostorija = new ArrayList<String>();
				prostorija.add(id.getText());
				prostorija.add(sala.getText());
				prostorija.add(zgrada.getText());
				prostorija.add(kapacitet.getText());

				if (Lokacija.updateLokacija(prostorija) == true) {
					try {
						greska.setVisible(false);
						secondStage.close();
						startProstorije(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					greska.setText("Unesite ispravne podatke!");
					greska.setVisible(true);
				}
			}
		});

		obrisi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (Lokacija.deleteLokacija(prostorija.getId()) == true) {
					try {
//						greska.setVisible(false);
						secondStage.close();
						startProstorije(primaryStage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
//					greska.setVisible(true);
				}
			}
		});

		ponisti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startProstorije(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startIzvjestajPage(Stage primaryStage, String mjesec, String datum) throws IOException {
		VBox izvjestaj = FXMLLoader.load(getClass().getResource("Izvjestaj.fxml"));
		Scene scene = new Scene(izvjestaj);

		Stage secondStage = new Stage();
		secondStage.setTitle("Izvjestaj");
		secondStage.setScene(scene);
		secondStage.show();

		Label izvrsilac1 = (Label) scene.lookup("#izvrsilac1");
		Label izvrsilac2 = (Label) scene.lookup("#izvrsilac2");
		Label ukupnoPS = (Label) scene.lookup("#up");
		Label ukupnoAS = (Label) scene.lookup("#ua");
		Label zaMjesec = (Label) scene.lookup("#zaMjesec");
		Label datumLabel = (Label) scene.lookup("#datum");

		izvrsilac1.setText("IZVRSILAC: " + trenutniKorisnik);
		izvrsilac2.setText("Izvrsilac: " + trenutniKorisnik);
		zaMjesec.setText("za mjesec " + Izvjestaj.prevediMjesec(mjesec) + " zimski/ljetni semestar ak. 2018/19 godine");
		datumLabel.setText("Datum podnosenja izvjestaja: " + datum);

		TableView<IzvjestajInfo> tabela = (TableView<IzvjestajInfo>) scene.lookup("#tabela");

		tabela.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("predmet"));
		tabela.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("datum"));
		tabela.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("mjesto"));
		tabela.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("brojStudenata"));
		tabela.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("brojP"));
		tabela.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("brojV"));

		List<IzvjestajInfo> termini = Izvjestaj.getIzvjestaj(trenutniKorisnik, mjesec);

		int ukupnoP = 0, ukupnoV = 0;

		for (IzvjestajInfo t : termini) {
			ukupnoP += Integer.parseInt(t.getBrojP());
			ukupnoV += Integer.parseInt(t.getBrojV());
		}

		ukupnoPS.setText(String.valueOf(ukupnoP));
		ukupnoAS.setText(String.valueOf(ukupnoV));

		System.out.println(termini);
		tabela.getItems().addAll(termini);
	}

	public void startTerminPage(Stage primaryStage, boolean registrovan, Collection<Termin> termini,
			List<String> vrijednosti) throws IOException {

		VBox terminPage = FXMLLoader.load(getClass().getResource("FormaTermin.fxml"));
		Scene scene = new Scene(terminPage);

		Stage secondStage = new Stage();
		secondStage.setTitle("Dodaj termin");
		secondStage.setScene(scene);
		secondStage.show();

		TextField predmet = (TextField) scene.lookup("#predmet");
		TextField pocetak = (TextField) scene.lookup("#pocetak");
		TextField kraj = (TextField) scene.lookup("#kraj");
		TextField zgrada = (TextField) scene.lookup("#zgrada");
		TextField sala = (TextField) scene.lookup("#sala");
		TextField usmjerenje = (TextField) scene.lookup("#usmjerenje");
		TextField tip = (TextField) scene.lookup("#tip");
		TextField grupa = (TextField) scene.lookup("#grupa");
		TextField profesor = (TextField) scene.lookup("#profesortext");
		Label greska = (Label) scene.lookup("#greska");
		Label profesorlabela = (Label) scene.lookup("#profesorlabel");

		Button dodaj = (Button) scene.lookup("#dodaj");
		Button nazad = (Button) scene.lookup("#nazad");

		if (trenutniKorisnik.equals("Emir Me??kovi??")) {
			profesorlabela.setVisible(true);
			profesor.setVisible(true);
		} else {
			profesorlabela.setVisible(false);
			profesor.setVisible(false);
		}

		dodaj.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				List<String> terminInfo = new ArrayList<String>();
				if (predmet.getText().equals("") || pocetak.getText().equals("") || zgrada.getText().equals("")
						|| sala.getText().equals("") || kraj.getText().equals("") || usmjerenje.getText().equals("")
						|| tip.getText().equals("")) {
					greska.setVisible(true);
					greska.setText("Niste unijeli sve podatke");
				} else {

					if (!trenutniKorisnik.equals("Emir Me??kovi??") && (tip.getText().equals("Predavanje")
							|| tip.getText().equals("Vjezbe") || tip.getText().equals("Laboratorija"))) {
						greska.setText("Nastavnik ne mo??e dodavati te termine!");
					} else {
						terminInfo.add(predmet.getText());
						terminInfo.add(pocetak.getText());
						terminInfo.add(kraj.getText());
						terminInfo.add(zgrada.getText());
						terminInfo.add(sala.getText());
						terminInfo.add(usmjerenje.getText());
						terminInfo.add(trenutniKorisnik);
						terminInfo.add(tip.getText());
						terminInfo.add(grupa.getText());
						if (trenutniKorisnik.equals("Emir Me??kovi??")) {
							terminInfo.add(profesor.getText());
						} else {
							terminInfo.add(trenutniKorisnik);
						}

						int code;
						if (tip.getText().equals("Predavanje") || tip.getText().equals("Vjezbe")
								|| tip.getText().equals("Laboratorija")) {

							code = Termin.dodajTermin(terminInfo, true);
						} else {
							code = Termin.dodajTermin(terminInfo, false);
						}

						switch (code) {
						case 1: {
							greska.setText("Dodano!");
							break;
						}
						case -1: {
							greska.setText("Taj predmet ne postoji!");
							greska.setVisible(true);
							break;
						}
						case -2: {
							greska.setText("Ne postoji trazena sala!");
							greska.setVisible(true);
							break;
						}
						case -3: {
							greska.setText("Zauzeta sala u trazenom terminu!");
							greska.setVisible(true);
							break;
						}
						case -4: {
							greska.setText("Ne postoji trazeni profesor!");
							greska.setVisible(true);
							break;
						}
						case -5: {
							greska.setText("Taj profesor ne postoji na tom predmetu!");
							greska.setVisible(true);
							break;
						}
						}
					}
				}
			}
		});

		nazad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					greska.setText("");
					secondStage.close();
					startRasporedPage(primaryStage, registrovan, Termin.getTermini(vrijednosti), vrijednosti);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startTerminObrisiPage(Stage primaryStage, boolean registrovan, Collection<Termin> termini,
			List<String> vrijednosti, Termin t) throws IOException {

		VBox terminObrisiPage = FXMLLoader.load(getClass().getResource("FormaTerminObrisi.fxml"));
		Scene scene = new Scene(terminObrisiPage);

		Stage secondStage = new Stage();
		secondStage.setTitle("Obrisi termin");
		secondStage.setScene(scene);
		secondStage.show();

		TextField predmet = (TextField) scene.lookup("#predmet");
		TextField pocetak = (TextField) scene.lookup("#pocetak");
		TextField kraj = (TextField) scene.lookup("#kraj");
		TextField zgrada = (TextField) scene.lookup("#zgrada");
		TextField sala = (TextField) scene.lookup("#sala");
		Label greska = (Label) scene.lookup("#greska");

		long id = t.getId();

		Button uredi = (Button) scene.lookup("#uredi");
		Button obrisi = (Button) scene.lookup("#izbrisi");
		Button nazad = (Button) scene.lookup("#nazad");

		CheckBox check = (CheckBox) scene.lookup("#check");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		predmet.setText(t.getPredmet().getNaziv());
		pocetak.setText(t.getStartTime().format(formatter));
		kraj.setText(t.getEndTime().format(formatter));
		zgrada.setText(t.getLokacija().getZgrada());
		sala.setText(t.getLokacija().getSala());

		uredi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				List<String> terminInfo = new ArrayList<String>();
				if (predmet.getText().equals("") || pocetak.getText().equals("") || zgrada.getText().equals("")
						|| sala.getText().equals("") || kraj.getText().equals("")) {
					greska.setVisible(true);
					greska.setText("Niste unijeli sve podatke");
				} else {
					terminInfo.add(String.valueOf(id));
					terminInfo.add(predmet.getText());
					terminInfo.add(pocetak.getText());
					terminInfo.add(kraj.getText());
					terminInfo.add(zgrada.getText());
					terminInfo.add(sala.getText());
					// Pozvati fju za UPDATE
					DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

					for (Termin t : termini) {
						if (t.getId() == id) {
							t.getPredmet().setNaziv(predmet.getText());
							t.setEndTime(LocalDateTime.parse(kraj.getText(), form));
							t.setStartTime(LocalDateTime.parse(pocetak.getText(), form));
							t.getLokacija().setZgrada(zgrada.getText());
							t.getLokacija().setSala(sala.getText());
						}
					}

					int code = Termin.updateTermin(terminInfo);
					if (code == 1) {
						try {
							startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (code == -1) {
						greska.setText("Taj predmet ne postoji!");
						greska.setVisible(true);
					}

					if (code == -2) {
						greska.setText("Ne postoji trazena sala!");
						greska.setVisible(true);
					}

					if (code == -3) {
						greska.setText("Zauzeta sala u trazenom terminu!");
						greska.setVisible(true);
					}
				}
			}
		});

		obrisi.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Pozvati funkciju za DELETE (proslijediti ID)
				Collection<Termin> obrisan = new ArrayList<>();

				for (Termin t : termini) {
					if (t.getId() != id) {
						obrisan.add(t);
					}
				}

				if (check.isSelected()) {
					Termin.deleteTermin((int) id, true);
				} else {
					Termin.deleteTermin((int) id, false);
				}

				try {
					secondStage.close();
					startRasporedPage(primaryStage, registrovan, obrisan, vrijednosti);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		nazad.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					secondStage.close();
					startRasporedPage(primaryStage, registrovan, termini, vrijednosti);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
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
		launch(args);
		factory.close();
	}
}