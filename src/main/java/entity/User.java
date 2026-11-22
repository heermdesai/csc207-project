package entity;

import java.util.Objects;

/**
 * A user account, identified by its email address.
 */
public class User {

    private final String email;
    private final String password;

    public User(String email, String password) {
         this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        return email.equals(((User) other).email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
