package models;

import java.util.ArrayList;
import java.util.Collection;
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

@NamedQueries({ @NamedQuery(name = "sviProfesori", query = "select prof from Profesor prof") })
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
	private String grupa;

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
		return id;
	}

	@Override
	public String toString() {
		return "Profesor [id=" + this.id + ", ime=" + this.ime + ", usmjerenje=" + this.usmjerenje + ", grupa="
				+ this.grupa + "]";
	}

	public Collection<Predmet> getPredmete() {
		if (this.predmeti == null) {
			this.predmeti = new ArrayList();
		}
		return this.predmeti;
	}

	public static Collection<Profesor> getProfesori() {
		EntityManager em = Main.getFactory().createEntityManager();
		Query upit = em.createNamedQuery("sviProfesori", Profesor.class);
		Collection<Profesor> rezultat = upit.getResultList();
		return rezultat;
	}
}
