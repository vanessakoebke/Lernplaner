package ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import util.FALoader;

import javax.swing.AbstractCellEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class SymbolButton {

    private final JButton button;

    // Konstruktor für Text-Button
    public SymbolButton(String text) {
        Font fa = FALoader.loadFontAwesome();
        button = new JButton(text);
        initButton();
        button.setPreferredSize(new java.awt.Dimension(24, 24));
        button.setFont(fa);
    }

    // Konstruktor für Icon-Button
    public SymbolButton(Icon icon) {
        button = new JButton(icon);
        initButton();
        button.setPreferredSize(new Dimension(icon.getIconWidth() + 4, icon.getIconHeight() + 4));
    }

    // Gemeinsame Initialisierung
    private void initButton() {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false); // <<< Hintergrund transparent machen
    }

    public JButton getButton() {
        return button;
    }

    public TableCellRenderer getRenderer() {
        return new ButtonRenderer();
    }

    public TableCellEditor getEditor(ActionListener clickAction) {
        return new ButtonEditor(clickAction);
    }

    // --------------------- innere Klassen ---------------------

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            super(button.getText());
            setIcon(button.getIcon());
            setFont(button.getFont()); // <<< FontAwesome-Font setzen
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setFont(button.getFont()); // <<< wichtig: jedes Mal den Font setzen
            return this;
        }
    }

    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton editorButton;

        public ButtonEditor(ActionListener clickAction) {
            editorButton = new JButton(button.getText(), button.getIcon());
            editorButton.setFont(button.getFont()); // <<< Font setzen
            editorButton.setFocusPainted(false);
            editorButton.setBorderPainted(false);
            editorButton.setContentAreaFilled(false);
            editorButton.setOpaque(false);
            editorButton.addActionListener(e -> {
                clickAction.actionPerformed(e);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            editorButton.setFont(button.getFont()); // <<< nochmal sicherstellen
            return editorButton;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }

}
