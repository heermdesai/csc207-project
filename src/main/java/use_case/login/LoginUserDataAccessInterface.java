package use_case.login;

import entity.User;

/**
 * DAO for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given email exists.
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);

    /**
     * Returns the user with the given email.
     * @param email the email to look up
     * @return the user with the given email
     */
    User get(String email);

    /**
     * Records which user is currently logged in.
     * @param email the email of the logged-in user, or null if nobody is logged in
     */
    void setCurrentEmail(String email);

}
