package model;

import java.time.LocalDate;

public class Modul {
    private Integer id; 
    private String name;
    private LocalDate klausurTermin;
    private boolean aktuell;
    private String note;
    private int tageWiederholen = 30;

    public Modul(String name, LocalDate klausurTermin, boolean aktuell, String note) {
        this.id = null;
        this.name = name;
        this.klausurTermin = klausurTermin;
        this.aktuell = aktuell;
        this.note = note;
    }

    public Modul(String name) {
        this(name, null, true, null);
    }
    
    public Integer getId() { 
        return id; 
        }
    
    public void setId(Integer id) { 
        this.id = id; 
        }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public int getTageWiederholen() {
        return tageWiederholen;
    }
    
    public void setTageWiederholen(int tageWiederholen) {
        this.tageWiederholen = tageWiederholen;
    }
    
    public LocalDate getKlausurTermin() {
        return klausurTermin;
    }
    
    public boolean getAktuell() {
        return aktuell;
    }
    
    public void setAktuell(boolean b) {
        aktuell = b;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setKlausurTermin(LocalDate termin) {
        klausurTermin = termin;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note= note;
    }
    
    
}
