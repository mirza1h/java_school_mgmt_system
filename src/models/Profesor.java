package models;

import java.util.Collection;

enum Usmjerenje{
	RI,
	AR,
	ESKE,
	EEMS,
	TK
}
public class Profesor {
	private String ime;
	private String prezime;
	private Collection<Predmet> predmeti;
	private String titula;
	private Usmjerenje usmjerenje;
	private String grupa;

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getTitula() {
		return titula;
	}
	public void setTitula(String titula) {
		this.titula = titula;
	}
	public Usmjerenje getUsmjerenje() {
		return usmjerenje;
	}
	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}
}
