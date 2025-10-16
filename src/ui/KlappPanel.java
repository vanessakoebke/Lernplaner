package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;

import model.Lerngruppe.Termin;

public class KlappPanel extends JPanel {
        private final JButton toggleButton;
        private final JPanel contentPanel;
        private boolean expanded = false;

        public KlappPanel(String titel, List<Termin> termine) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            toggleButton = new JButton("➕ " + titel);
            toggleButton.setFocusPainted(false);
            toggleButton.setContentAreaFilled(false);
            toggleButton.setFont(toggleButton.getFont().deriveFont(Font.BOLD, 14f));
            toggleButton.setHorizontalAlignment(SwingConstants.LEFT);

            // 📋 Liste der Termine
            DefaultListModel<Termin> listModel = new DefaultListModel<>();
            for (Termin termin : termine) {
                listModel.addElement(termin);
            }

            JList<Termin> terminListe = new JList<>(listModel);
            terminListe.setVisibleRowCount(Math.min(termine.size(), 5)); // optional: Höhe begrenzen
            terminListe.setFixedCellHeight(25);
            terminListe.setFont(UIManager.getFont("Label.font"));
            terminListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


            contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(terminListe, BorderLayout.CENTER);
            contentPanel.setVisible(false); // Start: eingeklappt

            toggleButton.addActionListener(this::toggle);

            add(toggleButton, BorderLayout.NORTH);
            add(contentPanel, BorderLayout.CENTER);
        }


        private void toggle(ActionEvent e) {
            expanded = !expanded;
            contentPanel.setVisible(expanded);
            toggleButton.setText((expanded ? "➖ " : "➕ ") + toggleButton.getText().substring(2));
            revalidate();
            repaint();
        }
    
}
