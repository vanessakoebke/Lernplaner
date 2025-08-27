package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import java.time.LocalDate;
import java.time.format.*;
import java.util.*;
import model.*;
import service.*;

public class NeueAufgabe extends JFrame {
    private EingabePanel eingabePanel;
    private AufgabenManager aufgabenManager;

     
    public NeueAufgabe(DefaultTableModel tabellenModell, AufgabenManager aufgabenManager) {
        super("Neue Aufgabe");
        

       this.aufgabenManager = aufgabenManager;
        this.eingabePanel = new EingabePanel();
        
        
        //Abbrechen-Button
        JButton buttonAbbrechen = new JButton("Abbrechen");
        buttonAbbrechen.addActionListener(e -> {
            this.dispose();
        });
        //Hinzufügen-Button
        JButton buttonHinzu = new JButton("Hinzufügen");
        buttonHinzu.addActionListener(e -> {
            Aufgabe aufgabe = eingabePanel.getAufgabe();
            Object[] zeile = {
                aufgabe.getTitel(),
                aufgabe.getBeschreibung() != null ? aufgabe.getBeschreibung() : "",
                aufgabe.getFaelligkeit() != null ? aufgabe.getFaelligkeit().toString() : "",
                aufgabe.getStatus()
            };
            tabellenModell.addRow(zeile);
            aufgabenManager.addAufgabe(aufgabe);
            this.dispose();});
        
      //Button-Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonHinzu);
        buttonPanel.add(buttonAbbrechen);
        
        //Neue-Aufgabe-Fenster
        add(eingabePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(450, 300);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

}
