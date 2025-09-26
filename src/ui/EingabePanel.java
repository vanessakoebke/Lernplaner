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
import service.Control;
import util.CalendarFormatter;

public class EingabePanel extends JPanel implements IAnsicht {

    private boolean neueingabe = false;
    private Aufgabe aufgabe;
    private JComboBox<Modul> modulFeld;
    private JComboBox<Aufgabentyp> aufgabentypFeld;
    private JTextField titelFeld;
    private JTextArea beschreibungFeld;
    private JDatePickerImpl startFeld;
    private JDatePickerImpl endeFeld;
    private JComboBox<Status> statusFeld;
    private JTextField seitenDurcharbeitenFeld;
    private JTextField ergebnisFeld;
    private JTextField einheitenFeld;
    private JComboBox<Lerneinheit> einheitenDropdown;

    private Control control;

    // Konstruktor für neue Aufgabe
    public EingabePanel(Control control) {
        this.control = control;
        this.aufgabe = null;
        this.neueingabe = true;
        initUI();
    }

    // Konstruktor zum Bearbeiten bestehender Aufgabe
    public EingabePanel(Aufgabe aufgabe, Control control) {
        this.control = control;
        this.aufgabe = aufgabe;
        this.neueingabe = false;
        initUI();
        setAufgabe(aufgabe);
    }

    // GUI-Aufbau
    private void initUI() {
        DateTimeFormatter formatter = control.getEinstellungen().getDatumsformat();
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // Modul
        JLabel modulLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
        modulFeld = new JComboBox<>(control.getMm().getAktuelleModule().toArray(new Modul[0]));
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(modulLabel, gbc);
        gbc.gridx = 1;
        this.add(modulFeld, gbc);

        // Aufgabentyp
        JLabel aufgabentypLabel = new JLabel(I18n.t("model.Aufgabentyp.Typ"));
        aufgabentypFeld = new JComboBox<>(Aufgabentyp.values());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(aufgabentypLabel, gbc);
        gbc.gridx = 1;
        this.add(aufgabentypFeld, gbc);
        if(!neueingabe) {
            aufgabentypFeld.setEnabled(false);
        }

        // Titel
        JLabel titelLabel = new JLabel(I18n.t("ui.Common.Aufgabenname"));
        titelFeld = new JTextField();
        titelFeld.setPreferredSize(new Dimension(200, 25));
        titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(titelLabel, gbc);
        gbc.gridx = 1;
        this.add(titelFeld, gbc);

        // Beschreibung
        JLabel beschreibungLabel = new JLabel(I18n.t("ui.Common.Beschreibung"));
        beschreibungFeld = new JTextArea(4, 20);
        beschreibungFeld.setLineWrap(true);
        beschreibungFeld.setWrapStyleWord(true);
        JScrollPane beschreibungScroll = new JScrollPane(beschreibungFeld, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.gridx = 0;
        gbc.gridy = 10;
        this.add(beschreibungLabel, gbc);
        gbc.gridx = 1;
        this.add(beschreibungScroll, gbc);

        // Startdatum
        JLabel startLabel = new JLabel(I18n.t("ui.Common.Start"));
        UtilDateModel startModel = new UtilDateModel();
        JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, getCalendarProperties());
        startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 12;
        this.add(startLabel, gbc);
        gbc.gridx = 1;
        this.add(startFeld, gbc);

        // Enddatum
        JLabel endeLabel = new JLabel(I18n.t("ui.Common.Faelligkeit"));
        UtilDateModel endeModel = new UtilDateModel();
        JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, getCalendarProperties());
        endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
        gbc.gridx = 0;
        gbc.gridy = 15;
        this.add(endeLabel, gbc);
        gbc.gridx = 1;
        this.add(endeFeld, gbc);

        // Status
        JLabel statusLabel = new JLabel(I18n.t("ui.Common.Status"));
        statusFeld = new JComboBox<>(Status.values());
        gbc.gridx = 0;
        gbc.gridy = 20;
        this.add(statusLabel, gbc);
        gbc.gridx = 1;
        this.add(statusFeld, gbc);

        
        // Seiten für DURCHARBEITEN
        JLabel seitenLabel = new JLabel(I18n.t("model.Lerneinheiten.Seiten"));
        seitenDurcharbeitenFeld = new JTextField();
        seitenDurcharbeitenFeld.setPreferredSize(new Dimension(200, 25));
        seitenDurcharbeitenFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 22;
        this.add(seitenLabel, gbc);
        gbc.gridx = 1;
        this.add(seitenDurcharbeitenFeld, gbc);
        if (!(aufgabe instanceof AufgabeDurcharbeiten) ) {
            seitenLabel.setVisible(false);
            seitenDurcharbeitenFeld.setVisible(false);
        }

        aufgabentypFeld.addActionListener(e -> {
            Aufgabentyp typ = (Aufgabentyp) aufgabentypFeld.getSelectedItem();
            boolean sichtbar = typ == Aufgabentyp.DURCHARBEITEN;
            seitenLabel.setVisible(sichtbar);
            seitenDurcharbeitenFeld.setVisible(sichtbar);
        });
        
        
        // Einheiten für WIEDERHOLEN
        JLabel einheitenLabel = new JLabel(I18n.t("model.Lerneinheiten.Einheiten"));
        einheitenFeld = new JTextField(5);
        einheitenDropdown = new JComboBox<Lerneinheit>(Lerneinheit.values());
        if (aufgabe != null && aufgabe instanceof AufgabeWiederholen) {
            einheitenDropdown.setSelectedItem(((AufgabeWiederholen) aufgabe).getEinheitstyp());
        }
        JPanel einheitenPanel = new JPanel();
        einheitenPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 5px Abstand, linksbündig
        einheitenPanel.add(einheitenFeld);
        einheitenPanel.add(einheitenDropdown);
        gbc.gridx = 0;
        gbc.gridy = 22;
        this.add(einheitenLabel, gbc);
        gbc.gridx = 1;
        this.add(einheitenPanel, gbc);

        if (!(aufgabe instanceof AufgabeWiederholen) ) {
            einheitenLabel.setVisible(false);
            einheitenFeld.setVisible(false);
            einheitenDropdown.setVisible(false);
        }

        aufgabentypFeld.addActionListener(e -> {
            Aufgabentyp typ = (Aufgabentyp) aufgabentypFeld.getSelectedItem();
            boolean sichtbar = typ == Aufgabentyp.WIEDERHOLEN;
            einheitenLabel.setVisible(sichtbar);
            einheitenFeld.setVisible(sichtbar);
            einheitenDropdown.setVisible(sichtbar);
        });

        
        // Ergebnis für EA und Altklausur
        JLabel ergebnisLabel = new JLabel(I18n.t("model.Ergebnis"));
        ergebnisFeld = new JTextField();
        ergebnisFeld.setPreferredSize(new Dimension(200, 25));
        ergebnisFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        gbc.gridx = 0;
        gbc.gridy = 22;
        this.add(ergebnisLabel, gbc);
        gbc.gridx = 1;
        this.add(ergebnisFeld, gbc);
        if (!(aufgabe instanceof AufgabeEA) && !(aufgabe instanceof AufgabeAltklausur)) {
            ergebnisLabel.setVisible(false);
            ergebnisFeld.setVisible(false);
        }
        
        
        aufgabentypFeld.addActionListener(e -> {
            Status status = (Status) statusFeld.getSelectedItem();
            Aufgabentyp typ = (Aufgabentyp) aufgabentypFeld.getSelectedItem();
            boolean sichtbar = (((typ == Aufgabentyp.EA) || (typ == Aufgabentyp.ALTKLAUSUR)) && (statusFeld.getSelectedItem() == Status.ERLEDIGT));
            ergebnisLabel.setVisible(sichtbar);
            ergebnisFeld.setVisible(sichtbar);
            if (typ == Aufgabentyp.EA) {
                titelFeld.setText(I18n.t("model.Aufgabentyp.EA_abr") + " ");
            } else if (typ == Aufgabentyp.ALTKLAUSUR){
                titelFeld.setText(I18n.t("model.Aufgabentyp.Altklausur") + " ");
            }else {
                String aktuell = titelFeld.getText();
                if (!aktuell.isEmpty() && aktuell.contains(I18n.t("model.Aufgabentyp.EA_abr") + " ")) {
                    int laenge = (I18n.t("model.Aufgabentyp.EA_abr") + " ").length();
                    String neu = aktuell.substring(laenge);
                    titelFeld.setText(neu);
                }
                if (!aktuell.isEmpty() && aktuell.contains(I18n.t("model.Aufgabentyp.Altklausur") + " ")) {
                    int laenge = (I18n.t("model.Aufgabentyp.Altklausur") + " ").length();
                    String neu = aktuell.substring(laenge);
                    titelFeld.setText(neu);
                }
            }
        });
        
        statusFeld.addActionListener(e -> {
            Status status = (Status) statusFeld.getSelectedItem();
            Aufgabentyp typ = (Aufgabentyp) aufgabentypFeld.getSelectedItem();
            boolean sichtbar = (((typ == Aufgabentyp.EA) || (typ == Aufgabentyp.ALTKLAUSUR)) && (status == Status.ERLEDIGT));
            ergebnisLabel.setVisible(sichtbar);
            ergebnisFeld.setVisible(sichtbar);
        });


    
    }

    private Properties getCalendarProperties() {
        Properties p = new Properties();
        p.put("text.today", I18n.t("ui.Kalender.Heute"));
        p.put("text.month", I18n.t("ui.Kalender.Monat"));
        p.put("text.year", I18n.t("ui.Kalender.Jahr"));
        return p;
    }

    public Aufgabe getAufgabe() {
        Date startDate = (Date) startFeld.getModel().getValue();
        LocalDate start = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        Date endDate = (Date) endeFeld.getModel().getValue();
        LocalDate ende = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        Aufgabentyp typ = (Aufgabentyp) aufgabentypFeld.getSelectedItem();

        if (neueingabe) {
            switch (typ) {
                case DURCHARBEITEN:
                    int seiten = 0;
                    try {
                        seiten = Integer.parseInt(seitenDurcharbeitenFeld.getText());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                I18n.t("Common.Errors.UngueltigeSeiten"),
                                I18n.t("Common.Errors.Warnung"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                    return new AufgabeDurcharbeiten(titelFeld.getText(), beschreibungFeld.getText(),
                            ende, start, (Status) statusFeld.getSelectedItem(), (Modul) modulFeld.getSelectedItem(), seiten);
                case EA:
                    return new AufgabeEA(titelFeld.getText(), beschreibungFeld.getText(),
                            ende, start, (Status) statusFeld.getSelectedItem(), (Modul) modulFeld.getSelectedItem(), ergebnisFeld.getText());
                case ALTKLAUSUR:
                    return new AufgabeAltklausur(titelFeld.getText(), beschreibungFeld.getText(),
                            ende, start, (Status) statusFeld.getSelectedItem(), (Modul) modulFeld.getSelectedItem(), ergebnisFeld.getText());
                case WIEDERHOLEN:
                    int einheiten = 0;
                    try {
                        einheiten = Integer.parseInt(einheitenFeld.getText());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                I18n.t("Common.Errors.UngueltigeSeiten"), //TODO warnung anpassen
                                I18n.t("Common.Errors.Warnung"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                    return new AufgabeWiederholen(titelFeld.getText(), beschreibungFeld.getText(),
                            ende, start, (Status) statusFeld.getSelectedItem(), (Modul) modulFeld.getSelectedItem(), einheiten, (Lerneinheit) einheitenDropdown.getSelectedItem());
                    
                default:
                    return new AufgabeAllgemein(titelFeld.getText(), beschreibungFeld.getText(),
                            ende, start, (Status) statusFeld.getSelectedItem(), (Modul) modulFeld.getSelectedItem());
            }
        } else {
            // bestehende Aufgabe updaten
            aufgabe.setTitel(titelFeld.getText());
            aufgabe.setBeschreibung(beschreibungFeld.getText());
            aufgabe.setStart(start);
            aufgabe.setEnde(ende);
            aufgabe.setStatus((Status) statusFeld.getSelectedItem());
            aufgabe.setModul((Modul) modulFeld.getSelectedItem());

            if (aufgabe instanceof AufgabeDurcharbeiten) {
                try {
                    ((AufgabeDurcharbeiten) aufgabe)
                            .setSeiten(Integer.parseInt(seitenDurcharbeitenFeld.getText()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            I18n.t("Common.Errors.UngueltigeSeiten"),
                            I18n.t("Common.Errors.Warnung"),
                            JOptionPane.WARNING_MESSAGE);
                }
            } else if (aufgabe instanceof AufgabeWiederholen) {
                try {
                    ((AufgabeWiederholen) aufgabe)
                            .setEinheiten(Integer.parseInt(einheitenFeld.getText()));
                    ((AufgabeWiederholen) aufgabe).setEinheitstyp((Lerneinheit) einheitenDropdown.getSelectedItem());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            I18n.t("Common.Errors.UngueltigeSeiten"), //TODO anpassen
                            I18n.t("Common.Errors.Warnung"),
                            JOptionPane.WARNING_MESSAGE);
                }
            } else if (aufgabe instanceof AufgabeEA) {
                ((AufgabeEA) aufgabe).setErgebnis(ergebnisFeld.getText());
            } else if (aufgabe instanceof AufgabeAltklausur) {
                ((AufgabeAltklausur) aufgabe).setErgebnis(ergebnisFeld.getText());
            }
            return aufgabe;
        }
    }

    private void setAufgabe(Aufgabe aufgabe) {
        this.aufgabe = aufgabe;
        this.neueingabe = false;

        modulFeld.setSelectedItem(aufgabe.getModul());
        aufgabentypFeld.setSelectedItem(aufgabe.getTyp());
        titelFeld.setText(aufgabe.getTitel());
        beschreibungFeld.setText(aufgabe.getBeschreibung());

        if (aufgabe.getStart() != null) {
            ((UtilDateModel) startFeld.getModel()).setValue(
                Date.from(aufgabe.getStart().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            ((UtilDateModel) startFeld.getModel()).setSelected(true);
        }

        if (aufgabe.getEnde() != null) {
            ((UtilDateModel) endeFeld.getModel()).setValue(
                Date.from(aufgabe.getEnde().atStartOfDay(ZoneId.systemDefault()).toInstant())
            );
            ((UtilDateModel) endeFeld.getModel()).setSelected(true);
        }


        statusFeld.setSelectedItem(aufgabe.getStatus());

        if (aufgabe instanceof AufgabeDurcharbeiten) {
            seitenDurcharbeitenFeld.setText(
                    String.valueOf(((AufgabeDurcharbeiten) aufgabe).getSeiten())
            );
            seitenDurcharbeitenFeld.setVisible(true);
        }
        
        if (aufgabe instanceof AufgabeEA) {
            ergebnisFeld.setText(((AufgabeEA) aufgabe).getErgebnis());
        }
        
        if (aufgabe instanceof AufgabeAltklausur) {
            ergebnisFeld.setText(((AufgabeAltklausur) aufgabe).getErgebnis());
        }
        
        if (aufgabe instanceof AufgabeWiederholen) {
            einheitenFeld.setText(String.valueOf(((AufgabeWiederholen) aufgabe).getEinheiten()));
            einheitenDropdown.setSelectedItem(((AufgabeWiederholen) aufgabe).getEinheitstyp());
        }
    }

    @Override
    public void refresh() {
        // kann bei Bedarf implementiert werden
    }
}
