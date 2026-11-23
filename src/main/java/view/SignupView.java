package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup Use Case.
 */
public class SignupView extends JPanel implements PropertyChangeListener {

    private final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    private final JTextField emailInputField = new JTextField();
    private final JPasswordField passwordInputField = new JPasswordField();
    private final JPasswordField repeatPasswordInputField = new JPasswordField();
    private final JLabel errorLabel = AuthLayout.errorLabel();
    private SignupController signupController;

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        final PrimaryButton createAccount = new PrimaryButton("Create account");
        createAccount.addActionListener(evt -> submit());
        repeatPasswordInputField.addActionListener(evt -> submit());

        final JPanel form = AuthLayout.formColumn();
        form.add(AuthLayout.tabs(false, () -> signupController.switchToLoginView(), () -> { }));
        form.add(Box.createVerticalStrut(28));
        form.add(AuthLayout.heading("Create your account", "Use your email. Takes 20 seconds."));
        form.add(Box.createVerticalStrut(24));
        form.add(AuthLayout.field("Email", new InputField(emailInputField), null));
        form.add(Box.createVerticalStrut(18));
        form.add(AuthLayout.field("Password", new InputField(passwordInputField),
                AuthLayout.hint("At least 8 characters")));
        form.add(Box.createVerticalStrut(18));
        form.add(AuthLayout.field("Confirm password", new InputField(repeatPasswordInputField),
                AuthLayout.hint("Must match the password above")));
        form.add(Box.createVerticalStrut(14));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(14));
        form.add(createAccount);
        form.add(Box.createVerticalStrut(20));
        form.add(AuthLayout.note("Got an invite code? You can join the group right after signing up."));

        AuthLayout.build(this, form);
        addListeners();
    }

    private void addListeners() {
        emailInputField.getDocument().addDocumentListener(new TextChangeListener(() -> {
            final SignupState state = signupViewModel.getState();
            state.setEmail(emailInputField.getText().trim());
            signupViewModel.setState(state);
        }));
        passwordInputField.getDocument().addDocumentListener(new TextChangeListener(() -> {
            final SignupState state = signupViewModel.getState();
            state.setPassword(new String(passwordInputField.getPassword()));
            signupViewModel.setState(state);
        }));
        repeatPasswordInputField.getDocument().addDocumentListener(new TextChangeListener(() -> {
            final SignupState state = signupViewModel.getState();
            state.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
            signupViewModel.setState(state);
        }));
    }

    private void submit() {
        final SignupState state = signupViewModel.getState();
        AuthLayout.showError(errorLabel, null);
        signupController.execute(state.getEmail(), state.getPassword(), state.getRepeatPassword());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        AuthLayout.showError(errorLabel, state.getEmailError());
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }
}
