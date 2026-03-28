package ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import lang.I18n;
import model.Lerngruppe;
import service.Control;
import util.FALoader;

public class LerngruppenAnsicht extends JPanel implements IAnsicht {
    private CenterPanel center;
    private final Control control;
    private final List<LGPanel> lerngruppen = new ArrayList<>();
    private  final DateTimeFormatter formatter;

    public LerngruppenAnsicht(Control control, CenterPanel center) {
        this.control = control;
        this.center = center;
        this.formatter = control.getEinstellungen().getDatumsformat();
        setLayout(new GridBagLayout());
        refresh();
    }

    @Override
    public void refresh() {
        removeAll();
        lerngruppen.clear();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(10, 15, 10, 15);

        for (Lerngruppe lg : control.getLm().getLerngruppen()) {
            LGPanel lgPanel = new LGPanel(lg, control);
            add(lgPanel, gbc);
            lerngruppen.add(lgPanel);
            gbc.gridy++;
        }

        // Füll-Komponente für den unteren Platz
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        revalidate();
        repaint();
    }

    private  class LGPanel extends JPanel implements IAnsicht {
        private final Lerngruppe lg;
        private final JLabel titel;
        private final JComboBox<Lerngruppe.Termin> terminDropdown;
        private final JTextArea aufgabe;
        private final JButton deleteButton;
        private final DateTimeFormatter formatter;

        private LGPanel(Lerngruppe lg, Control control) {
            this.lg = lg;
            this.formatter = control.getEinstellungen().getDatumsformat();

            setLayout(new GridBagLayout());
            setBackground(new Color(250, 250, 250));
            setBorder(new CompoundBorder(
                    new LineBorder(new Color(180, 180, 180), 1, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 4, 4, 4);
            gbc.anchor = GridBagConstraints.WEST;

            // Titel 
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.NONE;
            titel = new JLabel(lg.getTitel());
            titel.setFont(titel.getFont().deriveFont(Font.BOLD, 16f));
            add(titel, gbc);

         // Todo 
            gbc.gridx = 1;
            JLabel todo = new JLabel(I18n.t("ui.Lerngruppe.Todo"));
            add(todo, gbc);
            
            // Löschen-Button 
            gbc.gridx = 3;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.EAST;
            deleteButton = new JButton("\uf1f8"); 
            Font fa = FALoader.loadFontAwesome();
            deleteButton.setFont(fa);
            deleteButton.setToolTipText("Lerngruppe löschen");
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Lerngruppe \"" + lg.getTitel() + "\" wirklich löschen?",
                        "Bestätigung",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // 1. Lerngruppe aus dem Manager entfernen
                    control.getLm().deleteLg(lg);
                    control.getDb().removeLerngruppe(lg);

                    // 2. Panel aus der LerngruppenAnsicht entfernen
                    JPanel parent = (JPanel) this.getParent();
                    if (parent != null) {
                        parent.remove(this);

                        // 3. Layout aktualisieren
                        parent.revalidate();
                        parent.repaint();
                        center.refresh();
                    }
                }
            });

            add(deleteButton, gbc);

            // Dropdown für Termine (neue Zeile)
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1; // nur erste Spalte
            terminDropdown = new JComboBox<>(lg.getTermine().toArray(new Lerngruppe.Termin[0]));
            terminDropdown.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Lerngruppe.Termin t) {
                        setText(t.getDatum().format(formatter) + 
                                (t.getUhrzeitTermin() != null ? " - " + t.getUhrzeitTermin().toString() : ""));
                    }
                    return this;
                }
            });
            terminDropdown.setSize(25, 50);
            add(terminDropdown, gbc);



            // Aufgabe anzeigen 
            aufgabe = new JTextArea(4, 30);
            aufgabe.setLineWrap(true);
            aufgabe.setWrapStyleWord(true);
            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.EAST;
            aufgabe.setEditable(false);
            add(aufgabe, gbc);
            
         // Bearbeiten-Button rechts 
            JButton bearbeitenButton = new JButton("\uf044");
            bearbeitenButton.setFont(fa);
            gbc.gridx = 3;
           
            add(bearbeitenButton, gbc);

            bearbeitenButton.addActionListener(e -> {
                Lerngruppe.Termin ausgewaehlterTermin = (Lerngruppe.Termin) terminDropdown.getSelectedItem();
                if (ausgewaehlterTermin != null && ausgewaehlterTermin.getAufgabe() != null) {
                    // Hier öffnest du dein Bearbeiten-Fenster
                    JFrame fenster = new AufgabeBearbeiten(ausgewaehlterTermin.getAufgabe(), control);
                    fenster.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
                            updateAufgabeText(ausgewaehlterTermin);
                        }
                    });
                }
            });


            // Vorauswahl: nächster Termin
            Lerngruppe.Termin naechster = lg.getNaechstenTermin();
            if (naechster != null) {
                terminDropdown.setSelectedItem(naechster);
            }
            updateAufgabeText((Lerngruppe.Termin) terminDropdown.getSelectedItem());


            // Listener für Dropdown → Aufgabe aktualisieren
            terminDropdown.addActionListener(e -> {
                Lerngruppe.Termin selected = (Lerngruppe.Termin) terminDropdown.getSelectedItem();
                updateAufgabeText(selected);
            });
        }

        private void updateAufgabeText(Lerngruppe.Termin termin) {
            if (termin == null || termin.getAufgabe() == null) {
                aufgabe.setText("");
            } else {
                String beschr = termin.getAufgabe().getBeschreibung();
                this.aufgabe.setText( (beschr == null
                        ? ""
                        : beschr));
            }
            this.revalidate();
            this.repaint();
        }

        @Override
        public void refresh() {
            titel.setText(lg.getTitel());
            terminDropdown.removeAllItems();
            for (Lerngruppe.Termin t : lg.getTermine()) {
                terminDropdown.addItem(t);
            }
            Lerngruppe.Termin naechster = lg.getNaechstenTermin();
            if (naechster != null) {
                terminDropdown.setSelectedItem(naechster);
            }
            updateAufgabeText((Lerngruppe.Termin) terminDropdown.getSelectedItem());
        }
    }


}
