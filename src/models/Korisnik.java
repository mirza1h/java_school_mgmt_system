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

@NamedQueries({ @NamedQuery(name = "sviKorisnici", query = "select k from Korisnik k"),
		@NamedQuery(name = "dohvatiKorisnika", query = "select k from Korisnik k where k.username = ?1 and k.password = ?2") })
@Entity
public class Korisnik {
	public enum tipKorisnika {
		Prodekan, Nastavnik, Student
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String username;
	private String password;
	private tipKorisnika tip;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Korisnik [id=" + this.id + ", username=" + this.username + ", password=" + this.password + ", tip=" + this.tip + "]";
	}

	public tipKorisnika getTip() {
		return this.tip;
	}

	public void setTip(tipKorisnika tip) {
		this.tip = tip;
	}
	
	public static tipKorisnika nadjiKorisnika(String name, String pass) {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("dohvatiKorisnika", Korisnik.class);
		upit.setParameter(1, name);
		upit.setParameter(2, pass);
		Collection<Object> rezultat = upit.getResultList();
		Korisnik temp=null;
		for(Object o: rezultat) {
			temp=(Korisnik)o;
		}
		if(temp==null)
			return null;
		return temp.getTip();
		
	}
	public static void showKorisnici() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviKorisnici", Korisnik.class);
		Collection<Korisnik> rezultat=upit.getResultList();
		for(Korisnik o:rezultat) {
			System.out.println(o);
		}
		}

}
