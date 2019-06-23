package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import models.Predmet;
import models.Profesor;

public class ProfesoriGet {

	List<ProfesoriIspis> lista = new ArrayList<>();
	
	public static List<ProfesoriIspis> getTableProfesor() {
		Collection<Profesor> profesori = Profesor.getProfesori();
		List<ProfesoriIspis> ret = new ArrayList<>();
		
		for (Profesor p : profesori) {
			ProfesoriIspis ispis = new ProfesoriIspis();
			ispis.setId(String.valueOf(p.getId()));
			ispis.setImePrezime(p.getIme());
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
			Collection<Predmet> predmeti = p.getPredmete();
			String novi ="";
			for (Predmet pred : predmeti) {
				novi = pred.getNaziv() + ", ";
			}
			ispis.setPredmeti(novi);
			ret.add(ispis);
		}
		return ret;
	}
}
