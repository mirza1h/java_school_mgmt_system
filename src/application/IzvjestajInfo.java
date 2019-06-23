package application;

public class IzvjestajInfo {
	private String predmet;
	private String datum;
	private String mjesto;
	private String brojStudenata;
	private String brojP;
	private String brojV;

	@Override
	public String toString() {
		return "IzvjestajInfo [predmet=" + predmet + ", datum=" + datum + ", mjesto=" + mjesto + ", brojStudenata="
				+ brojStudenata + ", brojP=" + brojP + ", brojV=" + brojV + "]";
	}

	public String getPredmet() {
		return predmet;
	}

	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getMjesto() {
		return mjesto;
	}

	public void setMjesto(String mjesto) {
		this.mjesto = mjesto;
	}

	public String getBrojStudenata() {
		return brojStudenata;
	}

	public void setBrojStudenata(String brojStudenata) {
		this.brojStudenata = brojStudenata;
	}

	public String getBrojP() {
		return brojP;
	}

	public void setBrojP(String brojP) {
		this.brojP = brojP;
	}

	public String getBrojV() {
		return brojV;
	}

	public void setBrojV(String brojV) {
		this.brojV = brojV;
	}
	
}