package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import org.apache.derby.client.am.DateTime;

import application.Main;
import models.Korisnik.tipKorisnika;
import models.Profesor.Usmjerenje;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({ @NamedQuery(name = "sviTermini", query = "select t from Termin t"),
		@NamedQuery(name = "sviTerminiZaVrijeme", query = "select t from Termin t where t.startTime = ?1 and t.endTime = ?2"),
		@NamedQuery(name = "izbrisiTermin", query = "delete from Termin t where t.id = ?1") })
@Entity
public class Termin {
	public enum tipTermina {
		Predavanje, Vjezbe, Seminar, Nadoknada, Diplomski, Laboratorija
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Predmet predmet;
	@OneToOne
	private Lokacija lokacija;
	private String grupa;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime startTime;
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime endTime;
	@ManyToOne
	private Profesor profesor;
	@Enumerated(EnumType.STRING)
	private tipTermina tip;

	public Lokacija getLokacija() {
		return this.lokacija;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public Predmet getPredmet() {
		return this.predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public tipTermina getTip() {
		return this.tip;
	}

	public void setTip(tipTermina tip) {
		this.tip = tip;
	}

	public Profesor getProfesor() {
		return this.profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	@Override
	public String toString() {
		return "Termin [lokacija=" + this.lokacija + ", vrijeme1=" + this.startTime + ", vrijeme2=" + this.endTime
				+ ", grupa=" + this.grupa + ", tip=" + this.tip + "]";
	}

	public static void showTermini() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviTermini", Termin.class);
		Collection<Object> rezultat = upit.getResultList();
		for (Object o : rezultat) {
			System.out.println(o);
		}
		em.close();

	}

	public static void deleteTermin(long id, boolean sve) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		if (sve) {
			Termin temp = em.getReference(Termin.class, id);
			Query ostaliTermini = em.createQuery(
					"select p from Termin p where p.predmet.naziv=:var and p.tip=:sar and p.grupa=:ban", Termin.class);
			ostaliTermini.setParameter("var", temp.getPredmet().getNaziv());
			ostaliTermini.setParameter("sar", temp.getTip());
			ostaliTermini.setParameter("ban", temp.getGrupa());
			List<Termin> rez = ostaliTermini.getResultList();
			for (Termin o : rez) {
				Query izbrisiTermini = em.createQuery("delete from Termin p where p.id=:tar", Termin.class);
				izbrisiTermini.setParameter("tar", o.getId());
				izbrisiTermini.executeUpdate();

			}
			em.getTransaction().commit();
			em.close();
		} else {
			Query upit = em.createNamedQuery("izbrisiTermin", Termin.class);
			upit.setParameter(1, id);
			upit.executeUpdate();
			em.getTransaction().commit();
			em.close();
		}
	}

	public String getGrupa() {
		return this.grupa;
	}

	public void setGrupa(String grupa) {
		this.grupa = grupa;
	}

	public static Collection<Termin> getTermini(List<String> vrijednosti) {
		boolean datum = false;
		LocalDateTime datumPrvi = null;
		LocalDateTime datumDrugi = null;
		String finalQuery = "select distinct t from Termin t where 1=1";
		if (vrijednosti.get(0) != null) {
			finalQuery = finalQuery + " and t.lokacija.zgrada like '" + vrijednosti.get(0) + "'";
		}
		if (vrijednosti.get(1) != null) {
			finalQuery = finalQuery + " and t.lokacija.sala like '" + vrijednosti.get(1) + "'";
		}
		if (vrijednosti.get(2) != null) {
			finalQuery = finalQuery + " and t.profesor.ime like '" + vrijednosti.get(2) + "'";
		}
		if (vrijednosti.get(3) != null) {
			finalQuery = finalQuery + " and (t.predmet.semestar=" + ((2 * Integer.parseInt(vrijednosti.get(3))) - 1)
					+ " or t.predmet.semestar=" + ((2 * Integer.parseInt(vrijednosti.get(3)))) + ")";
		}
		if (vrijednosti.get(4) != null) {
			finalQuery = finalQuery + " and t.predmet.naziv like '" + vrijednosti.get(4) + "'";
		}
		if (vrijednosti.get(5) != null) {
			finalQuery = finalQuery + " and t.grupa like '" + vrijednosti.get(5) + "'";
		}
		if (vrijednosti.get(6) != null) {
			finalQuery = finalQuery + " and t.predmet.usmjerenje like '" + vrijednosti.get(6) + "'";
		}
		if ((vrijednosti.get(7) != null) && (vrijednosti.get(8) != null)) {
			DateTimeFormatter nesto = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate datum1 = LocalDate.parse(vrijednosti.get(7), nesto);
			datumPrvi = LocalDateTime.of(datum1, LocalTime.of(8, 0));
			LocalDate datum2 = LocalDate.parse(vrijednosti.get(8), nesto);
			datumDrugi = LocalDateTime.of(datum2, LocalTime.of(8, 0));
			finalQuery = finalQuery + " and t.startTime >= :prvo and t.endTime <= :drugo";
			datum = true;

		}

		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery(finalQuery, Termin.class);
		if (datum) {
			upit.setParameter("prvo", datumPrvi);
			upit.setParameter("drugo", datumDrugi);
		}
		Collection<Termin> rezultat = upit.getResultList();
		em.close();

		return rezultat;
	}

	public static int dodajTermin(List<String> unos,boolean sedmice) {
		String nazivPred=unos.get(0);
		DateTimeFormatter form= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime datum1=LocalDateTime.parse(unos.get(1),form);
		LocalDateTime datum2=LocalDateTime.parse(unos.get(2),form);
		String zgrada=unos.get(3);
		String sala=unos.get(4);
		Usmjerenje usm=Usmjerenje.valueOf(unos.get(5));
		String korisnik=unos.get(6);
		tipTermina tip=tipTermina.valueOf(unos.get(7));
		String grupa=unos.get(8);
		String kojemProf=unos.get(9);
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Query predmUpit=em.createQuery("select p from Predmet p where p.naziv=:var and p.usmjerenje=:tar",Predmet.class);
		predmUpit.setParameter("var", nazivPred);
		predmUpit.setParameter("tar", usm);
		List<Predmet> broj=predmUpit.getResultList();
		if(broj.size()==0) {
			return -1;
		}
		Query lokacijaUpit=em.createQuery("select p from Lokacija p where p.zgrada=:sar and p.sala=:lar",Lokacija.class);
		lokacijaUpit.setParameter("sar",zgrada);
		lokacijaUpit.setParameter("lar", sala);
		List<Lokacija> brojLok=lokacijaUpit.getResultList();
		if(brojLok.size()==0) {
			System.out.println("Nema lokacije");
			return -2;
		}
		Query terminUpit=em.createQuery("select p from Termin p where p.startTime>=:kar and p.endTime<=:dar and p.lokacija.zgrada=:moj and "
				+ "p.lokacija.sala=:tvoj",Termin.class);
		terminUpit.setParameter("kar",datum1);
		terminUpit.setParameter("dar",datum2);
		terminUpit.setParameter("moj", zgrada);
		terminUpit.setParameter("tvoj", sala);
		Collection<Termin> brojTer=terminUpit.getResultList();
		if(brojTer.size()!=0) {
			System.out.println("Zauzet termin");
			return -3;
		}
		
		Query korisnikUpit=em.createQuery("select p from Profesor p where p.ime=:mar",Profesor.class);
		korisnikUpit.setParameter("mar",kojemProf);
		List<Profesor> brojProf=korisnikUpit.getResultList();
		if(brojProf.size()==0) {
			System.out.println("Nema profesora sa tim imenom");
			return -4;
		}
		Query profnaPredUpit=em.createQuery("select p from Profesor p where p.ime=:yar and p.predmeti IN :qar",Profesor.class);
		profnaPredUpit.setParameter("yar",kojemProf);
		profnaPredUpit.setParameter("qar", broj);
		List<Profesor> brojprofnaPred=profnaPredUpit.getResultList();
		if(brojprofnaPred.size()==0) {
			System.out.println("Nema profesora na tom predmetu");
			return -5;
		}
		
		Termin novi=new Termin();
		novi.setPredmet(broj.get(0));
		novi.setLokacija(brojLok.get(0));
		novi.setProfesor(brojProf.get(0));
		novi.setTip(tip);
		novi.setStartTime(datum1);
		novi.setEndTime(datum2);
		novi.setGrupa(grupa);
		if(sedmice) {
			int mjesec = datum1.getMonthValue();
			int dan=datum1.getDayOfMonth();
			for(;mjesec<=6;mjesec=datum1.getMonthValue()) {
				System.out.println(datum1);
				datum1=datum1.plusDays(7);
				datum2=datum2.plusDays(7);
				List<String> listic=new ArrayList<String>();
				String mojDatum1=datum1.format(form);
				String mojDatum2=datum2.format(form);
				listic.add(nazivPred);
				listic.add(mojDatum1);
				listic.add(mojDatum2);
				listic.add(zgrada);
				listic.add(sala);
				listic.add(usm.toString());
				listic.add(korisnik);
				listic.add(tip.toString());
				listic.add(grupa);
				listic.add(kojemProf);
				Termin.dodajTermin(listic, false);
			}
			for(;mjesec>=9;mjesec=datum1.getMonthValue()) {
				datum1=datum1.plusDays(7);
				datum2=datum2.plusDays(7);
				List<String> listic=new ArrayList<String>();
				String mojDatum1=datum1.format(form);
				String mojDatum2=datum2.format(form);
				listic.add(nazivPred);
				listic.add(mojDatum1);
				listic.add(mojDatum2);
				listic.add(zgrada);
				listic.add(sala);
				listic.add(usm.toString());
				listic.add(korisnik);
				listic.add(tip.toString());
				listic.add(grupa);
				listic.add(kojemProf);
				Termin.dodajTermin(listic, false);
			}
		}
		em.persist(novi);
		em.getTransaction().commit();
		em.close();
		
		
		return 1;
	}
	public static int updateTermin(List<String> unos) {
		Long id=Long.valueOf(unos.get(0));
		String nazivPred=unos.get(1);
		DateTimeFormatter form= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime datum1=LocalDateTime.parse(unos.get(2),form);
		LocalDateTime datum2=LocalDateTime.parse(unos.get(3),form);
		String zgrada=unos.get(4);
		String sala=unos.get(5);
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Termin temp=em.getReference(Termin.class,id);
		Query predmUpit=em.createQuery("select p from Predmet p where p.naziv=:var",Predmet.class);
		predmUpit.setParameter("var", nazivPred);
		List<Predmet> broj=predmUpit.getResultList();
		if(broj.size()==0) {
			return -1;
		}
		Query lokacijaUpit=em.createQuery("select p from Lokacija p where p.zgrada=:sar and p.sala=:lar",Lokacija.class);
		lokacijaUpit.setParameter("sar",zgrada);
		lokacijaUpit.setParameter("lar", sala);
		List<Lokacija> brojLok=lokacijaUpit.getResultList();
		if(brojLok.size()==0) {
			System.out.println("Nema lokacije");
			return -2;
		}
		Query terminUpit=em.createQuery("select p from Termin p where p.startTime>=:kar and p.endTime<=:dar and p.lokacija.zgrada=:moj and "
				+ "p.lokacija.sala=:tvoj",Termin.class);
		terminUpit.setParameter("kar",datum1);
		terminUpit.setParameter("dar",datum2);
		terminUpit.setParameter("moj", zgrada);
		terminUpit.setParameter("tvoj", sala);
		Collection<Termin> brojTer=terminUpit.getResultList();
		for(Termin t : brojTer) {
			if(t.getId()!=temp.getId())
				return -3;
		}
		temp.setPredmet(broj.get(0));
		temp.getLokacija().setZgrada(zgrada);
		temp.getLokacija().setSala(sala);
		temp.setStartTime(datum1);
		temp.setEndTime(datum2);
		em.getTransaction().commit();
		em.close();
		
		return 1;
	}

}

