package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void usersWithSameEmailAreEqual() {
        final User first = new User("saky@mail.com", "password123");
        final User second = new User("saky@mail.com", "different456");
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void usersWithDifferentEmailsAreNotEqual() {
        assertNotEquals(new User("a@mail.com", "password123"), new User("b@mail.com", "password123"));
    }
}
