package models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

enum tipTermina {
	Predavanje, Vjezbe, Seminar, Nadoknada, Diplomski
}

@Entity
public class Termin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Predmet predmet;
	// Mozemo ovo staviti ko jedan string ?
	private String zgrada;
	private String sala;
	private Date vrijeme1;
	private Date vrijeme2;
	private tipTermina tip;

	public Predmet getPredmet() {
		return this.predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
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

	public Date getVrijeme1() {
		return this.vrijeme1;
	}

	public void setVrijeme1(Date vrijeme1) {
		this.vrijeme1 = vrijeme1;
	}

	public Date getVrijeme2() {
		return this.vrijeme2;
	}

	public void setVrijeme2(Date vrijeme2) {
		this.vrijeme2 = vrijeme2;
	}

	public tipTermina getTip() {
		return this.tip;
	}

	public void setTip(tipTermina tip) {
		this.tip = tip;
	}

	@Override
	public String toString() {
		return "Termin [id=" + this.id + ", predmet=" + this.predmet + ", zgrada=" + this.zgrada + ", sala=" + this.sala + ", vrijeme1="
				+ this.vrijeme1 + ", vrijeme2=" + this.vrijeme2 + ", tip=" + this.tip + "]";
	}

}
