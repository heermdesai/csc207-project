package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements PropertyChangeListener {

    private final String viewName = "log in";
    private final LoginViewModel loginViewModel;

    private final JTextField emailInputField = new JTextField();
    private final JPasswordField passwordInputField = new JPasswordField();
    private final JLabel errorLabel = AuthLayout.errorLabel();
    private LoginController loginController;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        final PrimaryButton logIn = new PrimaryButton("Log in");
        logIn.addActionListener(evt -> submit());
        passwordInputField.addActionListener(evt -> submit());

        final JPanel form = AuthLayout.formColumn();
        form.add(AuthLayout.tabs(true, () -> { }, () -> loginController.switchToSignupView()));
        form.add(Box.createVerticalStrut(28));
        form.add(AuthLayout.heading("Welcome back", "Log in to see your groups and balances."));
        form.add(Box.createVerticalStrut(24));
        form.add(AuthLayout.field("Email", new InputField(emailInputField), null));
        form.add(Box.createVerticalStrut(18));
        form.add(AuthLayout.field("Password", new InputField(passwordInputField), null));
        form.add(Box.createVerticalStrut(14));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(14));
        form.add(logIn);
        form.add(Box.createVerticalStrut(20));
        form.add(AuthLayout.note("New here? Switch to Sign up. Got an invite code? Enter it after you log in."));

        AuthLayout.build(this, form);

        emailInputField.getDocument().addDocumentListener(new TextChangeListener(() -> {
            final LoginState state = loginViewModel.getState();
            state.setEmail(emailInputField.getText().trim());
            loginViewModel.setState(state);
        }));
        passwordInputField.getDocument().addDocumentListener(new TextChangeListener(() -> {
            final LoginState state = loginViewModel.getState();
            state.setPassword(new String(passwordInputField.getPassword()));
            loginViewModel.setState(state);
        }));
    }

    private void submit() {
        final LoginState state = loginViewModel.getState();
        AuthLayout.showError(errorLabel, null);
        loginController.execute(state.getEmail(), state.getPassword());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();
        if (!emailInputField.getText().trim().equals(state.getEmail())) {
            emailInputField.setText(state.getEmail());
        }
        if (state.getPassword().isEmpty() && passwordInputField.getPassword().length > 0) {
            passwordInputField.setText("");
        }
        AuthLayout.showError(errorLabel, state.getLoginError());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
