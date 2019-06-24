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
		@NamedQuery(name = "dohvatiPredmet", query = "select p from Predmet p where p.naziv = ?1"),
		@NamedQuery(name = "unikatniPredmeti", query = "select p from Predmet p where p.id = "
				+ "(select min(p2.id) from Predmet p2 where p2.naziv = p.naziv and p2.usmjerenje = p.usmjerenje)") })
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

	public String getProfString() {
		String izlaz = new String();
		for (Profesor o : this.getProfesore()) {
			izlaz += o.getIme() + " ";
		}
		return izlaz;
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
		Query upit = em.createNamedQuery("unikatniPredmeti", Predmet.class);
		Collection<Object> rezultat = upit.getResultList();
		for (Object o : rezultat) {
			System.out.println(o);
		}

	}

	public static Collection<Predmet> getPredmeti() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("unikatniPredmeti", Predmet.class);
		Collection<Predmet> rezultat = upit.getResultList();
		return rezultat;

	}

	public static boolean unesiPredmet(List<String> unos) {
		String naziv = unos.get(0);
		String semestar = unos.get(1);
		int brojst = Integer.parseInt(unos.get(2));
		Usmjerenje usm = Usmjerenje.valueOf(unos.get(3));
		String upitProf = "select t from Profesor t where 1=1 and (t.ime='" + unos.get(4) + "'";
		for (int i = 5; i < unos.size(); i++) {
			upitProf += " or t.ime='" + unos.get(i);
		}
		upitProf += ")";
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createQuery(upitProf, Profesor.class);
		Collection<Profesor> rezultat = upit.getResultList();
		if (rezultat.size() == 0) {
			System.out.println("Nema profesora");
			return false;
		} else {
			Query finalUpit = em.createQuery(
					"select p from Predmet p where p.naziv='" + naziv + "' and p.usmjerenje=:usmj", Predmet.class);
			finalUpit.setParameter("usmj", usm);
			Collection<Predmet> rez = finalUpit.getResultList();
			if (rez.size() == 0) {
				em.getTransaction().begin();
				Predmet novi = new Predmet();
				novi.setNaziv(naziv);
				novi.setBrojStudenata(brojst);
				novi.setSemestar(Integer.valueOf(semestar));
				novi.setUsmjerenje(usm);
				novi.setProfesore(rezultat);
				for (Profesor nastavnik : rezultat) {
					Profesor temp = em.getReference(Profesor.class, nastavnik.getId());
					temp.getPredmete().add(novi);
				}
				em.persist(novi);
				em.getTransaction().commit();
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean updatePredmet(List<String> unos) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Long id = Long.valueOf(unos.get(0));
		Predmet taj = em.getReference(Predmet.class, id);
		String naziv = unos.get(1);
		String semestar = unos.get(2);
		int brojst = Integer.parseInt(unos.get(3));
		Usmjerenje usm = Usmjerenje.valueOf(unos.get(4));
		String upitProf = "select distinct t from Profesor t where 1=1 and (t.ime='" + unos.get(5) + "'";
		for (int i = 6; i < unos.size(); i++) {
			upitProf += (" or t.ime='" + unos.get(i) + "'");
		}
		upitProf += ")";
		Query profesori = em.createQuery(upitProf, Profesor.class);
		Collection<Profesor> rezultat = profesori.getResultList();
		if (rezultat.size() != (unos.size() - 5)) {
			System.out.println(rezultat.size());
			System.out.println(unos.size());
			return false;
		}
		Query test = em.createQuery("select t from Predmet t where t.naziv=:var and t.usmjerenje=:tar", Predmet.class);
		test.setParameter("var", naziv);
		test.setParameter("tar", usm);
		Collection<Predmet> testRez = test.getResultList();
		if (testRez.size() != 0) {
			return false;
		}
		Collection<Profesor> ukloniti = taj.getProfesore();
		for (Profesor kk : ukloniti) {
			Profesor temp = em.getReference(Profesor.class, kk.getId());
			temp.getPredmete().remove(taj);
		}
		taj.setNaziv(naziv);
		taj.setProfesore(rezultat);
		taj.setSemestar(Integer.valueOf(semestar));
		taj.setUsmjerenje(usm);
		for (Profesor nastavnik : rezultat) {
			Profesor temp = em.getReference(Profesor.class, nastavnik.getId());
			temp.getPredmete().add(taj);
		}
		em.getTransaction().commit();

		em.close();

		return true;
	}

	public static boolean deletePredmet(Long id) {
		EntityManager em = Main.getFactory().createEntityManager();
		em.getTransaction().begin();
		Predmet p = em.getReference(Predmet.class, id);
		Query termini = em.createQuery("delete from Termin t where t.predmet.naziv = ?1");
		termini.setParameter(1, p.getNaziv());
		termini.executeUpdate();
		Query upit = em.createQuery("delete from Predmet p where p.naziv=?1");
		upit.setParameter(1, p.getNaziv());
		upit.executeUpdate();
		em.getTransaction().commit();
		return true;
	}

	public Long getId() {
		return this.id;
	}

}
