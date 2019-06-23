package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Korisnik;
import models.Termin;

public class Izvjestaj {

	public static List<IzvjestajInfo> getIzvjestaj (String imeProfesora, String mjesec) {
		List<String> vr = new ArrayList<String>();
		String pocetak, kraj;
		
		if (mjesec.equals("JANUARY")) {
			pocetak = "01/01/2019";
			kraj = "31/01/2019";
		}
		else if (mjesec.equals("FEBRUARY")) {
			pocetak = "01/02/2019";
			kraj = "28/02/2019";
		}
		else if (mjesec.equals("MARCH")) {
			pocetak = "01/03/2019";
			kraj = "31/03/2019";
		}
		else if (mjesec.equals("APRIL")) {
			pocetak = "01/04/2019";
			kraj = "30/04/2019";
		}
		else if (mjesec.equals("MAY")) {
			pocetak = "01/05/2019";
			kraj = "31/05/2019";
		}
		else if (mjesec.equals("JUNE")) {
			pocetak = "01/06/2019";
			kraj = "30/06/2019";
		}
		else if (mjesec.equals("JULY")) {
			pocetak = "01/07/2019";
			kraj = "31/07/2019";
		}
		else if (mjesec.equals("AUGUST")) {
			pocetak = "01/08/2019";
			kraj = "31/08/2019";
		}
		else if (mjesec.equals("SEPTEMBER")) {
			pocetak = "01/09/2019";
			kraj = "30/09/2019";
		}
		else if (mjesec.equals("OCTOBER")) {
			pocetak = "01/10/2019";
			kraj = "31/10/2019";
		}
		else if (mjesec.equals("NOVEMBER")) {
			pocetak = "01/11/2019";
			kraj = "30/11/2019";
		}
		else{
			pocetak = "01/12/2019";
			kraj = "31/12/2019";
		}
			
		
		vr.add(null);
		vr.add(null);
		vr.add(imeProfesora);
		vr.add(null);
		vr.add(null);
		vr.add(null);
		vr.add(null);
		vr.add(pocetak);
		vr.add(kraj);
		
		Collection<Termin> termini = Termin.getTermini(vr);
		
		List<IzvjestajInfo> podaci = new ArrayList<>();
		
		LocalDateTime trenutniDatum = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		DateTimeFormatter formatterVrijeme = DateTimeFormatter.ofPattern("HH:mm");
		
		for (Termin t : termini) {
			IzvjestajInfo red = new IzvjestajInfo();
			red.setPredmet(t.getPredmet().getNaziv());
			red.setDatum(t.getStartTime().format(formatter));
			red.setMjesto(t.getLokacija().getZgrada()+" "+t.getLokacija().getSala() + " "
					+ t.getStartTime().format(formatterVrijeme) + " do "
					+ t.getEndTime().format(formatterVrijeme));
			red.setBrojStudenata(String.valueOf((int)(t.getPredmet().getBrojStudenata() * Math.random())));
			if (t.getTip() == Termin.tipTermina.Predavanje) {
				red.setBrojP(String.valueOf(3));
				red.setBrojV(String.valueOf(0));
			} 
			else {
				red.setBrojP(String.valueOf(0));
				red.setBrojV(String.valueOf(1));
			}
			podaci.add(red);
		}
		
		return podaci;
	}
	
	public static String prevediMjesec(String mjesec) {
		if (mjesec.equals("JANUARY")) {
			return "januar";
		}
		else if (mjesec.equals("FEBRUARY")) {
			return "februar";
		}
		else if (mjesec.equals("MARCH")) {
			return "mart";
		}
		else if (mjesec.equals("APRIL")) {
			return "april";
		}
		else if (mjesec.equals("MAY")) {
			return "maj";
		}
		else if (mjesec.equals("JUNE")) {
			return "juni";
		}
		else if (mjesec.equals("JULY")) {
			return "juli";
		}
		else if (mjesec.equals("AUGUST")) {
			return "august";
		}
		else if (mjesec.equals("SEPTEMBER")) {
			return "septembar";
		}
		else if (mjesec.equals("OCTOBER")) {
			return "oktobar";
		}
		else if (mjesec.equals("NOVEMBER")) {
			return "novembar";
		}
		else{
			return "decembar";
		}
	}
}