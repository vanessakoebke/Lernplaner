package ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.*;
import service.Control;

public class FortschrittAnsicht extends JPanel implements IAnsicht {
    private Control control;
    private final List<ModulProgress> moduleData = new ArrayList<>();

    private static final int PROGRESS_HEIGHT = 30;
    private static final int LABEL_HEIGHT = 25; // etwas größer für größere Schrift
    private static final int SPACING = 15;
    private static final int MARGIN = 10;
    private static final int CORNER_RADIUS = PROGRESS_HEIGHT; // komplett pillförmig

    private class ModulProgress {
        String name;
        int prozent;

        ModulProgress(String name, int prozent) {
            this.name = name;
            this.prozent = prozent;
        }
    }

    public FortschrittAnsicht(Control control) {
        this.control = control;
        setOpaque(false); // Panel transparent

        // Daten initial laden
        for (Modul modul : control.getMm().getAktuelleModule()) {
            moduleData.add(new ModulProgress(modul.getName(), getProzent(modul)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color progressColor = UIManager.getColor("textHighlight");      // System-Akzentfarbe
        if (progressColor == null) {
            progressColor = new Color(0, 122, 255); // macOS-Blau
        }
        
        Color backgroundColor = Color.decode("#f5f5f5"); // noch helleres Grau
        Color textColor = Color.BLACK;

        int y = MARGIN;
        int width = getWidth() - 2 * MARGIN;

        for (ModulProgress data : moduleData) {
            // Modulname: größer und fett
            g2d.setColor(UIManager.getColor("Label.foreground"));
            g2d.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 16f));
            g2d.drawString(data.name, MARGIN, y + LABEL_HEIGHT - 5);

            y += LABEL_HEIGHT + 5;

            // Hintergrund des Balkens (hellgrau)
            RoundRectangle2D backgroundRect = new RoundRectangle2D.Double(MARGIN, y, width, PROGRESS_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);
            g2d.setColor(backgroundColor);
            g2d.fill(backgroundRect);

            // Fortschritt
            if (data.prozent > 0) {
                int progressWidth = (int) (width * (data.prozent / 100.0));
                RoundRectangle2D progressRect = new RoundRectangle2D.Double(MARGIN, y, progressWidth, PROGRESS_HEIGHT, CORNER_RADIUS, CORNER_RADIUS);
                g2d.setColor(progressColor);
                g2d.fill(progressRect);
            }

            // Prozenttext mittig
            String text = data.prozent + "%";
            g2d.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = MARGIN + (width - textWidth) / 2;
            int textY = y + (PROGRESS_HEIGHT + fm.getAscent()) / 2 - 2;

            g2d.setColor(textColor);
            g2d.drawString(text, textX, textY);

            y += PROGRESS_HEIGHT + SPACING;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int totalHeight = MARGIN * 2 + moduleData.size() * (LABEL_HEIGHT + 5 + PROGRESS_HEIGHT + SPACING);
        return new Dimension(400, totalHeight);
    }

    @Override
    public void refresh() {
        moduleData.clear();
        for (Modul modul : control.getMm().getAktuelleModule()) {
            moduleData.add(new ModulProgress(modul.getName(), getProzent(modul)));
        }
        revalidate();
        repaint();
    }

    private int getProzent(Modul modul) {
        List<Aufgabe> liste = control.getAm().getAufgabenListe(modul);
        if (liste.isEmpty()) return 0;

        liste.removeIf(a -> a instanceof AufgabeLerngruppe);

        long erledigt = liste.stream()
                .filter(a -> a != null && a.getStatus() == Status.ERLEDIGT)
                .count();

        return (int) ((erledigt * 100.0) / liste.size());
    }
}
