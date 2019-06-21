package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Query;

import org.apache.derby.client.am.DateTime;

import application.Main;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



@NamedQueries({ @NamedQuery(name = "sviTermini", query = "select t from Termin t"),
		@NamedQuery(name = "sviTerminiZaVrijeme", query = "select t from Termin t where t.vrijeme1 = ?1 and t.vrijeme2 = ?2"),
		@NamedQuery(name = "izbrisiTermin", query= "delete from Termin t where t.id = ?1")
})
@Entity
public class Termin {
	public enum tipTermina {
		Predavanje, Vjezbe, Seminar, Nadoknada, Diplomski
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Predmet predmet;
	// Mozemo ovo staviti ko jedan string ?
	private String zgrada;
	private String sala;
	private String grupa;
	private Timestamp vrijeme1;
	private Timestamp vrijeme2;
	@ManyToOne
	private Profesor profesor;
	private tipTermina tip;

	public Predmet getPredmet() {
		return this.predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public String getZgrada() {
		return this.zgrada;
	}

	public void setZgrada(String zgrada) {
		this.zgrada = zgrada;
	}

	public String getSala() {
		return this.sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public Timestamp getVrijeme1() {
		return this.vrijeme1;
	}

	public void setVrijeme1(Timestamp vrijeme12) {
		this.vrijeme1 = vrijeme12;
	}

	public Timestamp getVrijeme2() {
		return this.vrijeme2;
	}

	public void setVrijeme2(Timestamp vrijeme2) {
		this.vrijeme2 = vrijeme2;
	}

	public tipTermina getTip() {
		return this.tip;
	}

	public void setTip(tipTermina tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return "Termin [id=" + this.id + ", predmet=" + this.predmet + ", zgrada=" + this.zgrada + ", sala=" + this.sala
				+ ", vrijeme1=" + this.vrijeme1 + ", vrijeme2=" + this.vrijeme2 + ", tip=" + this.tip + "]";
	}
	public static void showTermini() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviTermini", Termin.class);
		Collection<Object> rezultat = upit.getResultList();
		for(Object o: rezultat) {
			System.out.println(o);
		}
		em.close();
		
	}
	public static void deleteTermin(int id) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Query upit = em.createNamedQuery("izbrisiTermin", Termin.class);
		upit.setParameter(1,id);
		upit.executeUpdate();
		em.getTransaction().commit();
		em.close();
	}

	public String getGrupa() {
		return grupa;
	}

	public void setGrupa(String grupa) {
		this.grupa = grupa;
	}

}
