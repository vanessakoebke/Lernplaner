package ui;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.Modul;
import util.CalendarFormatter;

public class ModulPanel extends JPanel implements IAnsicht {
    private JTextField nameFeld;
    private JDatePickerImpl klausurFeld;
    private JCheckBox abgeschlossenFeld;
    private JTextField noteFeld;

    public ModulPanel(Modul modul) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        // Modulname
        JLabel nameLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // links oben
        this.add(nameLabel, gbc);
        this.nameFeld = new JTextField(modul.getName() == null ? "" : modul.getName());
        this.nameFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        this.nameFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.add(this.nameFeld, gbc);
        // Klausurtermin
        JLabel klausurLabel = new JLabel(I18n.t("ui.Modulverwaltung.Klausurtermin"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START; // links oben
        this.add(klausurLabel, gbc);
        // Klausurtermin mit Datepicker
        UtilDateModel model = new UtilDateModel();
        if (modul.getKlausurTermin() != null) {
            LocalDate klausurTermin = modul.getKlausurTermin();

            // LocalDate konvertieren
            model.setDate(
                klausurTermin.getYear(),
                klausurTermin.getMonthValue() - 1, // Achtung: Monate im UtilDateModel sind 0-basiert (Jan = 0)!
                klausurTermin.getDayOfMonth()
            );
            model.setSelected(true);
        } else {
            model.setSelected(false); // nichts vorausgewählt
        }
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        this.klausurFeld = new JDatePickerImpl(datePanel, new CalendarFormatter());
        gbc.gridx = 1;
        gbc.weightx = 1;
        this.add(this.klausurFeld, gbc); // TODO Icon für Kalender setzen
        // Abgeschlossenes Modul
        JLabel aktuellLabel = new JLabel(I18n.t("ui.Modulverwaltung.Abgeschlossen"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(aktuellLabel, gbc);
        this.abgeschlossenFeld = new JCheckBox();
        abgeschlossenFeld.setSelected(!modul.getAktuell());
        gbc.gridx = 1;
        this.add(abgeschlossenFeld, gbc);
        // Note
        JLabel noteLabel = new JLabel(I18n.t("ui.Modulverwaltung.Note"));
        gbc.gridx = 0;
        gbc.gridy = 5;
        noteLabel.setVisible(false);
        this.add(noteLabel, gbc);
        this.noteFeld = modul.getNote() == null ? new JTextField("") : new JTextField(String.valueOf(modul.getNote()));
        this.noteFeld.setPreferredSize(new Dimension(200, 25)); // 1 Zeile hoch
        this.noteFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        this.noteFeld.setVisible(false);
        gbc.gridx = 1;
        this.add(noteFeld, gbc);
        // TODO machen, dass es beim sichtbar schalten nicht springt
        //TODO machen, dass es beim öffnen bereits richtig gesetzt ist
        abgeschlossenFeld.addActionListener(e -> {
            noteLabel.setVisible(abgeschlossenFeld.isSelected());
            noteFeld.setVisible(abgeschlossenFeld.isSelected());
            this.revalidate();
            this.repaint();
        });
    }

    public Modul getModul() {
        Date selectedDate = (Date) klausurFeld.getModel().getValue();
        LocalDate klausurDatum = null;
        if (selectedDate != null) {
            klausurDatum = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return new Modul(nameFeld.getText(), klausurDatum, !abgeschlossenFeld.isSelected(), noteFeld.getText());
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
    }
}
