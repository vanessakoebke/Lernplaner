package util;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class FALoader {
    public static Font loadFontAwesome() {
        try {
            File fontFile = new File("src/resources//fontawesome-free-7.0.0-desktop/otfs/Font Awesome 7 Free-Solid-900.otf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontFile));
            return font.deriveFont(16f); // Größe anpassen
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 16); // Fallback
        }
    }
}
