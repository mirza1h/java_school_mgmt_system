package models;
import java.sql.Date;
enum tipTermina{
	Predavanje,
	Vjezbe,
	Seminar,
	Nadoknada,
	Diplomski
}
public class Termin {
	private Predmet predmet;
	//Mozemo ovo staviti ko jedan string ?
	private String zgrada;
	private String sala;
	private Date vrijeme1;
	private Date vrijeme2;
	private tipTermina tip;
	public Predmet getPredmet() {
		return predmet;
	}
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}
	public String getZgrada() {
		return zgrada;
	}
	public void setZgrada(String zgrada) {
		this.zgrada = zgrada;
	}
	public String getSala() {
		return sala;
	}
	public void setSala(String sala) {
		this.sala = sala;
	}
	public Date getVrijeme1() {
		return vrijeme1;
	}
	public void setVrijeme1(Date vrijeme1) {
		this.vrijeme1 = vrijeme1;
	}
	public Date getVrijeme2() {
		return vrijeme2;
	}
	public void setVrijeme2(Date vrijeme2) {
		this.vrijeme2 = vrijeme2;
	}
	public tipTermina getTip() {
		return tip;
	}
	public void setTip(tipTermina tip) {
		this.tip = tip;
	}

}
