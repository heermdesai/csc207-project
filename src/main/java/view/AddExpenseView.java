package view;

import interface_adapter.add_expense.AddExpenseController;
import interface_adapter.add_expense.AddExpenseState;
import interface_adapter.add_expense.AddExpenseViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The Add Expense screen.
 * 1. When "Add Expense" is clicked, it reads the fields and calls the controller.
 * 2. When the view model changes, it redraws itself from the state.
 */
public class AddExpenseView extends JPanel implements PropertyChangeListener {
    private final AddExpenseViewModel addExpenseViewModel;
    private AddExpenseController addExpenseController;

    private final JTextField descriptionField = new JTextField(20);
    private final JTextField amountField = new JTextField(10);
    private final JComboBox<String> payerBox = new JComboBox<>();
    private final JPanel participantsPanel = new JPanel(new GridLayout(0, 1));
    private final List<JCheckBox> participantBoxes = new ArrayList<>();
    private final JLabel messageLabel = new JLabel(" ");
    private final DefaultListModel<String> expenseListModel = new DefaultListModel<>();
    private final JButton addButton = new JButton("Add Expense");

    // the members currently shown as checkboxes
    private List<String> shownMembers = new ArrayList<>();

    public AddExpenseView(AddExpenseViewModel addExpenseViewModel) {
        this.addExpenseViewModel = addExpenseViewModel;
        this.addExpenseViewModel.addPropertyChangeListener(this);

        addButton.addActionListener(event -> submit());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(labelled("Description:", descriptionField));
        form.add(labelled("Amount:", amountField));
        form.add(labelled("Paid by:", payerBox));
        participantsPanel.setBorder(BorderFactory.createTitledBorder("Split between"));
        form.add(participantsPanel);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.add(addButton);
        buttonRow.add(messageLabel);
        form.add(buttonRow);

        JScrollPane expenseList = new JScrollPane(new JList<>(expenseListModel));
        expenseList.setBorder(BorderFactory.createTitledBorder("Expenses"));

        setLayout(new BorderLayout());
        add(form, BorderLayout.NORTH);
        add(expenseList, BorderLayout.CENTER);
    }

    /** The name of this screen in the app's CardLayout. */
    public String getViewName() {
        return AddExpenseViewModel.VIEW_NAME;
    }

    public void setAddExpenseController(AddExpenseController addExpenseController) {
        this.addExpenseController = addExpenseController;
    }

    /** Runs when "Add Expense" is clicked. */
    private void submit() {
        List<String> participants = new ArrayList<>();
        for (JCheckBox box : participantBoxes) {
            if (box.isSelected()) {
                participants.add(box.getText());
            }
        }
        String payer = (String) payerBox.getSelectedItem();
        String groupId = addExpenseViewModel.getState().getGroupId();

        addExpenseController.execute(groupId, descriptionField.getText(), amountField.getText(),
                payer, participants);
    }

    /** Runs when the presenter updates the view model. */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        AddExpenseState state = (AddExpenseState) evt.getNewValue();

        // Rebuild the member choices only if the group's members changed,
        // so the user's ticked boxes aren't reset after an error.
        if (!state.getMemberEmails().equals(shownMembers)) {
            showMembers(state.getMemberEmails());
        }

        expenseListModel.clear();
        for (String row : state.getExpenseRows()) {
            expenseListModel.addElement(row);
        }

        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getErrorMessage());
        }
        else if (state.getSuccessMessage() != null) {
            messageLabel.setForeground(new Color(0, 128, 0));
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
            JCheckBox box = new JCheckBox(email, true);
            participantBoxes.add(box);
            participantsPanel.add(box);
        }
        participantsPanel.revalidate();
        participantsPanel.repaint();
    }

    private JPanel labelled(String label, Component field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(field);
        return panel;
    }
}
