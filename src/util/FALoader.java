package util;

import java.awt.Font;
import java.io.InputStream;

public class FALoader {
    public static Font loadFontAwesome() {
        try (InputStream is = FALoader.class.getResourceAsStream("/Font Awesome 7 Free-Solid-900.otf")) {
            if (is == null) throw new RuntimeException("Font nicht gefunden im Klassenpfad!");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(16f); // Größe anpassen
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 16); // Fallback
        }
    }
}
