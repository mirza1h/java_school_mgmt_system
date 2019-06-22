package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Lokacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String zgrada;
	private String sala;
	private int kapacitet;

	public Lokacija() {
	}

	public Lokacija(String a, String b) {
		this.zgrada = a;
		this.sala = b;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getKapacitet() {
		return this.kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	@Override
	public String toString() {
		return "Lokacija [zgrada=" + this.zgrada + ", sala=" + this.sala + "]";
	}
}
