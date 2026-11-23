package view;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeViewTest {

    @Test
    void greetingDependsOnTimeOfDay() {
        assertEquals("Good morning", HomeView.timeOfDayGreeting(LocalTime.of(9, 0)));
        assertEquals("Good afternoon", HomeView.timeOfDayGreeting(LocalTime.of(12, 0)));
        assertEquals("Good evening", HomeView.timeOfDayGreeting(LocalTime.of(17, 30)));
    }

    @Test
    void displayNameUsesFirstPartOfEmail() {
        assertEquals("Saksham", HomeView.displayName("saksham.garg@mail.utoronto.ca"));
        assertEquals("Heer", HomeView.displayName("heer_desai@mail.com"));
        assertEquals("Anahita", HomeView.displayName("anahita@mail.com"));
    }

    @Test
    void displayNameFallsBackWhenEmailMissing() {
        assertEquals("there", HomeView.displayName(""));
        assertEquals("there", HomeView.displayName(null));
    }
}
