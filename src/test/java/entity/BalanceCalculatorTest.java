package entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceCalculatorTest {

    private static final String ANA = "ana@mail.com";
    private static final String BEN = "ben@mail.com";
    private static final String CAL = "cal@mail.com";

    private static Expense expense(String payer, String amount, List<String> participants) {
        return new Expense("e1", "g1", "Dinner", payer, participants, new BigDecimal(amount),
                "CAD", LocalDate.of(2026, 1, 1), new EqualSplitStrategy());
    }

    private static void assertBalance(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual), "expected " + expected + " but was " + actual);
    }

    @Test
    void membersWithNoActivityStillAppearAtZero() {
        Map<String, BigDecimal> result = new BalanceCalculator().calculate(List.of(ANA, BEN), List.of(), List.of());
        assertEquals(2, result.size());
        assertBalance("0", result.get(ANA));
        assertBalance("0", result.get(BEN));
    }

    @Test
    void payerIsOwedAndOthersOwe() {
        Map<String, BigDecimal> result = new BalanceCalculator().calculate(
                List.of(ANA, BEN, CAL), List.of(expense(ANA, "30.00", List.of(ANA, BEN, CAL))), List.of());
        assertBalance("20.00", result.get(ANA));
        assertBalance("-10.00", result.get(BEN));
        assertBalance("-10.00", result.get(CAL));
    }

    @Test
    void settlementReducesWhatWasOwed() {
        Settlement benPaysAna = new Settlement("s1", "g1", BEN, ANA, new BigDecimal("10.00"), LocalDate.of(2026, 1, 2));
        Map<String, BigDecimal> result = new BalanceCalculator().calculate(
                List.of(ANA, BEN, CAL), List.of(expense(ANA, "30.00", List.of(ANA, BEN, CAL))), List.of(benPaysAna));
        assertBalance("10.00", result.get(ANA));
        assertBalance("0.00", result.get(BEN));
        assertBalance("-10.00", result.get(CAL));
    }

    @Test
    void balancesAlwaysSumToZero() {
        Settlement s = new Settlement("s1", "g1", CAL, BEN, new BigDecimal("3.33"), LocalDate.of(2026, 1, 2));
        Map<String, BigDecimal> result = new BalanceCalculator().calculate(
                List.of(ANA, BEN, CAL), List.of(expense(BEN, "10.00", List.of(ANA, BEN, CAL))), List.of(s));
        BigDecimal sum = result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertBalance("0", sum);
    }

    @Test
    void personNoLongerInMemberListDoesNotCrash() {
        // CAL paid but is not in the member list (e.g. left the group)
        Map<String, BigDecimal> result = new BalanceCalculator().calculate(
                List.of(ANA, BEN), List.of(expense(CAL, "20.00", List.of(ANA, BEN))), List.of());
        assertBalance("20.00", result.get(CAL));
        assertBalance("-10.00", result.get(ANA));
    }

    @Test
    void settlementRejectsBadInput() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Settlement("s", "g1", ANA, ANA, BigDecimal.ONE, LocalDate.now()));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Settlement("s", "g1", ANA, BEN, BigDecimal.ZERO, LocalDate.now()));
    }
}
