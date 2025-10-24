package model;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import lang.I18n;

public class Lerngruppe {
    private Integer id;
    private Modul modul;
    private String titel;
    private DayOfWeek wochentag;
    private LocalTime uhrzeit;
    private LocalDate ende;
    private List<Termin> termine = new ArrayList<>();
    private List<Aufgabe> aufgaben = new ArrayList<>();

    public Lerngruppe(Modul modul, String titel, DayOfWeek wochentag, LocalTime uhrzeit, LocalDate ende) {
        this.modul = modul;
        this.titel = titel;
        this.wochentag = wochentag;
        this.uhrzeit = uhrzeit;
        this.ende = ende;
        berechneTermine();
    }
    
    private void entferneZukuenftige() {
        for (Termin t: termine) {
            if (t.getDatum().isAfter(LocalDate.now())) {
                termine.remove(t);
            }
        }
    }

    private void berechneTermine() {
        entferneZukuenftige();
        LocalDate start = LocalDate.now();
        LocalDate datumLG = LocalDate.now().with(TemporalAdjusters.nextOrSame(wochentag));
        while (!datumLG.isAfter(ende)) {
            termine.add(new Termin(datumLG, new AufgabeLerngruppe(I18n.t("model.Aufgabentyp.LG"), "", datumLG, start,
                    Status.NEU, modul, null)));
            start = datumLG;
            datumLG = datumLG.plusDays(7);
        }
    }
    
    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public List<Termin> getTermine() {
        return termine;
    }

    public void setTermine(List<Termin> termine) {
        this.termine = termine;
    }

    public List<Aufgabe> getAufgaben() {
        return aufgaben;
    }

    public void setAufgaben(List<Aufgabe> aufgaben) {
        this.aufgaben = aufgaben;
    }

    public DayOfWeek getWochentag() {
        return wochentag;
    }
    
    public void setEnde(LocalDate ende) {
        this.ende = ende;
    }
    
    public LocalDate getEnde() {
        return ende;
    }

    public void setWochentag(DayOfWeek wochentag) {
        this.wochentag = wochentag;
        berechneTermine();
    }

    public LocalTime getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(LocalTime uhrzeit) {
        this.uhrzeit = uhrzeit;
        berechneTermine();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Termin getNaechstenTermin() {
        
        return termine.stream()
                .filter(t -> t.getDatum() != null && !t.getDatum().isBefore(LocalDate.now())) // nur Termine ab heute
                .min(Comparator.comparing(Termin::getDatum)) // der kleinste (früheste) Termin
                .orElse(null); // wenn kein zukünftiger Termin existiert
    }
    
    public class Termin {
        private LocalDate datum;
        private LocalTime uhrzeitTermin = uhrzeit;
        private AufgabeLerngruppe aufgabe;

        public Termin(LocalDate datum, AufgabeLerngruppe aufgabe) {
            this.datum = datum;
            this.aufgabe = aufgabe;
        }

        public LocalDate getDatum() {
            return datum;
        }

        public void setDatum(LocalDate datum) {
            this.datum = datum;
        }

        public AufgabeLerngruppe getAufgabe() {
            return aufgabe;
        }

        public void setAufgabe(AufgabeLerngruppe aufgabe) {
            this.aufgabe = aufgabe;
        }

        public void setUhrzeitTermin(LocalTime uhrzeitTermin) {
            this.uhrzeitTermin = uhrzeitTermin;
        }
        
        public LocalTime getUhrzeitTermin () {
            return uhrzeitTermin;
        }
    }


}
