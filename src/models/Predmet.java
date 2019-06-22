package models;

import java.util.ArrayList;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import application.Main;
import models.Profesor.Usmjerenje;

@Entity
@NamedQueries({ @NamedQuery(name = "sviPredmeti", query = "select p from Predmet p"),
		@NamedQuery(name = "sviPredmetiProfesora", query = "select pred from Predmet pred, Profesor prof WHERE pred.profesori = prof AND prof.ime = ?1"),
		@NamedQuery(name = "dohvatiPredmet", query = "select p from Predmet p where p.naziv = ?1") })
public class Predmet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String naziv;
	private int brojStudenata;
	@Enumerated(EnumType.STRING)
	private Usmjerenje usmjerenje;
	private int semestar;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Collection<Profesor> profesori;

	public int getSemestar() {
		return this.semestar;
	}

	public void setSemestar(int semestar) {
		this.semestar = semestar;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public int getBrojStudenata() {
		return this.brojStudenata;
	}

	public void setBrojStudenata(int brojStudenata) {
		this.brojStudenata = brojStudenata;
	}

	public Usmjerenje getUsmjerenje() {
		return this.usmjerenje;
	}

	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}

	public void setProfesore(Collection<Profesor> p) {
		this.profesori = p;
	}

	@Override
	public String toString() {
		return "Predmet [id=" + this.id + ", naziv=" + this.naziv + ", brojStudenata=" + this.brojStudenata
				+ ", usmjerenje=" + this.usmjerenje + ", semestar=" + this.semestar + ", profesori=" + this.profesori
				+ "]";
	}

	public Collection<Profesor> getProfesore() {
		if (this.profesori == null) {
			this.profesori = new ArrayList<Profesor>();
		}
		return this.profesori;
	}

	public static void showPredmeti() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviPredmeti", Predmet.class);
		Collection<Object> rezultat = upit.getResultList();
		for (Object o : rezultat) {
			System.out.println(o);
		}

	}

	public static Collection<Object> getPredmeti() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviPredmeti", Predmet.class);
		Collection<Object> rezultat = upit.getResultList();
		return rezultat;

	}

}
