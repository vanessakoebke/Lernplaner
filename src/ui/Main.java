package ui;

import model.Aufgabe;
import service.AufgabenManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1️⃣ Aufgabenmanager erzeugen
        AufgabenManager aufgabenManager = new AufgabenManager();
        
        // 2️⃣ JFrame als Hauptfenster
        JFrame hauptfenster = new JFrame("Lernplaner");
        hauptfenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hauptfenster.setSize(900, 600);
        hauptfenster.setLocationRelativeTo(null);
        // 3️⃣ TableModel für die JTable vorbereiten
        String[] spaltenTitel = { "Titel", "Beschreibung", "Fälligkeit", "Status" };
        DefaultTableModel tabellenModell = new DefaultTableModel(spaltenTitel, 0); // 0 = Zeilenanzahl initial
        // Aufgaben in TableModel einfügen
        List<Aufgabe> aufgabenListe = aufgabenManager.getAufgabenListe();
        for (Aufgabe a : aufgabenListe) {
            Object[] zeile = { a.getTitel(), a.getBeschreibung() != null ? a.getBeschreibung() : "",
                    a.getFaelligkeit() != null ? a.getFaelligkeit().toString() : "", a.getStatus() };
            tabellenModell.addRow(zeile);
        }
        // 4️⃣ JTable erstellen
        JTable aufgabenTabelle = new JTable(tabellenModell);
        JScrollPane scrollPane = new JScrollPane(aufgabenTabelle);
        // 5️⃣ Button zum Hinzufügen von Aufgaben
        JButton aufgabeHinzu = new JButton("Aufgabe hinzufügen");
         aufgabeHinzu.addActionListener(e -> new NeueAufgabe(tabellenModell));

        // 6️⃣ Layout
        JPanel hauptPanel = new JPanel(new BorderLayout());
        hauptPanel.add(scrollPane, BorderLayout.CENTER);
        hauptPanel.add(aufgabeHinzu, BorderLayout.SOUTH);
        hauptfenster.setContentPane(hauptPanel);
        hauptfenster.setVisible(true);
    }
}
