package data_access;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
                                                     LoginUserDataAccessInterface,
                                                     LogoutUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean existsByEmail(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
    }

    @Override
    public User get(String email) {
        return users.get(email);
    }

    private String currentEmail;

    @Override
    public String getCurrentEmail() {
        return currentEmail;
    }

    @Override
    public void setCurrentEmail(String email) {
        this.currentEmail = email;
    }
}
