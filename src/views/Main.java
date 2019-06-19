package views;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import models.Predmet;
import models.Profesor;

public class Main {
	private static final String PERSISTENCE_UNIT_NAME = "RazvojSoftvera";
	private static EntityManagerFactory factory;

	private static void setDBSystemDir() {
		// Decide on the db system directory: <userhome>/.addressbook/
		String userHomeDir = System.getProperty("user.home", ".");
		String systemDir = userHomeDir + "/.addressbook";
		// Set the db system directory.
		System.setProperty("derby.system.home", systemDir);
	}

	public static void main(String[] args) {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		setDBSystemDir();
		/*
		 * Profesor amer = new Profesor(); amer.setIme("Amer"); Profesor emir = new
		 * Profesor(); emir.setIme("Emir"); Predmet mreze = new Predmet();
		 * mreze.setNaziv("Racunarske mreze"); mreze.getProfesore().add(amer); Predmet
		 * razvoj = new Predmet(); razvoj.setNaziv("Razvoj softvera");
		 * razvoj.getProfesore().add(emir);
		 */

		/*
		 * Profesor dario = new Profesor(); dario.setIme("Dario"); Query qp =
		 * em.createNamedQuery("dohvatiPredmet"); qp.setParameter(1, "Razvoj softvera");
		 * Predmet razvoj = (Predmet) qp.getSingleResult();
		 * razvoj.getProfesore().add(dario); em.getTransaction().begin();
		 * em.persist(razvoj); em.getTransaction().commit();
		 */
		Query q = em.createNamedQuery("sviProfesori");
		List<Profesor> lista = q.getResultList();
		for (Profesor prof : lista) {
			System.out.println(prof);
		}

		em.close();
	}

}