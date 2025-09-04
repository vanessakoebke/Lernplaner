package ui;

import model.*;
import service.*;
import ui.buttons.DeleteButton;
import ui.buttons.EditButton;
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
        AufgabenAnsicht aufgabenAnsicht = new AufgabenAnsicht(aufgabenManager);

        // JTable
        JTable tabelle = new JTable(aufgabenAnsicht);
        tabelle.getTableHeader().setFont(tabelle.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // Status-Dropdown
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

     // Spalte "Bearbeiten"
        EditButton editButton = new EditButton();
        tabelle.getColumnModel().getColumn(4)
                .setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(4)
                .setCellEditor(editButton.getEditor(e -> {
                    new AufgabeBearbeiten(aufgabenAnsicht, tabelle.getEditingRow());
                }));

        // Spaltenbreite anpassen
        JButton tempButton = editButton.getButton();
        int buttonBreite = tempButton.getPreferredSize().width + 40; // Puffer hinzufügen
        TableColumn col = tabelle.getColumnModel().getColumn(4);
        col.setPreferredWidth(buttonBreite);
        col.setMaxWidth(buttonBreite);
        col.setMinWidth(buttonBreite);



        // Spalte "Löschen"
        DeleteButton deleteButton = new DeleteButton(); 
        tabelle.getColumnModel().getColumn(5).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(5).setCellEditor(deleteButton.getEditor(e -> {
            int row = tabelle.getSelectedRow();
            int id = aufgabenAnsicht.getAufgabe(row).getId();
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                aufgabenAnsicht.removeAufgabe(row, id);
            }
        }));
        // Spaltenbreite anpassen
        tempButton = deleteButton.getButton();
        buttonBreite = tempButton.getPreferredSize().width + 40; // Puffer hinzufügen
        col = tabelle.getColumnModel().getColumn(5);
        col.setPreferredWidth(buttonBreite);
        col.setMaxWidth(buttonBreite);
        col.setMinWidth(buttonBreite);

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
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(aufgabenAnsicht));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonNeueAufgabe);

        hauptfenster.add(scrollPane, BorderLayout.CENTER);
        hauptfenster.add(buttonPanel, BorderLayout.SOUTH);
        hauptfenster.setVisible(true);
    }
}
