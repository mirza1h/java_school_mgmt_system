package models;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


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
	private String prezime;
	@OneToMany
	private Collection<Termin> termini;
	@ManyToMany(mappedBy = "profesori")
	private Collection<Predmet> predmeti;
	private String titula;
	private Usmjerenje usmjerenje;
	private String grupa;

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getTitula() {
		return this.titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
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

	@Override
	public String toString() {
		return "Profesor [id=" + this.id + ", ime=" + this.ime + ", prezime=" + this.prezime + ", predmeti=" + this.predmeti + ", titula="
				+ this.titula + ", usmjerenje=" + this.usmjerenje + ", grupa=" + this.grupa + "]";
	}

}
