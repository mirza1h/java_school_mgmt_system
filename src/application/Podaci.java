package application;

import models.Predmet;
import models.Profesor;
import models.Profesor.Usmjerenje;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
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

	/**
	 * 
	 */
	public static void dodajProfesore() {
		parsirajProfesore();
		parsirajPredmete();
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Usmjerenje[] usmjerenja = { Usmjerenje.AR, Usmjerenje.EEMS, Usmjerenje.ESKE, Usmjerenje.RI, Usmjerenje.TK };
		for (int i = 0; i < predmeti.size(); ++i) {
			String[] lines = predmeti.get(i).split("\\r?\\n");
			for (int j = 0; j < lines.length; ++j) {
				Predmet predmet = new Predmet();
				predmet.setNaziv(lines[j]);
				predmet.setUsmjerenje(usmjerenja[i]);
				predmet.setSemestar((int) (Math.random() * 8) + 1);
				predmet.setBrojStudenata((int) (Math.random() * 90) + 15);
				int brojProfesora = (int) (Math.random() * 4) + 1;
				String[] prof = profesori.get(i).split("\\r?\\n");
				for (int k = 0; k < brojProfesora; ++k) {
					Profesor profObj = new Profesor();
					int randomProfesor = (int) (Math.random() * prof.length);
					if (prof[randomProfesor].equals("koristen")) {
						continue;
					}
					profObj.setIme(prof[randomProfesor]);
					prof[randomProfesor] = new String("koristen");
					profObj.setUsmjerenje(usmjerenja[i]);
					profObj.getPredmete().add(predmet);
					// em.persist(predmetObj);
					predmet.getProfesore().add(profObj);
				}
				em.persist(predmet);
			}
		}

		em.getTransaction().commit();
		em.close();
	}
}
