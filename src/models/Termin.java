package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

	public static void deleteTermin(int id) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Query upit = em.createNamedQuery("izbrisiTermin", Termin.class);
		upit.setParameter(1, id);
		upit.executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	public String getGrupa() {
		return this.grupa;
	}

	public void setGrupa(String grupa) {
		this.grupa = grupa;
	}

	public static Collection<Termin> getTermini(List<String> vrijednosti) {
		boolean datum=false;
		LocalDateTime datumPrvi=null;
		LocalDateTime datumDrugi=null;
		String finalQuery = "select t from Termin t where 1=1";
		if (vrijednosti.get(0) != null) {
			finalQuery = finalQuery + " and t.lokacija.zgrada like '" + vrijednosti.get(0) + "'";
		}
		if (vrijednosti.get(1) != null) {
			finalQuery = finalQuery + " and t.lokacija.sala like '" + vrijednosti.get(1) + "'";
		}
		if (vrijednosti.get(2) != null) {
			finalQuery = finalQuery + " and t.profesor.ime_prezime like '" + vrijednosti.get(2) + "'";
		}
		if (vrijednosti.get(3) != null) {
			finalQuery = finalQuery + " and (t.predmet.semestar=" + (2 * Integer.parseInt(vrijednosti.get(3)))
					+ " or t.predmet.semestar=" + ((2 * Integer.parseInt(vrijednosti.get(3))) + 1) + ")";
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
		if(vrijednosti.get(7)!=null && vrijednosti.get(8)!=null) {
			DateTimeFormatter nesto=DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate datum1=LocalDate.parse(vrijednosti.get(7), nesto);
			datumPrvi=LocalDateTime.of(datum1,LocalTime.of(8, 0));
			LocalDate datum2=LocalDate.parse(vrijednosti.get(8), nesto);
			datumDrugi=LocalDateTime.of(datum2,LocalTime.of(8, 0));
			finalQuery = finalQuery + " and t.startTime >= :prvo and t.endTime <= :drugo";
			datum=true;
			
			
		}
		

		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery(finalQuery, Termin.class);
		if(datum) {
			upit.setParameter("prvo",datumPrvi);
			upit.setParameter("drugo",datumDrugi );
		}
		Collection<Termin> rezultat = upit.getResultList();
		for (Termin o : rezultat) {
			System.out.println(o);
		}
		em.close();

		return rezultat;
	}

}
