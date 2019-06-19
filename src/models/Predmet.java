package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Predmet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String naziv;
	private int brojStudenata;
	private Usmjerenje usmjerenje;
	private int semestar;
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getBrojStudenata() {
		return brojStudenata;
	}
	public void setBrojStudenata(int brojStudenata) {
		this.brojStudenata = brojStudenata;
	}
	public Usmjerenje getUsmjerenje() {
		return usmjerenje;
	}
	public void setUsmjerenje(Usmjerenje usmjerenje) {
		this.usmjerenje = usmjerenje;
	}
	

}
