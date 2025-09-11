package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class Aufgabe implements Serializable {
    private int id;
    private String titel;
    private String beschreibung;
    private LocalDate start;
    private LocalDate ende;
    private Status status;
    private static int nextId = 1;
    private long tageBisFaellig;
    private Modul modul;
    private List<Aufgabe> teilaufgaben = new ArrayList<Aufgabe>();

    public Aufgabe(String titel, String beschreibung, LocalDate ende) {
        this(titel, beschreibung, ende, LocalDate.now(),  0, null);
    }

    public Aufgabe(String titel, String beschreibung) {
        this(titel, beschreibung, null);
    }

    public Aufgabe(String titel, LocalDate ende) {
        this(titel, null, ende);
    }
    
    public Aufgabe(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, Modul modul) {
        this.id = nextId++;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.ende = ende;
        this.status = Status.fromInt(status);
        this.tageBisFaellig =  ende == null ? 200 : ChronoUnit.DAYS.between(LocalDate.now(), ende);
        this.modul = modul;
    }

    public Aufgabe(String titel) {
        this(titel, null, null);
    }

    public void update(Aufgabe neu) {
        this.titel = neu.titel;
        this.beschreibung = neu.beschreibung;
        this.ende = neu.ende;
        this.status = neu.status;
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
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public LocalDate getFaelligkeit() {
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

    public void setFaelligkeit(LocalDate faelligkeit) {
        this.ende = faelligkeit;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getId() {
        return id;
    }

}