package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

final class Theme {

    static final Color INK = new Color(20, 20, 20);
    static final Color MUTED = new Color(110, 109, 104);
    static final Color CANVAS = new Color(245, 244, 239);
    static final Color SURFACE = Color.WHITE;
    static final Color BORDER = new Color(231, 229, 222);
    static final Color TAB_TRACK = new Color(240, 238, 232);
    static final Color LIME = new Color(215, 244, 92);
    static final Color LIME_SOFT = new Color(238, 248, 196);
    static final Color ERROR = new Color(196, 64, 48);
    static final Color BRAND_TEXT = new Color(170, 169, 162);

    private static final String FAMILY = pickFamily();

    private Theme() {
    }

    static Font font(int style, float size) {
        return new Font(FAMILY, style, Math.round(size)).deriveFont(size);
    }

    private static String pickFamily() {
        final List<String> installed = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String candidate : new String[] {"Plus Jakarta Sans", "Inter", "Helvetica Neue", "Segoe UI"}) {
            if (installed.contains(candidate)) {
                return candidate;
            }
        }
        return Font.SANS_SERIF;
    }
}
