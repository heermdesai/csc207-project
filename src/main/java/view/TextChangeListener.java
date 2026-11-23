package view;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class TextChangeListener implements DocumentListener {

    private final Runnable onChange;

    TextChangeListener(Runnable onChange) {
        this.onChange = onChange;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        onChange.run();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        onChange.run();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        onChange.run();
    }
}
