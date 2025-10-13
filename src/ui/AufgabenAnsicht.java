package ui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import lang.I18n;
import model.*;
import service.Control;

public class AufgabenAnsicht extends AbstractTableModel implements IAnsicht {
    private final String[] spaltenTitel = { I18n.t("model.Aufgabentyp.Modul"), I18n.t("ui.Common.Aufgabenname"),
            I18n.t("ui.Common.Beschreibung"), I18n.t("ui.Common.Start"), I18n.t("ui.Common.Faelligkeit"),
            I18n.t("ui.Common.Status"), "", // Bearbeiten
            "" // Löschen
    };
    private final Control control;
    private final int aktuellView; // 0 = erledigt Aufgaben, 1= aktuelle Aufgaben, 2 = Aufgaben in Arbeit

    public AufgabenAnsicht(Control control, int aktuell) {
        this.control = control;
        this.aktuellView = aktuell;


    }

    /**
     * Liefert immer die aktuelle Liste (kein Snapshot!)
     */
    private List<Aufgabe> getBackingList() {
        switch (aktuellView) {
        case 0:
            return control.getAm().getAlte();
        case 1:
            return control.getAm().getAktuelle();
        case 2:
            return control.getAm().filter(e -> 
                e.getStart() != null && e.getStart().isBefore(LocalDate.now().minusDays(1)) && !e.isErledigt()
            );
        default:
            return control.getAm().getAktuelle();
        }
    }

    @Override
    public int getRowCount() {
        return getBackingList().size();
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
        switch (columnIndex) {
            case 0: return Modul.class;      // Modul
            case 3:                         // Start
            case 4:                         // Ende
                return LocalDate.class;
            case 5: return Status.class;
            default: return String.class;
        }
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5 || columnIndex >= 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Aufgabe a = getBackingList().get(rowIndex);
        switch (columnIndex) {
        case 0:
            return a.getModul();
        case 1:
            return a.getTitel();
        case 2:
            return a.getBeschreibung() != null ? a.getBeschreibung() : "";
        case 3: 
            return a.getStart();  // LocalDate direkt zurückgeben
        case 4: 
            return a.getEnde();   // LocalDate direkt zurückgeben
        case 5:
            return a.getStatus();
        default:
            return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 5 && aValue instanceof Status) {
            Aufgabe aufgabe = getBackingList().get(rowIndex);
            aufgabe.setStatus((Status) aValue);
            fireTableDataChanged();
        }
    }

    public void addAufgabe(Aufgabe aufgabe) {
        control.getAm().addAufgabe(aufgabe);
        fireTableDataChanged();
    }

    public void updateAufgabe(Aufgabe aufgabe, int id, int row) {
        control.getAm().updateAufgabe(aufgabe, id);
        fireTableDataChanged();
    }

    public Aufgabe getAufgabe(int row) {
        return getBackingList().get(row);
    }

    public void removeAufgabe(int row, int id) {
        Aufgabe zuLoeschen = control.getAm().getAufgabenListe().stream()
                .filter(a -> a.getId() != null && a.getId() == id).findFirst().orElse(null);
        if (zuLoeschen != null) {
            control.getAm().getAufgabenListe().remove(zuLoeschen);
            control.getDb().deleteAufgabe(zuLoeschen);
            fireTableDataChanged();
        }
    }

    @Override
    public void refresh() {
        // ruft einfach die Tabelle neu ab – Daten kommen dynamisch über
        // getBackingList()
        fireTableDataChanged();
    }
}
