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
    private LocalDate faelligkeit;
    private Status status;
    private static int nextId = 1;
    private long tageBisFaellig;
    private List<Aufgabe> teilaufgaben = new ArrayList<Aufgabe>();

    public Aufgabe(String titel, String beschreibung, LocalDate faelligkeit) {
        this(titel, beschreibung, faelligkeit, 0);
    }

    public Aufgabe(String titel, String beschreibung) {
        this(titel, beschreibung, null);
    }

    public Aufgabe(String titel, LocalDate faelligkeit) {
        this(titel, null, faelligkeit);
    }
    
    public Aufgabe(String titel, String beschreibung, LocalDate faelligkeit, int status) {
        this.id = nextId++;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.faelligkeit = faelligkeit;
        this.status = Status.fromInt(status);
        this.tageBisFaellig =  faelligkeit == null ? 200 : ChronoUnit.DAYS.between(LocalDate.now(), faelligkeit);
    }
    

    public Aufgabe(String titel) {
        this(titel, null, null);
    }

    public void update(Aufgabe neu) {
        this.titel = neu.titel;
        this.beschreibung = neu.beschreibung;
        this.faelligkeit = neu.faelligkeit;
        this.status = neu.status;
    }
    
    public void updateTageBisFaellig() {
        this.tageBisFaellig =  ChronoUnit.DAYS.between(LocalDate.now(), faelligkeit);
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
        return faelligkeit;
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
        this.faelligkeit = faelligkeit;
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