package use_case.signup;

import data_access.InMemoryUserDataAccessObject;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SignupInteractorTest {

    private void expectFailure(InMemoryUserDataAccessObject repository, SignupInputData input, String expected) {
        final SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals(expected, error);
            }

            @Override
            public void switchToLoginView() {
                fail("Switching views is unexpected.");
            }
        };
        new SignupInteractor(repository, presenter, new UserFactory()).execute(input);
    }

    private void expectFailure(SignupInputData input, String expected) {
        expectFailure(new InMemoryUserDataAccessObject(), input, expected);
    }

    @Test
    void successSavesNormalizedEmail() {
        final InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
        final SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals("saky@mail.utoronto.ca", outputData.getEmail());
                assertTrue(repository.existsByEmail("saky@mail.utoronto.ca"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }

            @Override
            public void switchToLoginView() {
                fail("Switching views is unexpected.");
            }
        };
        new SignupInteractor(repository, presenter, new UserFactory())
                .execute(new SignupInputData("  Saky@Mail.UToronto.ca ", "password123", "password123"));
    }

    @Test
    void emptyFieldsAreRejected() {
        expectFailure(new SignupInputData("", "", ""), "Please fill in every field.");
    }

    @Test
    void invalidEmailIsRejected() {
        expectFailure(new SignupInputData("saky", "password123", "password123"), "Enter a valid email address.");
        expectFailure(new SignupInputData("saky@mail", "password123", "password123"), "Enter a valid email address.");
    }

    @Test
    void shortPasswordIsRejected() {
        expectFailure(new SignupInputData("saky@mail.com", "short", "short"),
                "Password must be at least 8 characters.");
    }

    @Test
    void mismatchedPasswordsAreRejected() {
        expectFailure(new SignupInputData("saky@mail.com", "password123", "password124"), "Passwords don't match.");
    }

    @Test
    void duplicateEmailIgnoresCase() {
        final InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
        repository.save(new UserFactory().create("saky@mail.com", "password123"));
        expectFailure(repository, new SignupInputData("SAKY@mail.com", "password456", "password456"),
                "An account with this email already exists.");
    }

    @Test
    void nothingSavedOnFailure() {
        final InMemoryUserDataAccessObject repository = new InMemoryUserDataAccessObject();
        expectFailure(repository, new SignupInputData("bad", "password123", "password123"), "Enter a valid email address.");
        assertFalse(repository.existsByEmail("bad"));
    }
}
