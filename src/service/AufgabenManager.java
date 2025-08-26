package service;
import java.util.*;
import model.*;

public class AufgabenManager {
    private List<Aufgabe> aufgabenListe;
    public AufgabenManager() {
        aufgabenListe = new ArrayList<Aufgabe>();
    }
    
    public void addAufgabe(Aufgabe aufgabe) {
        aufgabenListe.add(aufgabe);
    }
    
    public void deleteAufgabe(int id) {
        aufgabenListe.removeIf(a -> a.getId() == id);
    }

    public List<Aufgabe> getAufgabenListe() {
        return aufgabenListe;
    }
    
}
