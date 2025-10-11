package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;

import lang.I18n;
import service.Control;
import util.FALoader;

public class Hauptfenster extends JFrame implements IAnsicht {
    private Control control;
    private CenterPanel center;
    private int centerNr;
    

    public Hauptfenster(Control control) {
        super(I18n.t("ui.Main.FensterTitel"));
        centerNr = 0;
        // Button neue Aufgabe
        JButton buttonNeueAufgabe = new JButton(I18n.t("ui.Main.ButtonNeueAufgabe"));
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(center, control));
        //Button Fortschritt anzeigen
        JButton buttonFortschritt = new JButton(I18n.t("ui.Fortschritt.FortschrittAnzeigen"));
        buttonFortschritt.addActionListener(e -> new Fortschritt(control));        
        // Button Einstellungen
        JButton buttonEinstellungen = new JButton("\uf013");
        buttonEinstellungen.setPreferredSize(new java.awt.Dimension(25, 25));
        buttonEinstellungen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));
        buttonEinstellungen.setFont(FALoader.loadFontAwesome());
        buttonEinstellungen.addActionListener(e -> new EinstellungenAnsicht(control));
        
        //Exportieren-Button
        JButton buttonSpeichern = new JButton("\uf0c7");
        buttonSpeichern.setPreferredSize(new java.awt.Dimension(25, 25));
        buttonSpeichern.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 0));
        buttonSpeichern.setFont(FALoader.loadFontAwesome());
        buttonSpeichern.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("data"));
            chooser.setDialogTitle("Datenbank exportieren");
            chooser.setSelectedFile(new File("Lernplaner_backup.db"));
            int result = chooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File ziel = chooser.getSelectedFile();
                control.getDb().exportDatabase(ziel);
            }
        });
        
        //Importieren-Button
        JButton loadButton = new JButton("\uf07c");
        loadButton.setPreferredSize(new java.awt.Dimension(25, 25));
        loadButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        loadButton.setFont(FALoader.loadFontAwesome());
        loadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("data"));
            chooser.setDialogTitle("Datenbank importieren");
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File quelle = chooser.getSelectedFile();
                control.getDb().importDatabase(quelle);
                control.reloadData();
                center.refresh();

            }
        });
        
        // Hauptfenster
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                control.getPersistenz().speichern(control.getAm().getAufgabenListe());
                control.getPersistenz().speichern(control.getEinstellungen());
                System.exit(0);
            }
        });

        //Center Panel
        center = new CenterPanel(control);
        add(center, BorderLayout.CENTER);
        
        //Seitliches Panel
        JPanel westPanel = new NavigationsPanel(this);
        
        // Unteres Panel
        JPanel southPanel = new JPanel();
        southPanel.add(buttonNeueAufgabe);
        // Oberes Panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        JPanel saveLoad = new JPanel(new FlowLayout());
        saveLoad.add(buttonSpeichern);
        saveLoad.add(loadButton);
        northPanel.add(buttonEinstellungen, BorderLayout.EAST);
        northPanel.add(saveLoad, BorderLayout.WEST);
        add(northPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void refresh() {
        center.showPanel(centerNr); // CenterPanel hat Methode, die Inhalt je nach centerNr tauscht
    }

    
    void setCenter(int i) {
        this.centerNr = i;
        refresh();
    }
    
    Control getControl() {
        return control;
    }
}
