package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.*;
import util.CalendarFormatter;

public class EingabePanel extends JPanel implements IAnsicht{
    private Aufgabe aufgabe;
    private JComboBox<Modul> modulFeld;
    private JTextField titelFeld;
    private JTextArea beschreibungFeld;
    private JDatePickerImpl startFeld;
    private JDatePickerImpl endeFeld;
    private JComboBox<Status> statusFeld;
    private DateTimeFormatter formatter;
    private Einstellungen einstellungen;

    public EingabePanel(Einstellungen einstellungen) {
        this(new Aufgabe("", ""), einstellungen);
    }

    public EingabePanel(String titel, String beschreibung, LocalDate ende, LocalDate start, int status, Modul modul, Einstellungen einstellungen) {
        this(new Aufgabe(titel, beschreibung, ende, start, status, modul), einstellungen);
    }
    

    public EingabePanel(Aufgabe aufgabe, Einstellungen einstellungen) {
        this.aufgabe = aufgabe;
        this.einstellungen = einstellungen;
        formatter = DateTimeFormatter.ofPattern(I18n.t("Common.Datumsformat_Java"));
        
        // Panel für das Formular
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        //Label und Textfeld für das Modul
        JLabel modulLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
        this.modulFeld = new JComboBox<Modul>(einstellungen.getModulManager().getAktuelleModule().toArray(new Modul[0]));
        if (aufgabe.getModul() != null) {
            modulFeld.setSelectedItem(aufgabe.getModul());
        } else {
            modulFeld.setSelectedItem(null);
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(modulLabel, gbc);
        gbc.gridx = 1;
        this.add(modulFeld, gbc);
        
        // Label und Textfeld für Titel
        JLabel titelLabel = new JLabel(I18n.t("ui.Common.Aufgabenname"));
        titelFeld = new JTextField(aufgabe.getTitel());
        titelFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(titelLabel, gbc);
        gbc.gridx = 1;
        this.add(titelFeld, gbc);
        
        // Label und Textbereich für Beschreibung
        JLabel beschreibungLabel = new JLabel(I18n.t("ui.Common.Beschreibung"));
        beschreibungFeld = new JTextArea(aufgabe.getBeschreibung(), 4, 20); // 4 Zeilen hoch
        beschreibungFeld.setLineWrap(true);
        beschreibungFeld.setWrapStyleWord(true);
        JScrollPane beschreibungScroll = new JScrollPane(beschreibungFeld, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(beschreibungLabel, gbc);
        gbc.gridx = 1;
        this.add(beschreibungScroll, gbc);
        
        //Label und JDatePicker für Starttermin
        JLabel startLabel = new JLabel(I18n.t("ui.Common.Start"));
        UtilDateModel startModel = new UtilDateModel();
        if (aufgabe.getStart() != null) {
            LocalDate start = aufgabe.getStart();

            // LocalDate konvertieren
            startModel.setDate(
                start.getYear(),
                start.getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                start.getDayOfMonth()
            );
            startModel.setSelected(true);
        } else if (titelFeld.getText() == ""){
            startModel.setDate(
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                    LocalDate.now().getDayOfMonth()
                );
            startModel.setSelected(true); // Starttermin heute vorausgewählt bei neuen Aufgaben
        } else {
            startModel.setSelected(false); //Starttermin leer bei bestehenden Aufgaben ohne Starttermin
        }
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, p);
        this.startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 12;
        this.add(startLabel, gbc);
        gbc.gridx = 1;
        this.add(startFeld, gbc);
        
        // Label und Textfeld für Endtermin
        JLabel endeLabel = new JLabel(I18n.t("ui.Common.Faelligkeit"));
        UtilDateModel endeModel = new UtilDateModel();
        if (aufgabe.getEnde() != null) {
            LocalDate ende = aufgabe.getEnde();

            // LocalDate konvertieren
            endeModel.setDate(
                ende.getYear(),
                ende.getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                ende.getDayOfMonth()
            );
            endeModel.setSelected(true);
        } else {
            startModel.setSelected(false); //Endtermin leer bei bestehenden Aufgaben ohne Endtermin
        }
        JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, p);
        this.endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 15;
        this.add(endeLabel, gbc);
        gbc.gridx = 1;
        this.add(endeFeld, gbc);
        
        // Label und Textfeld für Status-Dropdown
        JLabel statusLabel = new JLabel(I18n.t("ui.Common.Status"));
        statusFeld = new JComboBox<Status>(Status.values());
        statusFeld.setSelectedItem(aufgabe.getStatus());
        gbc.gridx = 0;
        gbc.gridy = 20;
        this.add(statusLabel, gbc);
        gbc.gridx = 1;
        this.add(statusFeld, gbc);
    }

    public Aufgabe getAufgabe() {
        Date selectedStartDate = (Date) startFeld.getModel().getValue();
        LocalDate start = null;
        if (selectedStartDate != null) {
            start = selectedStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        Date selectedEndDate = (Date) endeFeld.getModel().getValue();
        LocalDate ende = null;
        if (selectedEndDate != null) {
            ende = selectedEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return new Aufgabe(titelFeld.getText(), beschreibungFeld.getText(), ende, start, statusFeld.getSelectedIndex(), (Modul) modulFeld.getSelectedItem());
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
        
    }
}
