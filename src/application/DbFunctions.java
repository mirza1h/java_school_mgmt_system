package application;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.Predmet;
import models.Termin;
import models.Termin.tipTermina;

public class DbFunctions {
	public static void createTermini() {
		EntityManager em=Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Collection<Object> predmeti=Predmet.getPredmeti();
		ArrayList<Predmet> pr=new ArrayList<Predmet>();
		for (Object o: predmeti) {
			pr.add((Predmet) o);
		}
		Termin novi= new Termin();
		novi.setPredmet(pr.get(0));
		Timestamp vrijeme1=Timestamp.valueOf("2019-06-21 13:00:00");
		Timestamp vrijeme2=Timestamp.valueOf("2019-06-21 16:00:00");
		novi.setVrijeme1(vrijeme1);
		novi.setVrijeme2(vrijeme2);
		novi.setTip(tipTermina.Predavanje);
		novi.setZgrada("FE");
		novi.setSala("008");
		em.persist(novi);
		Termin novi2= new Termin();
		novi.setPredmet(pr.get(1));
		Timestamp vrijeme12=Timestamp.valueOf("2019-06-21 16:00:00");
		Timestamp vrijeme22=Timestamp.valueOf("2019-06-21 19:00:00");
		novi.setVrijeme1(vrijeme12);
		novi.setVrijeme2(vrijeme22);
		novi.setTip(tipTermina.Predavanje);
		novi.setZgrada("FE");
		novi.setSala("101");
		em.persist(novi2);
		em.getTransaction().commit();
		em.close();
		
		
	}

}
