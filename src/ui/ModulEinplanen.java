package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.*;
import service.Control;
import service.Kalender;
import util.CalendarFormatter;

public class ModulEinplanen extends JFrame {
    Modul modul;
    Control control;
    Hauptfenster hf;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton buttonZurueck;
    private JButton buttonWeiter;
    private int aktuellerSchritt = 0;
    private Schritt1WelcheAufgaben schritt1;
    private java.util.List<Aufgabe> aufgabenListe = new ArrayList<>();
    private final Map<String, JPanel> alleSchritte = new LinkedHashMap<>();
    private final java.util.List<String> sichtbareSchritte = new ArrayList<>();
    Aufgabe vorherige = null;

    public ModulEinplanen(Control control, Hauptfenster hf) {
        super(I18n.t("ui.Main.ButtonModulEinplanen"));
        this.hf = hf;
        this.control = control;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        // Schritt1 Panel
        schritt1 = new Schritt1WelcheAufgaben();
        cardPanel.add(schritt1, "Schritt 1");
        ;
        // Weitere mögliche Schritte
        alleSchritte.put("ALLGEMEIN", new SchrittAllgemein());
        alleSchritte.put("DURCHARBEITEN", new SchrittDurcharbeiten());
        alleSchritte.put("EA", new SchrittEA());
        alleSchritte.put("WIEDERHOLEN", new SchrittWiederholen());
        alleSchritte.put("ALTKLAUSUR", new SchrittAltklausur());
        // Buttons
        buttonZurueck = new JButton("← Zurück");
        buttonWeiter = new JButton("Weiter →");
        buttonZurueck.addActionListener(e -> geheZuSchritt(aktuellerSchritt - 1));
        buttonWeiter.addActionListener(e -> {
            if (aktuellerSchritt == 0) {
                // Ersten Schritt fertig – erst die sichtbaren Schritte vorbereiten
                vorbereiteSchritte();
            } else if (aktuellerSchritt == sichtbareSchritte.size()) {
                dispose();
            } else {
                geheZuSchritt(aktuellerSchritt + 1);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonZurueck);
        buttonPanel.add(buttonWeiter);
        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        updateButtons();
        setVisible(true);
    }

    private void vorbereiteSchritte() {
        // 1️⃣ Modul-Auswahl prüfen
        if (modul == null) {
            JOptionPane.showMessageDialog(this, "Bitte wähle zuerst ein Modul aus, bevor du fortfährst.",
                    "Kein Modul ausgewählt", JOptionPane.WARNING_MESSAGE);
            return;
        }
        sichtbareSchritte.clear();
        sichtbareSchritte.addAll(schritt1.gewählteTypen());
        if (sichtbareSchritte.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte mindestens einen Aufgabentyp auswählen!");
            return;
        }
        // Füge die ausgewählten Panels hinzu (nur wenn noch nicht vorhanden)
        for (String typ : sichtbareSchritte) {
            if (cardPanel.getComponentZOrder(alleSchritte.get(typ)) == -1) {
                cardPanel.add(alleSchritte.get(typ), typ);
            }
        }
        // Zeige den ersten ausgewählten Schritt
        aktuellerSchritt = 1; // Schritt 0 = Auswahl, nächster Schritt = erstes Panel
        cardLayout.show(cardPanel, sichtbareSchritte.get(0));
        updateButtons();
    }

    private void geheZuSchritt(int neuerSchritt) {
        if (neuerSchritt < 0 || neuerSchritt > sichtbareSchritte.size()) return;
        aktuellerSchritt = neuerSchritt;
        if (aktuellerSchritt == 0) {
            cardLayout.show(cardPanel, "Schritt1");
        } else {
            cardLayout.show(cardPanel, sichtbareSchritte.get(aktuellerSchritt - 1));
        }
        updateButtons();
    }

    private void updateButtons() {
        for (ActionListener al : buttonWeiter.getActionListeners()) {
            buttonWeiter.removeActionListener(al);
        }
        buttonZurueck.setEnabled(aktuellerSchritt > 0);
        // Schritt1 immer "Weiter →"
        if (aktuellerSchritt == 0) {
            buttonWeiter.setText("Weiter →");
            buttonWeiter.setEnabled(true);
            buttonWeiter.addActionListener(e -> vorbereiteSchritte());
        }
        // letzte sichtbare Schritte = "Fertig"
        else if (aktuellerSchritt >= sichtbareSchritte.size()) {
            buttonWeiter.setText("Fertig");
            buttonWeiter.setEnabled(true);
            buttonWeiter.addActionListener(x -> {
                // 1. Holen Sie den String-Namen des aktuellen Schritts
                String schrittName = sichtbareSchritte.get(aktuellerSchritt - 1);
                // ACHTUNG: Der Index des aktuellen Schritts im Array ist aktuellerSchritt - 1
                // da aktuellerSchritt 1-basiert ist (1 bis sichtbareSchritte.size())
                // 2. Holen Sie das tatsächliche JPanel-Objekt aus der Map
                JPanel aktuellesPanel = alleSchritte.get(schrittName);
                // 3. Prüfen Sie den Typ des JPanel-Objekts
                if (aktuellesPanel instanceof AufgabenSchritte) {
                    ((AufgabenSchritte) aktuellesPanel).addAufgaben();
                }
                for (Aufgabe a : aufgabenListe) {
                    control.getAm().addAufgabe(a);
                    control.getDb().upsertAufgabe(a);
                }
                hf.refresh();
                dispose();
            });
        }
        // normale Zwischen-Schritte
        else {
            buttonWeiter.setText("Weiter →");
            buttonWeiter.setEnabled(true);
            buttonWeiter.addActionListener(x -> {
                // 1. Holen Sie den String-Namen des aktuellen Schritts
                String schrittName = sichtbareSchritte.get(aktuellerSchritt - 1);
                // ACHTUNG: Der Index des aktuellen Schritts im Array ist aktuellerSchritt - 1
                // da aktuellerSchritt 1-basiert ist (1 bis sichtbareSchritte.size())
                // 2. Holen Sie das tatsächliche JPanel-Objekt aus der Map
                JPanel aktuellesPanel = alleSchritte.get(schrittName);
                // 3. Prüfen Sie den Typ des JPanel-Objekts
                if (aktuellesPanel instanceof AufgabenSchritte) {
                    ((AufgabenSchritte) aktuellesPanel).addAufgaben();
                }
                // 4. Gehe zu nächstem Schritt
                geheZuSchritt(aktuellerSchritt + 1);
            });
        }
    }

    // ----------------- Panels -----------------
    private abstract class AufgabenSchritte extends JPanel {
        protected List<JPanel> panelListe = new ArrayList<>();
        protected JScrollPane aufgabenPanel;

        protected abstract void addAufgaben();

        protected JScrollPane getAufgabenPanel(int anzahl, Lerneinheit einheitstyp) {
            // Das eigentliche Panel, das die Aufgaben enthält
            JPanel contentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            int offset = 0;
            panelListe = new ArrayList<>();
            for (int i = 1; i <= anzahl; i++) {
                JPanel aufgabeI = new JPanel(new BorderLayout(10, 0));
                // Titelfeld
                JTextField titelI = new JTextField(einheitstyp + " " + i);
                titelI.setPreferredSize(new Dimension(150, 25));
                aufgabeI.add(titelI, BorderLayout.WEST);
                JDatePickerImpl startFeld;
                JDatePickerImpl endeFeld;
                if (anzahl == 7) {
                    // Startdatum
                    UtilDateModel startModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
                    startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
                    aufgabeI.add(startFeld, BorderLayout.CENTER);
                    offset += 14;
                    // Enddatum
                    UtilDateModel endeModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
                    endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
                    aufgabeI.add(endeFeld, BorderLayout.EAST);
                } else {
                    int zeitProEinheit = Kalender.getTageDurcharbeiten(anzahl);
                    // Startdatum
                    UtilDateModel startModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
                    startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
                    offset += zeitProEinheit;
                    // Enddatum
                    UtilDateModel endeModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
                    endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
                }
                aufgabeI.add(startFeld, BorderLayout.CENTER);
                aufgabeI.add(endeFeld, BorderLayout.EAST);
                contentPanel.add(aufgabeI, gbc);
                gbc.gridy++;
                panelListe.add(aufgabeI);
            }
            // Jetzt das contentPanel in ein ScrollPane einbetten
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setPreferredSize(new Dimension(500, 350));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            return scrollPane;
        }

        protected JScrollPane getAufgabenPanel(int anzahl) {
            // Das eigentliche Panel, das die Aufgaben enthält
            JPanel contentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            int offset = 0;
            panelListe = new ArrayList<>();
            for (int i = 1; i <= anzahl; i++) {
                JPanel aufgabeI = new JPanel(new BorderLayout(10, 0));
                // Titelfeld
                JTextField titelI = new JTextField(I18n.t("model.Aufgabentyp.EA_abr") + " " + i);
                titelI.setPreferredSize(new Dimension(150, 25));
                aufgabeI.add(titelI, BorderLayout.WEST);
                JDatePickerImpl startFeld;
                JDatePickerImpl endeFeld;
                if (anzahl == 7) {
                    // Startdatum
                    UtilDateModel startModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
                    startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
                    aufgabeI.add(startFeld, BorderLayout.CENTER);
                    offset += 14;
                    // Enddatum
                    UtilDateModel endeModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
                    endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
                    aufgabeI.add(endeFeld, BorderLayout.EAST);
                } else {
                    int zeitProEinheit = Kalender.getTageDurcharbeiten(anzahl);
                    // Startdatum
                    UtilDateModel startModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
                    startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
                    offset += zeitProEinheit;
                    // Enddatum
                    UtilDateModel endeModel = Kalender.getModellMitSemesterdatum(offset);
                    JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
                    endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
                }
                aufgabeI.add(startFeld, BorderLayout.CENTER);
                aufgabeI.add(endeFeld, BorderLayout.EAST);
                contentPanel.add(aufgabeI, gbc);
                gbc.gridy++;
                panelListe.add(aufgabeI);
            }
            // Jetzt das contentPanel in ein ScrollPane einbetten
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setPreferredSize(new Dimension(500, 350));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            return scrollPane;
        }

        protected JScrollPane getAufgabenPanel(LocalDate start, LocalDate ende, int anzahl) {
            // Das eigentliche Panel, das die Aufgaben enthält
            JPanel contentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 10, 5, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            int offset = 0;
            panelListe = new ArrayList<>();
            for (int i = 1; i <= anzahl; i++) {
                JPanel aufgabeI = new JPanel(new BorderLayout(10, 0));
                // Titelfeld
                JTextField titelI = new JTextField();
                titelI.setPreferredSize(new Dimension(150, 25));
                aufgabeI.add(titelI, BorderLayout.WEST);
                // Startdatum
                UtilDateModel startModel = new UtilDateModel();
                startModel.setValue(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                startModel.setSelected(true);
                JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
                JDatePickerImpl startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
                // Enddatum
                UtilDateModel endeModel = new UtilDateModel();
                endeModel.setValue(Date.from(ende.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                endeModel.setSelected(true);
                JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
                JDatePickerImpl endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
                // Komponenten hinzufügen
                aufgabeI.add(startFeld, BorderLayout.CENTER);
                aufgabeI.add(endeFeld, BorderLayout.EAST);
                contentPanel.add(aufgabeI, gbc);
                gbc.gridy++;
                panelListe.add(aufgabeI);
            }
            // Jetzt das contentPanel in ein ScrollPane einbetten
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setPreferredSize(new Dimension(500, 350));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            return scrollPane;
        }
    }

    private class Schritt1WelcheAufgaben extends JPanel {
        JCheckBox cbDurcharbeiten = new JCheckBox(I18n.t("model.Aufgabentyp.Durcharbeiten"));
        JCheckBox cbEA = new JCheckBox(I18n.t("model.Aufgabentyp.EA"));
        JCheckBox cbWiederholen = new JCheckBox(I18n.t("model.Aufgabentyp.Wiederholen"));
        JCheckBox cbAltklausur = new JCheckBox(I18n.t("model.Aufgabentyp.Altklausur"));
        JCheckBox cbAllgemein = new JCheckBox(I18n.t("model.Aufgabentyp.Allgemein"));

        Schritt1WelcheAufgaben() {
            setLayout(new BorderLayout());
            // Modul-Wahl
            JPanel modulWahl = new JPanel(new GridLayout(0, 1, 5, 5));
            JLabel modulWaehlenLabel = new JLabel(I18n.t("ui.ModulEinplanen.ModulWaehlen"), SwingConstants.LEFT);
            modulWaehlenLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
            modulWahl.add(modulWaehlenLabel);
            Modul[] modulArray = control.getMm().getAktuelleModule().toArray(new Modul[0]);
            JComboBox<Modul> module = new JComboBox<>(modulArray);
            modul = (Modul) module.getSelectedItem();
            module.addActionListener(e -> modul = (Modul) module.getSelectedItem());
            modulWahl.add(module);
            add(modulWahl, BorderLayout.NORTH);
            JLabel labelWelcheAufgaben = new JLabel(I18n.t("ui.ModulEinplanen.WelcheAufgaben"), SwingConstants.LEFT);
            labelWelcheAufgaben.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
            // Aufgaben-Wahl
            JPanel aufgabenWahl = new JPanel(new GridLayout(0, 1, 5, 5));
            aufgabenWahl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            aufgabenWahl.add(labelWelcheAufgaben);
            JCheckBox[] checkboxes = { cbDurcharbeiten, cbEA, cbAltklausur };
            for (JCheckBox cb : checkboxes) {
                cb.setFocusPainted(false);
                aufgabenWahl.add(cb);
            }
            add(aufgabenWahl, BorderLayout.CENTER);
        }

        java.util.List<String> gewählteTypen() {
            java.util.List<String> result = new ArrayList<>();
            if (cbAllgemein.isSelected()) result.add("ALLGEMEIN");
            if (cbDurcharbeiten.isSelected()) result.add("DURCHARBEITEN");
            if (cbEA.isSelected()) result.add("EA");
            if (cbWiederholen.isSelected()) result.add("WIEDERHOLEN");
            if (cbAltklausur.isSelected()) result.add("ALTKLAUSUR");
            return result;
        }
    }

    private class SchrittAllgemein extends AufgabenSchritte {
        SchrittAllgemein() {
            setLayout(new BorderLayout());
            add(new JLabel("Allgemein Schritt", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        @Override
        protected void addAufgaben() {
            // TODO Auto-generated method stub
        }
    }

    private class SchrittDurcharbeiten extends AufgabenSchritte {
        SchrittDurcharbeiten() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);
            gbc.anchor = GridBagConstraints.WEST;
            // Zeile 1: Label
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(new JLabel(I18n.t("model.Lerneinheiten.Einheiten") + ":"), gbc);
            // Zeile 1: Textfeld
            gbc.gridx = 1;
            JTextField anzEinheiten = new JTextField(5);
            add(anzEinheiten, gbc);
            // Zeile 1: ComboBox
            gbc.gridx = 2;
            JComboBox<Lerneinheit> einheitstyp = new JComboBox<>();
            einheitstyp.addItem(Lerneinheit.LEKTION);
            einheitstyp.addItem(Lerneinheit.KAPITEL);
            add(einheitstyp, gbc);
            // Zeile 1: Button
            gbc.gridx = 3;
            JButton erzeuge = new JButton(I18n.t("ui.ModulEinplanen.ErzeugeAufgaben"));
            add(erzeuge, gbc);
            // Zeile 2: Aufgabenpanel
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4; // über volle Breite
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            aufgabenPanel = getAufgabenPanel(7, Lerneinheit.LEKTION);
            aufgabenPanel.setBorder(null);
            add(aufgabenPanel, gbc);
            erzeuge.addActionListener(e -> {
                try {
                    int anzahl = Integer.parseInt(anzEinheiten.getText().trim());
                    Lerneinheit typ = (Lerneinheit) einheitstyp.getSelectedItem();
                    remove(aufgabenPanel);
                    aufgabenPanel = getAufgabenPanel(anzahl, typ);
                    add(aufgabenPanel, gbc);
                    aufgabenPanel.setBorder(null);
                    revalidate();
                    repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Bitte gib eine ganze Zahl ein!", "Ungültige Eingabe",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        @Override
        protected void addAufgaben() {
            vorherige = null;
            for (JPanel p : panelListe) {
                String titel = ((JTextField) p.getComponent(0)).getText();
                Date startDate = (Date) ((JDatePickerImpl) p.getComponent(1)).getModel().getValue();
                LocalDate start = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                Date endDate = (Date) ((JDatePickerImpl) p.getComponent(2)).getModel().getValue();
                LocalDate ende = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                AufgabeDurcharbeiten ad = new AufgabeDurcharbeiten(titel, "", ende, start, Status.NEU, modul, 0, null);
                if (vorherige != null) {
                    vorherige.setFolgeAufgabe(ad);
                }
                vorherige = ad;
                aufgabenListe.add(ad);
            }
        }
    }

    private class SchrittEA extends AufgabenSchritte {
        SchrittEA() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);
            gbc.anchor = GridBagConstraints.WEST;
            // Zeile 1: Label
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(new JLabel(I18n.t("ui.ModulEinplanen.AnzEAs") + ":"), gbc);
            // Zeile 1: Textfeld
            gbc.gridx = 1;
            JTextField anzEinheiten = new JTextField(5);
            add(anzEinheiten, gbc);
            // Zeile 1: Button
            gbc.gridx = 3;
            JButton erzeuge = new JButton(I18n.t("ui.ModulEinplanen.ErzeugeAufgaben"));
            add(erzeuge, gbc);
            // Zeile 2: Aufgabenpanel
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4; // über volle Breite
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            aufgabenPanel = getAufgabenPanel(7);
            aufgabenPanel.setBorder(null);
            add(aufgabenPanel, gbc);
            erzeuge.addActionListener(e -> {
                try {
                    int anzahl = Integer.parseInt(anzEinheiten.getText().trim());
                    remove(aufgabenPanel);
                    aufgabenPanel = getAufgabenPanel(anzahl);
                    add(aufgabenPanel, gbc);
                    aufgabenPanel.setBorder(null);
                    revalidate();
                    repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Bitte gib eine ganze Zahl ein!", "Ungültige Eingabe",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        @Override
        protected void addAufgaben() {
            vorherige = null;
            for (JPanel p : panelListe) {
                String titel = ((JTextField) p.getComponent(0)).getText();
                Date startDate = (Date) ((JDatePickerImpl) p.getComponent(1)).getModel().getValue();
                LocalDate start = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                Date endDate = (Date) ((JDatePickerImpl) p.getComponent(2)).getModel().getValue();
                LocalDate ende = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                AufgabeEA ea = new AufgabeEA(titel, "", ende, start, Status.NEU, modul, "", null);
                if (vorherige != null) {
                    vorherige.setFolgeAufgabe(ea);
                }
                vorherige = ea;
                aufgabenListe.add(ea);
            }
        }
    }

    private class SchrittWiederholen extends AufgabenSchritte {
        SchrittWiederholen() {
            setLayout(new BorderLayout());
            add(new JLabel("Wiederholen Schritt", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        @Override
        protected void addAufgaben() {
            // TODO Auto-generated method stub
        }
    }

    private class SchrittAltklausur extends AufgabenSchritte {
        SchrittAltklausur() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);
            gbc.anchor = GridBagConstraints.WEST;
            // Zeile 1: Label
            gbc.gridx = 0;
            gbc.gridy = 0;
            add(new JLabel(I18n.t("ui.ModulEinplanen.AnzAltklausuren") + ":"), gbc);
            // Zeile 1: Textfeld
            gbc.gridx = 1;
            JTextField anzEinheiten = new JTextField(5);
            add(anzEinheiten, gbc);
            // Zeile 2: Start Altklausuren
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(new JLabel(I18n.t("ui.ModulEinplanen.StartAltklausuren") + ":"), gbc);
            gbc.gridx = 1;
            UtilDateModel startModel = Kalender.getModellMitSemesterdatum(101);
            JDatePanelImpl startDatePanel = new JDatePanelImpl(startModel, Kalender.getCalendarProperties());
            JDatePickerImpl startFeld = new JDatePickerImpl(startDatePanel, new CalendarFormatter());
            add(startFeld, gbc);
            // Zeile 2: Button
            gbc.gridx = 3;
            JButton erzeuge = new JButton(I18n.t("ui.ModulEinplanen.ErzeugeAufgaben"));
            add(erzeuge, gbc);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 4; // über volle Breite
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            // Zeile 3: Aufgabenpanel
            erzeuge.addActionListener(e -> {
                try {
                    LocalDate start = ((Date) startFeld.getModel().getValue()).toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate klausur;
                    if (modul.getKlausurTermin() != null) {
                        klausur = modul.getKlausurTermin();
                    } else {
                        if (Kalender.isSommersemester()) {
                            klausur = LocalDate.of(LocalDate.now().getYear(), Month.AUGUST, 20);
                        } else if (LocalDate.now().getMonth().getValue() <= 12) {
                            klausur = LocalDate.of(LocalDate.now().getYear() + 1, 2, 20);
                        } else {
                            klausur = LocalDate.of(LocalDate.now().getYear(), 2, 20);
                        }
                    }
                    LocalDate ende = klausur.minusDays(10);
                    int anzahl = Integer.parseInt(anzEinheiten.getText().trim());
                    if (aufgabenPanel != null) {
                        remove(aufgabenPanel); // nur entfernen, wenn schon vorhanden
                    }
                    aufgabenPanel = getAufgabenPanel(start, ende, anzahl);
                    add(aufgabenPanel, gbc);
                    aufgabenPanel.setBorder(null);
                    revalidate();
                    repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Bitte gib eine ganze Zahl ein!", "Ungültige Eingabe",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
        }

        @Override
        protected void addAufgaben() {
            vorherige = null;
            for (int i = aufgabenListe.size() - 1; i >= 0; i--) {
                if (aufgabenListe.get(i).getTyp() == Aufgabentyp.DURCHARBEITEN || aufgabenListe.get(i).getTyp() ==  Aufgabentyp.EA) {
                    vorherige = aufgabenListe.get(i);
                    break; // gefunden, Schleife beenden
                }
            }

            for (JPanel p : panelListe) {
                String titel = ((JTextField) p.getComponent(0)).getText();
                Date startDate = (Date) ((JDatePickerImpl) p.getComponent(1)).getModel().getValue();
                LocalDate start = startDate != null ? startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                Date endDate = (Date) ((JDatePickerImpl) p.getComponent(2)).getModel().getValue();
                LocalDate ende = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null;
                AufgabeAltklausur ak = new AufgabeAltklausur(titel, "", ende, start, Status.NEU, modul, "", null);
                if (vorherige != null) {
                    vorherige.setFolgeAufgabe(ak);
                }
                vorherige = ak;
                aufgabenListe.add(ak);
            }
        }
    }
}
