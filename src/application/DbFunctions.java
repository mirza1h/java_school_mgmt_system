package application;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Korisnik;
import models.Korisnik.tipKorisnika;
import models.Lokacija;
import models.Predmet;
import models.Profesor.Usmjerenje;
import models.Termin;
import models.Termin.tipTermina;

public class DbFunctions {
	public static void createPredmet() {
		EntityManager em=Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Predmet novi=new Predmet();
		novi.setBrojStudenata(300);
		novi.setNaziv("Osnovi elektronike");
		novi.setUsmjerenje(Usmjerenje.TK);
		em.persist(novi);
		Predmet novi2=new Predmet();
		novi2.setBrojStudenata(35);
		novi2.setNaziv("Razvoj softvera");
		novi2.setUsmjerenje(Usmjerenje.RI);
		em.persist(novi2);
		em.getTransaction().commit();
		em.close();
	}
	public static void createTermini() {
		EntityManager em=Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Collection<Predmet> predmeti=Predmet.getPredmeti();
		ArrayList<Predmet> pr=new ArrayList<Predmet>();
		for (Object o: predmeti) {
			pr.add((Predmet) o);
		}
		Termin novi= new Termin();
		novi.setPredmet(pr.get(0));
		LocalDateTime vrijeme1=LocalDateTime.of(LocalDate.of(2019, 6, 21),LocalTime.of(13, 0,0));
		LocalDateTime vrijeme2=LocalDateTime.of(LocalDate.of(2019, 6, 21),LocalTime.of(16, 0,0));
		novi.setStartTime(vrijeme1);
		novi.setEndTime(vrijeme2);
		novi.setTip(tipTermina.Predavanje);
		novi.setLokacija(new Lokacija("FE","101"));
		em.persist(novi);
		Termin novi2= new Termin();
		novi2.setPredmet(pr.get(1));
		LocalDateTime vrijeme12=LocalDateTime.of(LocalDate.of(2019, 6, 21),LocalTime.of(16, 0,0));
		LocalDateTime vrijeme22=LocalDateTime.of(LocalDate.of(2019, 6, 21),LocalTime.of(19, 0,0));
		novi2.setStartTime(vrijeme12);
		novi2.setEndTime(vrijeme22);
		novi2.setTip(tipTermina.Predavanje);
		novi.setLokacija(new Lokacija("FE","101"));
		em.persist(novi2);
		em.getTransaction().commit();
		em.close();
		
	}
	public static void addProdekan() {
		EntityManager em=Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Korisnik mesak=new Korisnik();
		mesak.setUsername("Emir Mešković");
		mesak.setPassword("123");
		mesak.setTip(tipKorisnika.Prodekan);
		em.persist(mesak);
		em.getTransaction().commit();
		em.close();
		
	}
	
	public static void addProfesor() {
		EntityManager em=Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Korisnik novi=new Korisnik();
		novi.setUsername("Emir Mešković");
		novi.setPassword("123");
		novi.setTip(tipKorisnika.Nastavnik);
		em.persist(novi);
		em.getTransaction().commit();
		em.close();
		
	}

}
