package view;

import interface_adapter.create_group.CreateGroupController;
import interface_adapter.create_group.CreateGroupState;
import interface_adapter.create_group.CreateGroupViewModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The form for creating a new group.
 */
public class CreateGroupView extends JPanel implements PropertyChangeListener {

    private static final String[] CURRENCIES = {"CAD", "USD", "EUR", "GBP", "INR"};
    private static final int WIDTH = 420;
    private static final int FIELD_HEIGHT = 44;

    private final CreateGroupViewModel createGroupViewModel;
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> currencyBox = new JComboBox<>(CURRENCIES);
    private final JLabel errorLabel = AuthLayout.errorLabel();
    private final JPanel successBox;
    private final JLabel joinCodeLabel = AuthLayout.text(" ", Font.BOLD, 26f, Theme.INK);
    private final JLabel successTitle = AuthLayout.text(" ", Font.BOLD, 14f, Theme.INK);
    private CreateGroupController createGroupController;
    private String creatorEmail = "";

    public CreateGroupView(CreateGroupViewModel createGroupViewModel) {
        this.createGroupViewModel = createGroupViewModel;
        createGroupViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setBackground(Theme.CANVAS);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        currencyBox.setFont(Theme.font(Font.PLAIN, 14f));
        currencyBox.setAlignmentX(LEFT_ALIGNMENT);
        currencyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIELD_HEIGHT));

        final PrimaryButton create = new PrimaryButton("Create group");
        create.addActionListener(evt -> submit());
        nameField.addActionListener(evt -> submit());

        successBox = success();
        successBox.setVisible(false);

        final JPanel form = AuthLayout.formColumn();
        form.add(AuthLayout.heading("New group", "You'll get a join code to share with friends."));
        form.add(Box.createVerticalStrut(22));
        form.add(AuthLayout.field("Group name", new InputField(nameField), null));
        form.add(Box.createVerticalStrut(16));
        form.add(labelled("Currency", currencyBox,
                AuthLayout.hint("All balances in this group are kept in this currency.")));
        form.add(Box.createVerticalStrut(14));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(14));
        form.add(create);
        form.add(Box.createVerticalStrut(18));
        form.add(successBox);
        add(form, BorderLayout.NORTH);
        setPreferredSize(new Dimension(WIDTH + 64, 470));
    }

    private static JPanel labelled(String label, JComponent input, JLabel hint) {
        final JPanel panel = AuthLayout.formColumn();
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(AuthLayout.text(label, Font.BOLD, 13f, Theme.INK));
        panel.add(Box.createVerticalStrut(8));
        panel.add(input);
        panel.add(Box.createVerticalStrut(6));
        panel.add(hint);
        return panel;
    }

    private JPanel success() {
        final RoundedPanel box = new RoundedPanel(new BorderLayout(0, 6), 24, Theme.LIME_SOFT, null);
        box.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        box.add(successTitle, BorderLayout.NORTH);
        box.add(joinCodeLabel, BorderLayout.CENTER);
        box.add(AuthLayout.text("Share this code so friends can join.", Font.PLAIN, 12f, Theme.MUTED),
                BorderLayout.SOUTH);
        box.setAlignmentX(LEFT_ALIGNMENT);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        return box;
    }

    private void submit() {
        AuthLayout.showError(errorLabel, null);
        createGroupController.execute(nameField.getText(), creatorEmail, (String) currencyBox.getSelectedItem());
    }

    public void prepare(String email) {
        this.creatorEmail = email;
        nameField.setText("");
        currencyBox.setSelectedIndex(0);
        AuthLayout.showError(errorLabel, null);
        successBox.setVisible(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CreateGroupState state = createGroupViewModel.getState();
        AuthLayout.showError(errorLabel, state.getErrorMessage());
        final boolean created = state.getErrorMessage() == null && !state.getJoinCode().isEmpty();
        if (created) {
            successTitle.setText("“" + state.getGroupName() + "” is ready");
            joinCodeLabel.setText(state.getJoinCode());
        }
        successBox.setVisible(created);
        revalidate();
        repaint();
    }

    public String getViewName() {
        return createGroupViewModel.getViewName();
    }

    public void setCreateGroupController(CreateGroupController createGroupController) {
        this.createGroupController = createGroupController;
    }
}
