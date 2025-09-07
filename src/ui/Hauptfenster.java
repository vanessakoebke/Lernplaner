package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.TableColumn;

import lang.I18n;
import model.Einstellungen;
import model.Status;
import service.AufgabenManager;
import service.Persistenz;
import ui.buttons.DeleteButton;
import ui.buttons.EditButton;
import util.FALoader;

public class Hauptfenster extends JFrame {
    private Persistenz persistenz;
    private AufgabenManager aufgabenManager;
    private Einstellungen einstellungen;

    public Hauptfenster(Persistenz persistenz, AufgabenManager aufgabenManager, Einstellungen einstellungen) {
        super(I18n.t("ui.Main.FensterTitel"));
        this.einstellungen = einstellungen;
        this.persistenz = persistenz;
        this.aufgabenManager = aufgabenManager;
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
        tabelle.getColumnModel().getColumn(4).setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(4).setCellEditor(editButton.getEditor(e -> {
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
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung", // TODO Texte aus I18n
                                                                                                // holen
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                persistenz.speichern(aufgabenManager.getAufgabenListe());
                persistenz.speichern(einstellungen);
                System.exit(0);
            }
        });
        // Button neue Aufgabe
        JButton buttonNeueAufgabe = new JButton(I18n.t("ui.Main.ButtonNeueAufgabe"));
        buttonNeueAufgabe.addActionListener(e -> new NeueAufgabe(aufgabenAnsicht));
        // Button Einstellungen
        JButton buttonEinstellungen = new JButton("\uf013");
        buttonEinstellungen.setPreferredSize(new java.awt.Dimension(25, 25));
        buttonEinstellungen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));
        buttonEinstellungen.setFont(FALoader.loadFontAwesome());
        buttonEinstellungen.addActionListener(e -> new EinstellungenAnsicht(einstellungen));
        // Unteres Panel
        JPanel southPanel = new JPanel();
        southPanel.add(buttonNeueAufgabe);
        // Oberes Panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(buttonEinstellungen, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
