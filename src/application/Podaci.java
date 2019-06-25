package application;

import models.Korisnik;
import models.Korisnik.tipKorisnika;
import models.Lokacija;
import models.Predmet;
import models.Profesor;
import models.Profesor.Usmjerenje;
import models.Termin;
import models.Termin.tipTermina;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import application.Main;

public class Podaci {
	public static List<String> profesori;
	public static List<String> predmeti;

	public static void parsirajProfesore() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("/home/mirza/profesori.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delimiter = System.getProperty("line.separator");
		scanner.useDelimiter(delimiter);
		StringBuilder sb = new StringBuilder();
		profesori = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (!(line.trim().length() == 0)) {
				sb.append(line).append(delimiter);
			} else if (sb.toString().length() > 0) {
				profesori.add(sb.toString());
				sb.setLength(0);
			}
		}
		if (sb.toString().length() > 0) {
			profesori.add(sb.toString());
		}
	}

	public static void parsirajPredmete() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("/home/mirza/predmeti.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delimiter = System.getProperty("line.separator");
		scanner.useDelimiter(delimiter);
		StringBuilder sb = new StringBuilder();
		predmeti = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (!(line.trim().length() == 0)) {
				sb.append(line).append(delimiter);
			} else if (sb.toString().length() > 0) {
				predmeti.add(sb.toString());
				sb.setLength(0);
			}
		}
		if (sb.toString().length() > 0) {
			predmeti.add(sb.toString());
		}
	}

	public static void napuniBazu() {
		LocalDateTime pocetakSemestra = LocalDateTime.of(LocalDate.of(2019, 6, 10), LocalTime.of(0, 0, 0));
		int pocetak = 8;
		int trajanje = 3;
		boolean dodajSlovo = false;
		parsirajProfesore();
		// parsirajPredmete();
		String salePredavanja[] = { "FE-008", "FE-101", "STELEKT-A", "G-A" };
		String saleVjezbe[] = { "FE-005", "FE-102", "FE-RC14", "FE-RC15", "G-RC19", "G-RC21" };
		Usmjerenje[] usmjerenja = { Usmjerenje.AR, Usmjerenje.EEMS, Usmjerenje.ESKE, Usmjerenje.RI, Usmjerenje.TK };
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
//		for (int i = 0; i < profesori.size(); ++i) {
//			String[] lines = profesori.get(i).split("\\r?\\n");
//			for (int j = 0; j < lines.length; ++j) {
		Korisnik korisnik = new Korisnik();
		korisnik.setUsername("Emir Mešković");
		korisnik.setTip(tipKorisnika.Prodekan);
		korisnik.setPassword("123");
		em.persist(korisnik);
//				for (int m = 0; m < 3; ++m) {
//					Predmet predmet = new Predmet();
//					Termin termin = new Termin();
//					String sale[] = null;
//					Lokacija lokacija = new Lokacija();
//					if (m == 0) {
//						termin.setTip(tipTermina.Predavanje);
//						trajanje = 3;
//						sale = salePredavanja;
//					} else if (m == 1) {
//						termin.setTip(tipTermina.Vjezbe);
//						trajanje = 1;
//						sale = saleVjezbe;
//					} else if (m == 2) {
//						termin.setTip(tipTermina.Laboratorija);
//						trajanje = 1;
//						sale = saleVjezbe;
//					}
//					LocalDateTime vrijeme1 = pocetakSemestra.plusHours(pocetak);
//					LocalDateTime vrijeme2 = vrijeme1.plusHours(trajanje);
//					termin.setStartTime(vrijeme1);
//					termin.setEndTime(vrijeme2);
//					pocetak += trajanje;
//					if ((vrijeme2.getHour() >= 17) && (m == 0)) {
//						pocetakSemestra = pocetakSemestra.plusDays(1);
//						pocetak = 8;
//					}
//					if ((vrijeme2.getHour() >= 19) && ((m == 1) || (m == 2))) {
//						pocetakSemestra = pocetakSemestra.plusDays(1);
//						pocetak = 8;
//					}
//					if (vrijeme1.getDayOfWeek().getValue() == 5) {
//						pocetakSemestra = pocetakSemestra.plusDays(3);
//					}
//					String randomSala = sale[(int) ((Math.random() * sale.length) - 1) + 0];
//					if (randomSala.equals("FE-008") || randomSala.equals("G-A")) {
//						lokacija.setKapacitet(100);
//					} else if (randomSala.equals("FE-101") || randomSala.equals("STELEKT-A")) {
//						lokacija.setKapacitet(50);
//					} else {
//						lokacija.setKapacitet(20);
//					}
//					String salaZgrada[] = randomSala.split("-");
//					lokacija.setZgrada(salaZgrada[0]);
//					lokacija.setSala(salaZgrada[1]);
//					termin.setLokacija(lokacija);
//					predmet.setNaziv(lines[j]);
//					predmet.setUsmjerenje(usmjerenja[i]);
//					predmet.setSemestar((int) (Math.random() * 8) + 1);
//					int godina = izracunajGodinu(predmet.getSemestar());
//					String grupa = godina + "-" + predmet.getUsmjerenje();
//					termin.setGrupa(grupa);
//					predmet.setBrojStudenata((int) (Math.random() * 90) + 15);
//					int brojProfesora = (int) (Math.random() * 4) + 1;
//					String[] prof = profesori.get(i).split("\\r?\\n");
//					for (int k = 0; k < brojProfesora; ++k) {
//				Profesor profObj = new Profesor();
//						int randomProfesor = (int) (Math.random() * prof.length);
//						if (prof[randomProfesor].equals("koristen")) {
//							continue;
//						}
//				profObj.setIme(lines[j]);
//						prof[randomProfesor] = new String("koristen");
//				profObj.setUsmjerenje(usmjerenja[i]);
//						profObj.getPredmete().add(predmet);
//				em.persist(profObj);
//						predmet.getProfesore().add(profObj);
//					}
//					termin.setProfesor(predmet.getOneProfesor());
//					em.persist(lokacija);
//					termin.setPredmet(predmet);
//					em.persist(termin);
//					em.persist(predmet);
//				}
//	}

		// pocetakSemestra = LocalDateTime.of(LocalDate.of(2019, 6, 10), LocalTime.of(0,
		// 0, 0));
//	}

		em.getTransaction().commit();
		em.close();
	}

	public static int izracunajGodinu(int semestar) {
		if (semestar <= 2) {
			return 1;
		} else if (semestar <= 4) {
			return 2;
		} else if (semestar <= 6) {
			return 3;
		} else {
			return 4;
		}
	}
}