package models;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

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
import models.Korisnik.tipKorisnika;
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

	public Profesor getOneProfesor() {
		return this.profesori.iterator().next();
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

	public static Collection<Predmet> getPredmeti() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviPredmeti", Predmet.class);
		Collection<Predmet> rezultat = upit.getResultList();
		return rezultat;

	}
	public static boolean unesiPredmet(List<String> unos) {
		String naziv=unos.get(0);
		String semestar=unos.get(1);
		int brojst=Integer.parseInt(unos.get(2));
		Usmjerenje usm=Usmjerenje.valueOf(unos.get(3));
		String upitProf="select t from Profesor t where 1=1 and (t.ime='"+unos.get(4)+"'";
		for(int i=5;i<unos.size();i++) {
			upitProf+=" or t.ime='"+unos.get(i);
		}
		upitProf+=")";
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery(upitProf, Profesor.class);
		Collection<Profesor> rezultat = upit.getResultList();
		if(rezultat.size()==0) {
			System.out.println("Nema profesora");
			return false;
		}
		else {
			Query finalUpit =em.createQuery("select p from Predmet p where p.naziv='"+naziv+"' and p.usmjerenje=:usmj",Predmet.class);
			finalUpit.setParameter("usmj",usm);
			Collection<Predmet> rez= finalUpit.getResultList();
			if(rez.size()==0) {
				em.getTransaction().begin();
				Predmet novi=new Predmet();
				novi.setNaziv(naziv);
				novi.setBrojStudenata(brojst);
				novi.setSemestar(Integer.valueOf(semestar));
				novi.setUsmjerenje(usm);
				novi.setProfesore(rezultat);
				for(Profesor nastavnik : rezultat) {
					Profesor temp=em.getReference(Profesor.class,nastavnik.getId());
					temp.setPredmete(rez);
				}
				em.persist(novi);
				em.getTransaction().commit();
				return true;
			}
			else{
				return false;
			}
		}
	}

}
