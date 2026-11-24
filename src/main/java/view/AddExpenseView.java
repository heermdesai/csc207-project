package view;

import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.add_expense.AddExpenseState;
import interface_adapter.add_expense.AddExpenseViewModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The View for the Add Expense use case.
 */
public class AddExpenseView extends JPanel implements PropertyChangeListener {

    private static final Color SUCCESS = new Color(30, 120, 70);
    private static final int FORM_WIDTH = 380;
    private static final int FIELD_HEIGHT = 44;
    private static final int ROW_PADDING = 10;
    private static final int HTML_WIDTH_PERCENT = 75;

    private final AddExpenseViewModel addExpenseViewModel;
    private AddExpenseController addExpenseController;

    private final JTextField descriptionField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JComboBox<String> payerBox = new JComboBox<>();
    private final JPanel participantsPanel = new JPanel(new GridLayout(0, 1, 0, 6));
    private final List<JCheckBox> participantBoxes = new ArrayList<>();
    private final JLabel messageLabel = AuthLayout.text(" ", Font.PLAIN, 13f, Theme.MUTED);
    private final DefaultListModel<String> expenseListModel = new DefaultListModel<>();
    private final JLabel emptyExpenses = AuthLayout.text("No expenses yet.", Font.PLAIN, 13f, Theme.MUTED);

    private List<String> shownMembers = new ArrayList<>();

    public AddExpenseView(AddExpenseViewModel addExpenseViewModel) {
        this.addExpenseViewModel = addExpenseViewModel;
        this.addExpenseViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(24, 0));
        setBackground(Theme.CANVAS);
        setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));
        add(formCard(), BorderLayout.WEST);
        add(expensesCard(), BorderLayout.CENTER);
    }

    private JPanel formCard() {
        final PrimaryButton addButton = new PrimaryButton("Add expense");
        addButton.addActionListener(event -> submit());
        amountField.addActionListener(event -> submit());

        payerBox.setFont(Theme.font(Font.PLAIN, 14f));
        payerBox.setAlignmentX(LEFT_ALIGNMENT);
        payerBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));
        participantsPanel.setOpaque(false);
        participantsPanel.setAlignmentX(LEFT_ALIGNMENT);

        final JPanel form = AuthLayout.formColumn();
        form.add(AuthLayout.heading("Add expense", "Split equally between the people involved."));
        form.add(Box.createVerticalStrut(20));
        form.add(AuthLayout.field("Description", new InputField(descriptionField), null));
        form.add(Box.createVerticalStrut(14));
        form.add(AuthLayout.field("Amount", new InputField(amountField), null));
        form.add(Box.createVerticalStrut(14));
        form.add(labelled("Paid by", payerBox));
        form.add(Box.createVerticalStrut(14));
        form.add(labelled("Split between", participantsPanel));
        form.add(Box.createVerticalStrut(14));
        form.add(messageLabel);
        form.add(Box.createVerticalStrut(12));
        form.add(addButton);

        final RoundedPanel card = new RoundedPanel(new BorderLayout(), 24, Theme.SURFACE, Theme.BORDER);
        card.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        card.add(form, BorderLayout.NORTH);
        card.setPreferredSize(new Dimension(FORM_WIDTH + 48, 0));
        return card;
    }

    private JPanel expensesCard() {
        final JList<String> list = new JList<>(expenseListModel);
        list.setCellRenderer(expenseRowRenderer());
        list.setOpaque(false);
        list.setFocusable(false);
        list.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                list.setFixedCellHeight(1);
                list.setFixedCellHeight(-1);
            }
        });

        final JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        final JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(emptyExpenses, BorderLayout.NORTH);
        body.add(scroll, BorderLayout.CENTER);

        final RoundedPanel card = new RoundedPanel(new BorderLayout(0, 14), 24, Theme.SURFACE, Theme.BORDER);
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        card.add(AuthLayout.text("Expenses", Font.BOLD, 16f, Theme.INK), BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private static ListCellRenderer<String> expenseRowRenderer() {
        return (list, value, index, selected, focused) -> {
            final int width = Math.max((list.getWidth() - 8) * HTML_WIDTH_PERCENT / 100, 200);
            final int split = value.indexOf(": ");
            final String text = split < 0 ? escape(value)
                    : "<b>" + escape(value.substring(0, split)) + "</b><br>" + escape(value.substring(split + 2));
            final JLabel row = new JLabel("<html><body style='width:" + width + "px'>" + text + "</body></html>");
            row.setFont(Theme.font(Font.PLAIN, 13f));
            row.setForeground(Theme.INK);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(index == 0 ? 0 : 1, 0, 0, 0, Theme.BORDER),
                    BorderFactory.createEmptyBorder(ROW_PADDING, 0, ROW_PADDING, 0)));
            return row;
        };
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static JPanel labelled(String label, JComponent input) {
        final JPanel panel = AuthLayout.formColumn();
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(AuthLayout.text(label, Font.BOLD, 13f, Theme.INK));
        panel.add(Box.createVerticalStrut(8));
        panel.add(input);
        return panel;
    }

    /**
     * The name of this screen in the app's CardLayout.
     * @return the view name
     */
    public String getViewName() {
        return AddExpenseViewModel.VIEW_NAME;
    }

    public void setAddExpenseController(AddExpenseController addExpenseController) {
        this.addExpenseController = addExpenseController;
    }

    private void submit() {
        final List<String> participants = new ArrayList<>();
        for (JCheckBox box : participantBoxes) {
            if (box.isSelected()) {
                participants.add(box.getText());
            }
        }
        final String payer = (String) payerBox.getSelectedItem();
        final String groupId = addExpenseViewModel.getState().getGroupId();

        addExpenseController.execute(groupId, descriptionField.getText(), amountField.getText(),
                payer, participants);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AddExpenseState state = (AddExpenseState) evt.getNewValue();

        if (!state.getMemberEmails().equals(shownMembers)) {
            showMembers(state.getMemberEmails());
        }

        expenseListModel.clear();
        for (String row : state.getExpenseRows()) {
            expenseListModel.addElement(row);
        }
        emptyExpenses.setVisible(expenseListModel.isEmpty());

        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(Theme.ERROR);
            messageLabel.setText(state.getErrorMessage());
        }
        else if (state.getSuccessMessage() != null) {
            messageLabel.setForeground(SUCCESS);
            messageLabel.setText(state.getSuccessMessage());
            descriptionField.setText("");
            amountField.setText("");
        }
    }

    private void showMembers(List<String> memberEmails) {
        shownMembers = memberEmails;
        payerBox.removeAllItems();
        participantsPanel.removeAll();
        participantBoxes.clear();
        for (String email : memberEmails) {
            payerBox.addItem(email);
            final JCheckBox box = new JCheckBox(email, true);
            box.setFont(Theme.font(Font.PLAIN, 13f));
            box.setForeground(Theme.INK);
            box.setOpaque(false);
            participantBoxes.add(box);
            participantsPanel.add(box);
        }
        participantsPanel.revalidate();
        participantsPanel.repaint();
    }
}
