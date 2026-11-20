package use_case.logout;

public class LogoutInteractor implements LogoutInputBoundary {

    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessObject,
                            LogoutOutputBoundary logoutPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.logoutPresenter = logoutPresenter;
    }

    @Override
    public void execute() {
        final String username = userDataAccessObject.getCurrentUsername();
        if (username == null) {
            logoutPresenter.prepareFailView("No user is currently logged in.");
        }
        else {
            userDataAccessObject.setCurrentUsername(null);
            logoutPresenter.prepareSuccessView(new LogoutOutputData(username));
        }
    }
}
