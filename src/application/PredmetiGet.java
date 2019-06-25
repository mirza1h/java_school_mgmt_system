package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Predmet;
import models.Profesor;

public class PredmetiGet {
	
List<PredmetiIspis> lista = new ArrayList<>();
	
	public static List<PredmetiIspis> getTablePredmeti() {
		
		Collection<Predmet> predmeti = Predmet.getPredmeti();
		List<PredmetiIspis> ret = new ArrayList<>();
		
		for (Predmet p : predmeti) {
			PredmetiIspis ispis = new PredmetiIspis();
			ispis.setNaziv(p.getNaziv());
			ispis.setId(String.valueOf(p.getId()));
			ispis.setBrStud(String.valueOf(p.getBrojStudenata()));
			ispis.setSemestar(String.valueOf(p.getSemestar()));
			if (p.getUsmjerenje() == Profesor.Usmjerenje.AR)
				ispis.setUsmjerenje("AR");
			if (p.getUsmjerenje() == Profesor.Usmjerenje.RI)
				ispis.setUsmjerenje("RI");
			if (p.getUsmjerenje() == Profesor.Usmjerenje.ESKE)
				ispis.setUsmjerenje("ESKE");
			if (p.getUsmjerenje() == Profesor.Usmjerenje.EEMS)
				ispis.setUsmjerenje("EEMS");
			if (p.getUsmjerenje() == Profesor.Usmjerenje.TK)
				ispis.setUsmjerenje("TK");
			
			Collection<Profesor> profesori = p.getProfesore();
			String novi ="";
			for (Profesor prof : profesori) {
				novi = prof.getIme() + "; ";
			}
			ispis.setProfesori(novi);
			ret.add(ispis);
		}
		return ret;
	}

}
