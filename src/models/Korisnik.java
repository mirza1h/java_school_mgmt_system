package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

enum tipKorisnika {
	Prodekan, Nastavnik, Student
}

@NamedQueries({ @NamedQuery(name = "sviKorisnici", query = "select k from Korisnik k"),
		@NamedQuery(name = "dohvatiKorisnika", query = "select k from Korisnik k where k.username = ?1 and k.password = ?2") })
@Entity
public class Korisnik {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	private String username;
	private String password;

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

	public tipKorisnika getTip() {
		return this.tip;
	}

	public void setTip(tipKorisnika tip) {
		this.tip = tip;
	}

	private tipKorisnika tip;

}
