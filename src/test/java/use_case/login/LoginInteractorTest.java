package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class LoginInteractorTest {

    @Test
    void successRecordsCurrentUser() {
        final InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
        userRepository.save(new UserFactory().create("Paul", "password"));

        final LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals("Paul", outputData.getUsername());
                assertEquals("Paul", userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        new LoginInteractor(userRepository, successPresenter).execute(new LoginInputData("Paul", "password"));
    }

    @Test
    void wrongPasswordDoesNotLogIn() {
        final InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
        userRepository.save(new UserFactory().create("Paul", "password"));

        final LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"Paul\".", error);
                assertNull(userRepository.getCurrentUsername());
            }
        };

        new LoginInteractor(userRepository, failurePresenter).execute(new LoginInputData("Paul", "wrong"));
    }
}
