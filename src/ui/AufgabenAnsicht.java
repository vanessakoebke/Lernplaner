package ui;

import model.*;
import service.*;
import lang.I18n;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AufgabenAnsicht extends AbstractTableModel {

    private final String[] spaltenTitel = {
            I18n.t("ui.Common.Aufgabenname"),
            I18n.t("ui.Common.Beschreibung"),
            I18n.t("ui.Common.Faelligkeit"),
            I18n.t("ui.Common.Status"),
            "", // Bearbeiten
            ""  // Löschen
    };

    private AufgabenManager aufgabenManager;
    private Einstellungen einstellungen;
    

    public AufgabenAnsicht(AufgabenManager aufgabenManager, Einstellungen einstellungen) {
        this.aufgabenManager = aufgabenManager;
        this.einstellungen = einstellungen;
    }

    @Override
    public int getRowCount() {
        return aufgabenManager.getAufgabenListe().size();
    }

    @Override
    public int getColumnCount() {
        return spaltenTitel.length;
    }

    @Override
    public String getColumnName(int column) {
        return spaltenTitel[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) return Status.class; // Status-Enum
        if (columnIndex >= 4) return Object.class;  // Buttons
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3 || columnIndex >= 4; // Status + Buttons editierbar
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Aufgabe a = aufgabenManager.getAufgabenListe().get(rowIndex);
        switch (columnIndex) {
            case 0: return a.getTitel();
            case 1: return a.getBeschreibung() != null ? a.getBeschreibung() : "";
            case 2: return a.getFaelligkeit() != null ? a.getFaelligkeit().format(einstellungen.getDatumsformat()) : "";
            case 3: return a.getStatus();
            default: return "Button"; // Platzhalter für Button-Spalten
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 3 && aValue instanceof Status) {
            Aufgabe aufgabe = aufgabenManager.getAufgabenListe().get(rowIndex);
            aufgabe.setStatus((Status) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void addAufgabe(Aufgabe aufgabe) {
        aufgabenManager.addAufgabe(aufgabe);
        int row = getRowCount() - 1;
        fireTableRowsInserted(row, row);
    }
    
    public void updateAufgabe(Aufgabe aufgabe, int id, int row) {
        aufgabenManager.updateAufgabe(aufgabe, id);
        fireTableRowsUpdated(row, row);
    }

    public Aufgabe getAufgabe(int row) {
        return aufgabenManager.getAufgabenListe().get(row);
    }

    public void removeAufgabe(int row, int id) {
        aufgabenManager.getAufgabenListe().removeIf(a -> a.getId() == id);
        fireTableRowsDeleted(row, row);
    }
}
