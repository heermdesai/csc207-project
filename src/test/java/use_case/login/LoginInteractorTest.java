package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class LoginInteractorTest {

    private static final String EMAIL = "paul@mail.utoronto.ca";

    private InMemoryUserDataAccessObject repositoryWithPaul() {
        final InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
        repository.save(new UserFactory().create(EMAIL, "password123"));
        return repository;
    }

    private void expectFailure(String email, String password, String expectedMessage) {
        final InMemoryUserDataAccessObject repository = repositoryWithPaul();
        final LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(expectedMessage, error);
                assertNull(repository.getCurrentEmail());
            }

            @Override
            public void switchToSignupView() {
                fail("Switching views is unexpected.");
            }
        };
        new LoginInteractor(repository, presenter).execute(new LoginInputData(email, password));
    }

    @Test
    void successRecordsCurrentUser() {
        final InMemoryUserDataAccessObject repository = repositoryWithPaul();
        final LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals(EMAIL, outputData.getEmail());
                assertEquals(EMAIL, repository.getCurrentEmail());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignupView() {
                fail("Switching views is unexpected.");
            }
        };
        new LoginInteractor(repository, presenter).execute(new LoginInputData(" Paul@Mail.UToronto.ca ", "password123"));
    }

    @Test
    void wrongPasswordGivesGenericError() {
        expectFailure(EMAIL, "wrongpass", LoginInteractor.INCORRECT_CREDENTIALS);
    }

    @Test
    void unknownEmailGivesSameGenericError() {
        expectFailure("nobody@mail.com", "password123", LoginInteractor.INCORRECT_CREDENTIALS);
    }

    @Test
    void emptyFieldsAreRejected() {
        expectFailure("", "", "Please enter your email and password.");
    }
}
