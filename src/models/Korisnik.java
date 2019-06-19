package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

enum tipKorisnika{
	Prodekan,Nastavnik,Student
}
@Entity
public class Korisnik {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	private String username;
	private String password;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public tipKorisnika getTip() {
		return tip;
	}
	public void setTip(tipKorisnika tip) {
		this.tip = tip;
	}
	private tipKorisnika tip;
	 

}
