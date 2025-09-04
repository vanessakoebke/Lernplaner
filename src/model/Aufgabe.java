package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Aufgabe implements Serializable {
    private int id;
    private String titel;
    private String beschreibung;
    private LocalDate faelligkeit;
    private Status status;
    private static int nextId = 1;

    public Aufgabe(String titel, String beschreibung, LocalDate faelligkeit) {
        this.id = nextId++;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.faelligkeit = faelligkeit;
        status = Status.NEU;
    }

    public Aufgabe(String titel, String beschreibung) {
        this(titel, beschreibung, null);
    }

    public Aufgabe(String titel, LocalDate faelligkeit) {
        this(titel, null, faelligkeit);
    }
    

    
    public void update(Aufgabe neu) {
        this.titel = neu.titel;
        this.beschreibung = neu.beschreibung;
        this.faelligkeit = neu.faelligkeit;
        this.status = neu.status;
    }

    public Aufgabe(String titel) {
        this(titel, null, null);
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

    public String getStatus() {
        return status.toString();
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