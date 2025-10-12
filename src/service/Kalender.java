package service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Properties;

import org.jdatepicker.impl.UtilDateModel;

import lang.I18n;

public class Kalender {

    
    public static Properties getCalendarProperties() {
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        return p;
    }
    


    /**
     * Erzeugt ein UtilDateModel mit einem voreingestellten Datum basierend auf
     * dem aktuellen Semester und einem Offset in Tagen.
     * * @param offsetTage Die Anzahl der Tage, die zum Semesterstart addiert werden.
     * @return Ein vorkonfiguriertes UtilDateModel.
     */
    public static UtilDateModel getModellMitSemesterdatum(int offsetTage) {
        Calendar heute = Calendar.getInstance();
        int aktuellerMonat = heute.get(Calendar.MONTH);
        int aktuellesJahr = heute.get(Calendar.YEAR);
        
        // Annahme:
        // Wintersemester (WS) beginnt am 1. Oktober (Monat 9) und endet am 31. März (Monat 2)
        // Sommersemester (SS) beginnt am 1. April (Monat 3) und endet am 30. September (Monat 8)
        
        int startMonat = getStartMonat(aktuellerMonat); // 0-basiert
        int startJahr = aktuellesJahr;


        // --- Zieldatum berechnen (Semesterstart + Offset) ---
        Calendar zieldatum = Calendar.getInstance();
        zieldatum.set(startJahr, startMonat, 1); // Setze auf den 1. des Startmonats
        
        // Addiere den Offset in Tagen
        zieldatum.add(Calendar.DAY_OF_MONTH, offsetTage);
        
        // --- Modell initialisieren und Datum setzen ---
        UtilDateModel model = new UtilDateModel();
        model.setDate(
            zieldatum.get(Calendar.YEAR),
            zieldatum.get(Calendar.MONTH),
            zieldatum.get(Calendar.DAY_OF_MONTH)
        );
        
        // Zeigt das Datum im Textfeld an
        model.setSelected(true);
        
        return model;
    }
    
    public static int getTageDurcharbeiten(int einheiten) {
        LocalDate heute = LocalDate.now();
        LocalDate startDatum =  LocalDate.of(heute.getYear(), getStartMonat(heute.getMonthValue()-1), 1);
        LocalDate endDatum = startDatum.plusDays(139);
        return (int )ChronoUnit.DAYS.between(heute, endDatum) /  einheiten;
    }
    
    private static int getStartMonat(int aktuellerMonat) {
        int startMonat;
        // Wenn der aktuelle Monat zwischen April (3) und September (8) liegt, ist SS aktiv.
        if (aktuellerMonat >= Calendar.MARCH && aktuellerMonat <= Calendar.AUGUST) {
            startMonat = Calendar.APRIL;
            // Startjahr bleibt aktuelles Jahr (z.B. SS 2025 beginnt 01.04.2025)
        } 
        // Wenn der aktuelle Monat zwischen Oktober (9) und März (2) liegt, ist WS aktiv.
        else {
            startMonat = Calendar.OCTOBER;

        }
        return startMonat;
    }
}
