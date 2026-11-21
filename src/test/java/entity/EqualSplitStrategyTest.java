package entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EqualSplitStrategyTest {

    @Test
    void evenSplit() {
        Map<String, BigDecimal> shares = new EqualSplitStrategy()
                .split(new BigDecimal("30.00"), List.of("a@mail.com", "b@mail.com"));
        assertEquals(new BigDecimal("15.00"), shares.get("a@mail.com"));
        assertEquals(new BigDecimal("15.00"), shares.get("b@mail.com"));
    }

    @Test
    void sharesAlwaysAddUpToTotal() {
        BigDecimal total = new BigDecimal("0.05");
        Map<String, BigDecimal> shares = new EqualSplitStrategy()
                .split(total, List.of("a@mail.com", "b@mail.com", "c@mail.com"));

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal share : shares.values()) {
            sum = sum.add(share);
        }
        assertEquals(total, sum);
    }
}
