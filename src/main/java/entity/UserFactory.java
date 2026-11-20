package entity;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    public User create(String email, String password) {
        return new User(email, password);
    }
}
