package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class Aufgabe {
    private Integer id;
    private String titel;
    private String beschreibung;
    private LocalDate start;
    private LocalDate ende;
    private Status status;
    private long tageBisFaellig;
    private Modul modul;
    private List<Aufgabe> teilaufgaben = new ArrayList<Aufgabe>();
    
    public Aufgabe(String titel, String beschreibung, LocalDate ende, LocalDate start, Status status, Modul modul) {
        this.id = null;
        this.titel = titel.toString();
        this.beschreibung = beschreibung;
        this.ende = ende;
        this.start = start;
        this.status = status;
        this.tageBisFaellig =  ende == null ? 200 : ChronoUnit.DAYS.between(LocalDate.now(), ende);
        this.modul = modul;
    }
    
    public void update(Aufgabe neu) {
        this.titel = neu.titel;
        this.modul = neu.modul;
        this.beschreibung = neu.beschreibung;
        this.start = neu.start;
        this.ende = neu.ende;
        this.status = neu.status;
        this.teilaufgaben = neu.teilaufgaben;
    }
    
    public LocalDate getStart() {
        return start;
    }
    
    public void setStart(LocalDate start) {
        this.start = start;
    }
    
    public Modul getModul() {
        return modul;
    }
    
    public void setModul(Modul modul) {
        this.modul = modul;
    }
    
    public void updateTageBisFaellig() {
        this.start = LocalDate.now(); //TODO prüfen, ob das sinnvoll ist
        this.tageBisFaellig =  ChronoUnit.DAYS.between(LocalDate.now(), ende);
    }
    
    public void addTeilaufgabe(Aufgabe teilaufgabe) {
        teilaufgaben.add(teilaufgabe);
    }
    
    public List<Aufgabe> getTeilaufgaben(){
        return teilaufgaben;
    }
    
    public long getTageBisFaellig() {
        return tageBisFaellig;
    }
    
    public String getTitel() {
        return titel.toString();
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public LocalDate getEnde() {
        return ende;
    }
    
    public Status getStatus() {
        return status;
    }

    public String getStatusString() {
        return status.toString();
    }
    
    public int getStatusIndex() {
        return status.toInt();
    }

    public boolean isErledigt() {
        return status == Status.ERLEDIGT;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setEnde(LocalDate ende) {
        this.ende = ende;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public abstract Aufgabentyp getTyp();
}
