package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Query;

import application.Main;
import models.Korisnik.tipKorisnika;

@NamedQueries({ @NamedQuery(name = "sviProfesori", query = "select prof from Profesor prof"),
		@NamedQuery(name = "unikatniProfesori", query = "select p from Profesor p where p.id = "
				+ "(select min(p2.id) from Profesor p2 where p2.ime = p.ime and p2.usmjerenje = p.usmjerenje)") })
@Entity
public class Profesor {
	public enum Usmjerenje {
		RI, AR, ESKE, EEMS, TK
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String ime;
	@OneToMany(mappedBy = "profesor")
	private Collection<Termin> termini;
	@ManyToMany(mappedBy = "profesori")
	private Collection<Predmet> predmeti;
	@Enumerated(EnumType.STRING)
	private Usmjerenje usmjerenje;

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public Usmjerenje getUsmjerenje() {
		return this.usmjerenje;
	}

	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}

	public void setPredmete(Collection<Predmet> p) {
		this.predmeti = p;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Profesor [id=" + this.id + ", ime=" + this.ime + ", usmjerenje=" + this.usmjerenje + "]";
	}

	public Collection<Predmet> getPredmete() {
		if (this.predmeti == null) {
			this.predmeti = new ArrayList();
		}
		return this.predmeti;
	}

	public static Collection<Profesor> getProfesori() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("unikatniProfesori", Profesor.class);
		Collection<Profesor> rezultat = upit.getResultList();
		return rezultat;
	}

	public static int unesiProfesor(List<String> unos) {
		String ime = unos.get(0) + " " + unos.get(1);
		Usmjerenje usm = Usmjerenje.valueOf(unos.get(2));
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery("select t from Profesor t where t.ime='" + ime + "'", Profesor.class);
		Collection<Profesor> rezultat = upit.getResultList();
		if (rezultat.size() != 0) {
			em.close();
			return -1;
		} else {
			em.getTransaction().begin();
			Profesor novi = new Profesor();
			novi.setIme(ime);
			novi.setUsmjerenje(usm);
			Korisnik prof = new Korisnik();
			prof.setUsername(ime);
			prof.setPassword("123");
			prof.setTip(tipKorisnika.Nastavnik);
			em.persist(novi);
			em.persist(prof);
			em.getTransaction().commit();
			em.close();
			return 1;
		}
	}

	public static boolean updateProfesor(List<String> unos) {
		Long id = Long.valueOf(unos.get(0));
		String ime = unos.get(1);
		Usmjerenje usm = Usmjerenje.valueOf(unos.get(2));
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Profesor prof = em.getReference(Profesor.class, id);
		Query testniUpit = em.createQuery("select t from Profesor t where t.ime='" + ime + "'",Profesor.class);
		Collection<Profesor> provjera = testniUpit.getResultList();
		for(Profesor pp : provjera){
			if(pp.getId()!=prof.getId())
				return false;
		}
		Query upit = em.createQuery("select t from Korisnik t where t.username='" + prof.getIme() + "'",
				Profesor.class);
		Collection<Korisnik> rezultat = upit.getResultList();
		for (Korisnik k : rezultat) {
			Korisnik temp = em.getReference(Korisnik.class, k.getId());
			temp.setUsername(ime);
		}
		prof.setIme(ime);
		prof.setUsmjerenje(usm);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	public static boolean deleteProfesor(Long id) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Profesor prof = em.getReference(Profesor.class, id);
		Query termini = em.createQuery("delete from Termin t where t.profesor.ime=:var", Termin.class);
		termini.setParameter("var", prof.getIme());
		termini.executeUpdate();
		Query drugiUpit = em.createQuery("delete from Korisnik k where k.username=:tar and k.tip=:var");
		drugiUpit.setParameter("tar", prof.getIme());
		drugiUpit.setParameter("var", tipKorisnika.Nastavnik);
		drugiUpit.executeUpdate();
		Query upit = em.createQuery("delete from Profesor p where p.ime=?1");
		upit.setParameter(1, prof.getIme());
		upit.executeUpdate();
		em.getTransaction().commit();
		return true;
	}
}
