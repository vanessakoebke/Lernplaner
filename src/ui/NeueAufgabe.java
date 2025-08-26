package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import java.time.LocalDate;
import java.time.format.*;
import java.util.*;
import model.*;

public class NeueAufgabe extends JFrame {

     
    public NeueAufgabe(DefaultTableModel tabellenModell) {
        super("Neue Aufgabe");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(450, 300);
        this.setLocationRelativeTo(null);

       
        EingabePanel eingabePanel = new EingabePanel();
        JPanel buttonPanel = new JPanel();
        JButton buttonAbbrechen = new JButton("Abbrechen");
        buttonAbbrechen.addActionListener(e -> {
            this.dispose();
        });
        JButton buttonHinzu = new JButton("Hinzufügen");
        buttonHinzu.addActionListener(e -> {
            Aufgabe neue = eingabePanel.getAufgabe();
            Object[] zeile = {
                neue.getTitel(),
                neue.getBeschreibung() != null ? neue.getBeschreibung() : "",
                neue.getFaelligkeit() != null ? neue.getFaelligkeit().toString() : "",
                neue.getStatus()
            };
            tabellenModell.addRow(zeile);
            this.dispose();});
        buttonPanel.add(buttonAbbrechen);
        buttonPanel.add(buttonHinzu);
        add(eingabePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

  
    
}
