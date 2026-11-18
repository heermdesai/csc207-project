package use_case.logout;

import data_access.InMemoryUserDataAccessObject;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class LogoutInteractorTest {

    @Test
    void successLogsOutCurrentUser() {
        final InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();
        userRepository.save(new UserFactory().create("Paul", "password"));
        userRepository.setCurrentUsername("Paul");

        final LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                assertEquals("Paul", outputData.getUsername());
                assertNull(userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        new LogoutInteractor(userRepository, successPresenter).execute();
    }

    @Test
    void failureWhenNobodyIsLoggedIn() {
        final InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        final LogoutOutputBoundary failurePresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("No user is currently logged in.", errorMessage);
            }
        };

        new LogoutInteractor(userRepository, failurePresenter).execute();
    }
}
