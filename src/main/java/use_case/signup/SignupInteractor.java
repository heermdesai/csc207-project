package use_case.signup;

import entity.User;
import entity.UserFactory;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {

    static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern EMAIL_FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        final String email = normalize(signupInputData.getEmail());
        final String password = signupInputData.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            userPresenter.prepareFailView("Please fill in every field.");
        }
        else if (!EMAIL_FORMAT.matcher(email).matches()) {
            userPresenter.prepareFailView("Enter a valid email address.");
        }
        else if (password.length() < MIN_PASSWORD_LENGTH) {
            userPresenter.prepareFailView("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        else if (!password.equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else if (userDataAccessObject.existsByEmail(email)) {
            userPresenter.prepareFailView("An account with this email already exists.");
        }
        else {
            final User user = userFactory.create(email, password);
            userDataAccessObject.save(user);
            userPresenter.prepareSuccessView(new SignupOutputData(user.getEmail(), false));
        }
    }

    static String normalize(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
