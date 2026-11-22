package use_case.login;

import entity.User;

import java.util.Locale;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {

    static final String INCORRECT_CREDENTIALS = "Incorrect email or password.";

    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String email = loginInputData.getEmail() == null
                ? "" : loginInputData.getEmail().trim().toLowerCase(Locale.ROOT);
        final String password = loginInputData.getPassword();
        if (email.isEmpty() || password == null || password.isEmpty()) {
            loginPresenter.prepareFailView("Please enter your email and password.");
        }
        else if (!userDataAccessObject.existsByEmail(email)
                || !password.equals(userDataAccessObject.get(email).getPassword())) {
            loginPresenter.prepareFailView(INCORRECT_CREDENTIALS);
        }
        else {
            final User user = userDataAccessObject.get(email);
            userDataAccessObject.setCurrentEmail(user.getEmail());
            loginPresenter.prepareSuccessView(new LoginOutputData(user.getEmail(), false));
        }
    }
}
