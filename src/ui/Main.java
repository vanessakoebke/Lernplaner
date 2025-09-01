package ui;

import model.*;
import service.*;
import lang.I18n;
import util.*;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        // Verzeichnisse & Sprache
        new File("data").mkdirs();
        I18n.load("de");

        Persistenz persistenz = new Persistenz();
        AufgabenManager aufgabenManager = new AufgabenManager(persistenz.laden());

        // TableModel
        AufgabenAnsicht tableModel = new AufgabenAnsicht(aufgabenManager);

        // JTable
        JTable tabelle = new JTable(tableModel);
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // Status-ComboBox
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

     // Spalte "Bearbeiten"
        EditButton editButton = new EditButton();
        int spalte = 4; // Index der Spalte
        tabelle.getColumnModel().getColumn(spalte)
                .setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(spalte)
                .setCellEditor(editButton.getEditor(e -> {
                    System.out.println("Das hat geklappt.");
                }));

        // Spaltenbreite anpassen
        JButton tempButton = editButton.getButton();
        int buttonBreite = tempButton.getPreferredSize().width + 60; // Puffer hinzufügen
        TableColumn col = tabelle.getColumnModel().getColumn(spalte);
        col.setPreferredWidth(buttonBreite);
        col.setMaxWidth(buttonBreite);
        col.setMinWidth(buttonBreite);



        // Spalte "Löschen"
        DeleteButton deleteButton = new DeleteButton(); 
        tabelle.getColumnModel().getColumn(5).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(5).setCellEditor(deleteButton.getEditor(e -> {
            int row = tabelle.getSelectedRow();
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?") == JOptionPane.YES_OPTION) {
                tableModel.removeAufgabe(row);
            }
        }));

        JScrollPane scrollPane = new JScrollPane(tabelle);

        // Hauptfenster
        JFrame hauptfenster = new JFrame(I18n.t("ui.Main.FensterTitel"));
        hauptfenster.setSize(800, 600);
        hauptfenster.setLocationRelativeTo(null);
        hauptfenster.setLayout(new BorderLayout());

        hauptfenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                persistenz.speichern(aufgabenManager.getAufgabenListe());
                System.exit(0);
            }
        });

        // Button neue Aufgabe
        JButton buttonNeueAufgabe = new JButton(I18n.t("ui.Main.ButtonNeueAufgabe"));
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(aufgabe -> tableModel.addAufgabe(aufgabe)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonNeueAufgabe);

        hauptfenster.add(scrollPane, BorderLayout.CENTER);
        hauptfenster.add(buttonPanel, BorderLayout.SOUTH);
        hauptfenster.setVisible(true);
    }
}
