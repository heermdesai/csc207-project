package view;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

class InputField extends RoundedPanel {

    private static final int HEIGHT = 44;

    private final JTextField field;

    InputField(JTextField field) {
        super(new BorderLayout(), 20, Theme.SURFACE, Theme.BORDER);
        this.field = field;
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setOpaque(false);
        field.setFont(Theme.font(Font.PLAIN, 14f));
        field.setForeground(Theme.INK);
        field.setCaretColor(Theme.INK);
        setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        add(field, BorderLayout.CENTER);
        setAlignmentX(LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(400, HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorderColor(Theme.INK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorderColor(Theme.BORDER);
            }
        });
    }

    JTextField getField() {
        return field;
    }
}
