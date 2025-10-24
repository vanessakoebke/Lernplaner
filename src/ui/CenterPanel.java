package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import model.Aufgabe;
import model.Status;
import service.Control;
import ui.buttons.DeleteButton;
import ui.buttons.EditButton;

public class CenterPanel extends JPanel implements IAnsicht {
    private AufgabenAnsicht aktuell;
    private AufgabenAnsicht alt;
    private AufgabenAnsicht inArbeit;
    private FortschrittAnsicht fortschrittPanel;
    private LerngruppenAnsicht lerngruppenPanel;
    private final List<JPanel> panelListe = new ArrayList<>();
    private JPanel aktuellesPanel;
    private int index;

    public CenterPanel(Control control) {
        setLayout(new BorderLayout());
        initAufgabenAnsichtAktuell(control);
        initAufgabenAnsichtAlt(control);
        initAufgabenAnsichtInArbeit(control);
        initFortschrittAnsicht(control);
        initLerngruppenAnsicht(control);
        showPanel(2);
        index = 2;
    }

    private void initAufgabenAnsichtAktuell(Control control) {
        aktuell = new AufgabenAnsicht(control, 1);
        JTable tabelle = new JTable(aktuell);
        setupTabelle(tabelle, aktuell, control);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        JPanel aufgabenPanel = new JPanel(new BorderLayout());
        aufgabenPanel.add(scrollPane, BorderLayout.CENTER);
        panelListe.add(0, aufgabenPanel);
    }

    private void initAufgabenAnsichtAlt(Control control) {
        alt = new AufgabenAnsicht(control, 0);
        JTable tabelle = new JTable(alt);
        setupTabelle(tabelle, alt, control);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        JPanel aufgabenPanel = new JPanel(new BorderLayout());
        aufgabenPanel.add(scrollPane, BorderLayout.CENTER);
        panelListe.add(1, aufgabenPanel);
    }

    private void initAufgabenAnsichtInArbeit(Control control) {
        inArbeit = new AufgabenAnsicht(control, 2);
        JTable tabelle = new JTable(inArbeit);
        setupTabelle(tabelle, inArbeit, control);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        JPanel aufgabenPanel = new JPanel(new BorderLayout());
        aufgabenPanel.add(scrollPane, BorderLayout.CENTER);
        panelListe.add(2, aufgabenPanel);
    }

    private void initFortschrittAnsicht(Control control) {
        fortschrittPanel = new FortschrittAnsicht(control);
        JScrollPane scrollPane = new JScrollPane(fortschrittPanel);
        JPanel fortPanelContainer = new JPanel(new BorderLayout());
        fortPanelContainer.add(scrollPane, BorderLayout.CENTER);
        panelListe.add(3, fortPanelContainer);
    }

    private void initLerngruppenAnsicht(Control control) {
        lerngruppenPanel = new LerngruppenAnsicht(control, this);
        JScrollPane scrollPane = new JScrollPane(lerngruppenPanel);
        JPanel lgPanelContainer = new JPanel(new BorderLayout());
        lgPanelContainer.add(scrollPane, BorderLayout.CENTER);
        panelListe.add(4, lgPanelContainer);
    }

    private void setupTabelle(JTable tabelle, AufgabenAnsicht model, Control control) {
        TableRowSorter<AufgabenAnsicht> sorter = new TableRowSorter<>(model);
        tabelle.setRowSorter(sorter);
        tabelle.getTableHeader().setFont(tabelle.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        // Datumsspalten
        TableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof LocalDate date) {
                    setText(date.format(control.getEinstellungen().getDatumsformat()));
                } else
                    setText("");
            }
        };
        tabelle.getColumnModel().getColumn(3).setCellRenderer(dateRenderer);
        tabelle.getColumnModel().getColumn(4).setCellRenderer(dateRenderer);
        // Status-Dropdown
        JComboBox<Status> statusCombo = new JComboBox<>(Status.values());
        tabelle.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(statusCombo));
        // Bearbeiten
        EditButton editButton = new EditButton();
        tabelle.getColumnModel().getColumn(6).setCellRenderer(editButton.getRenderer());
        tabelle.getColumnModel().getColumn(6).setCellEditor(editButton.getEditor(e -> {
            int viewRow = tabelle.getSelectedRow();
            if (viewRow < 0) return;
            int modelRow = tabelle.convertRowIndexToModel(viewRow);
            if (tabelle.isEditing()) tabelle.getCellEditor().stopCellEditing();
            new AufgabeBearbeiten(model, modelRow, control);
        }));
        fixButtonColumnWidth(tabelle, 6, editButton.getButton());
        // Löschen
        DeleteButton deleteButton = new DeleteButton();
        tabelle.getColumnModel().getColumn(7).setCellRenderer(deleteButton.getRenderer());
        tabelle.getColumnModel().getColumn(7).setCellEditor(deleteButton.getEditor(e -> {
            int viewRow = tabelle.getSelectedRow();
            if (viewRow < 0) return;
            int modelRow = tabelle.convertRowIndexToModel(viewRow);
            Aufgabe a = model.getAufgabe(modelRow);
            if (JOptionPane.showConfirmDialog(null, "Aufgabe wirklich löschen?", "Bestätigung",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                if (tabelle.isEditing()) tabelle.getCellEditor().stopCellEditing();
                model.removeAufgabe(modelRow, a.getId(), tabelle); // Modell löschen, fireTableDataChanged() wird
                                                                   // aufgerufen
            }
        }));
        fixButtonColumnWidth(tabelle, 7, deleteButton.getButton());
    }

    private void fixButtonColumnWidth(JTable table, int colIndex, JButton button) {
        int breite = button.getPreferredSize().width + 40;
        TableColumn col = table.getColumnModel().getColumn(colIndex);
        col.setPreferredWidth(breite);
        col.setMaxWidth(breite);
        col.setMinWidth(breite);
    }

    public void showPanel(int index) {
        if (index < 0 || index >= panelListe.size()) return;
        if (aktuellesPanel != null) remove(aktuellesPanel);
        aktuellesPanel = panelListe.get(index);
        add(aktuellesPanel, BorderLayout.CENTER);
        this.index = index;
        if (index == 3 && fortschrittPanel != null) fortschrittPanel.refresh();
        if (index == 4 && lerngruppenPanel != null) lerngruppenPanel.refresh();
        revalidate();
        repaint();
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void refresh() {
        if (aktuell != null) aktuell.refresh();
        if (alt != null) alt.refresh();
        if (aktuellesPanel != null) aktuellesPanel.revalidate();
        if (lerngruppenPanel != null) lerngruppenPanel.refresh();
        revalidate();
        repaint();
    }

    AufgabenAnsicht getAktuell() {
        return aktuell;
    }

    AufgabenAnsicht getAlt() {
        return alt;
    }

    public JPanel getPanel(int index) {
        if (index < 0 || index >= panelListe.size()) return null;
        return panelListe.get(index);
    }
}
