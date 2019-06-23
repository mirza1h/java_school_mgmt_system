package models;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import application.Main;

@NamedQueries({ @NamedQuery(name = "sveLokacije", query = "select t from Lokacija t") })
@Entity
public class Lokacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String zgrada;
	private String sala;
	private int kapacitet;

	public Lokacija() {
	}

	public Lokacija(String a, String b) {
		this.zgrada = a;
		this.sala = b;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getKapacitet() {
		return this.kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	@Override
	public String toString() {
		return "Lokacija [zgrada=" + this.zgrada + ", sala=" + this.sala + "]";
	}
	public static Collection<Lokacija> getLokacije() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sveLokacije", Lokacija.class);
		Collection<Lokacija> rezultat = upit.getResultList();
		return rezultat;

	}
}
