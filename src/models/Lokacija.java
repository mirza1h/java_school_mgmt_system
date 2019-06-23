package models;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import application.Main;
import models.Korisnik.tipKorisnika;

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
	public static boolean unesiLokaciju(List<String> unos) {
		String zgrada=unos.get(1);
		String sala=unos.get(0);
		int kapacitet=Integer.valueOf(unos.get(2));
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery("select t from Lokacija t where t.zgrada='"+zgrada+"' and t.sala='"+sala+"'", Lokacija.class);
		Collection<Lokacija> rezultat = upit.getResultList();
		if(rezultat.size()!=0) {
			em.close();
			return false;
		}
		else {
		em.getTransaction().begin();
		Lokacija nova= new Lokacija();
		nova.setZgrada(zgrada);
		nova.setKapacitet(kapacitet);
		nova.setSala(sala);
		em.persist(nova);
		em.getTransaction().commit();
		return true;
	}
	}
}
