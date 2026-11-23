package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

final class AuthLayout {

    static final int FORM_WIDTH = 400;
    private static final int BRAND_WIDTH = 520;
    private static final int BRAND_PADDING = 56;

    private AuthLayout() {
    }

    static void build(JPanel screen, JComponent form) {
        screen.setLayout(new BorderLayout());
        screen.setBackground(Theme.CANVAS);
        screen.add(brandPanel(), BorderLayout.WEST);

        final JPanel formSide = new JPanel(new GridBagLayout());
        formSide.setBackground(Theme.CANVAS);
        final JPanel column = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(FORM_WIDTH, super.getPreferredSize().height);
            }
        };
        column.setOpaque(false);
        column.add(form, BorderLayout.CENTER);
        formSide.add(column);
        screen.add(formSide, BorderLayout.CENTER);
    }

    static JPanel formColumn() {
        final JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        return form;
    }

    static JPanel tabs(boolean loginActive, Runnable onLogin, Runnable onSignup) {
        final RoundedPanel track = new RoundedPanel(new GridLayout(1, 2, 4, 0), 24, Theme.TAB_TRACK, null);
        track.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        track.add(tab("Log in", loginActive, onLogin));
        track.add(tab("Sign up", !loginActive, onSignup));
        track.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        track.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        return track;
    }

    private static JComponent tab(String text, boolean active, Runnable onClick) {
        final RoundedPanel pill = new RoundedPanel(new BorderLayout(), 18, active ? Theme.SURFACE : null, null);
        final JButton button = new JButton(text);
        button.setFont(Theme.font(Font.BOLD, 14f));
        button.setForeground(active ? Theme.INK : Theme.MUTED);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        if (!active) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(evt -> onClick.run());
        }
        pill.add(button, BorderLayout.CENTER);
        return pill;
    }

    static JPanel heading(String title, String subtitle) {
        final JPanel panel = formColumn();
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(text(title, Font.BOLD, 28f, Theme.INK));
        panel.add(Box.createVerticalStrut(6));
        panel.add(text(subtitle, Font.PLAIN, 14f, Theme.MUTED));
        return panel;
    }

    static JPanel field(String label, InputField input, JLabel hint) {
        final JPanel panel = formColumn();
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.add(text(label, Font.BOLD, 13f, Theme.INK));
        panel.add(Box.createVerticalStrut(8));
        panel.add(input);
        if (hint != null) {
            panel.add(Box.createVerticalStrut(6));
            panel.add(hint);
        }
        return panel;
    }

    static JLabel hint(String message) {
        return text(message, Font.PLAIN, 12f, Theme.MUTED);
    }

    static JLabel errorLabel() {
        final JLabel label = text(" ", Font.PLAIN, 13f, Theme.ERROR);
        label.setVisible(false);
        return label;
    }

    static void showError(JLabel label, String message) {
        final boolean hasMessage = message != null && !message.isEmpty();
        label.setText(hasMessage ? message : " ");
        label.setVisible(hasMessage);
    }

    static JPanel note(String message) {
        final RoundedPanel box = new RoundedPanel(new BorderLayout(), 24, Theme.LIME_SOFT, null);
        box.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        final JTextArea body = wrapped(message, Font.PLAIN, 13f, Theme.INK, FORM_WIDTH - 32);
        box.add(body, BorderLayout.CENTER);
        box.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        final int height = body.getPreferredSize().height + 28;
        box.setPreferredSize(new Dimension(FORM_WIDTH, height));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        return box;
    }

    static JLabel text(String content, int style, float size, Color color) {
        final JLabel label = new JLabel(content);
        label.setFont(Theme.font(style, size));
        label.setForeground(color);
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return label;
    }

    static JTextArea wrapped(String content, int style, float size, Color color, int width) {
        final JTextArea area = new JTextArea(content);
        area.setFont(Theme.font(style, size));
        area.setForeground(color);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setBorder(BorderFactory.createEmptyBorder());
        area.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        area.setSize(width, Short.MAX_VALUE);
        final Dimension fitted = new Dimension(width, area.getPreferredSize().height);
        area.setPreferredSize(fitted);
        area.setMaximumSize(fitted);
        return area;
    }

    private static JPanel brandPanel() {
        final JPanel brand = new JPanel(new BorderLayout());
        brand.setBackground(Theme.INK);
        brand.setPreferredSize(new Dimension(BRAND_WIDTH, 0));
        brand.setBorder(BorderFactory.createEmptyBorder(48, BRAND_PADDING, 40, BRAND_PADDING));

        brand.add(logo(), BorderLayout.NORTH);

        final JPanel pitch = formColumn();
        pitch.add(Box.createVerticalGlue());
        pitch.add(text("Split the bill,", Font.BOLD, 40f, Color.WHITE));
        pitch.add(text("not the friendship.", Font.BOLD | Font.ITALIC, 40f, Theme.LIME));
        pitch.add(Box.createVerticalStrut(24));
        pitch.add(wrapped("Track shared costs with roommates, trips and friends. "
                + "Everyone logs what they paid, Splitter does the math.",
                Font.PLAIN, 15f, Theme.BRAND_TEXT, BRAND_WIDTH - 2 * BRAND_PADDING - 40));
        pitch.add(Box.createVerticalGlue());
        brand.add(pitch, BorderLayout.CENTER);

        final JPanel perks = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        perks.setOpaque(false);
        for (String perk : new String[] {"Join with a code", "Multi-currency", "Settle in one tap"}) {
            perks.add(text("\u2713 " + perk + "     ", Font.PLAIN, 12f, Theme.BRAND_TEXT));
        }
        brand.add(perks, BorderLayout.SOUTH);
        return brand;
    }

    private static JPanel logo() {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        final RoundedPanel mark = new RoundedPanel(new BorderLayout(), 14, Theme.LIME, null);
        mark.setPreferredSize(new Dimension(34, 34));
        final JLabel letter = text("S", Font.BOLD, 17f, Theme.INK);
        letter.setHorizontalAlignment(SwingConstants.CENTER);
        mark.add(letter, BorderLayout.CENTER);
        row.add(mark);
        row.add(text("Splitter", Font.BOLD, 19f, Color.WHITE));
        return row;
    }
}
