package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Splits the total equally. Any leftover cent goes to the first person,
 * so $10.00 between 3 people is 3.34 + 3.33 + 3.33.
 */
public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public Map<String, BigDecimal> split(BigDecimal total, List<String> participantEmails) {
        BigDecimal numberOfPeople = new BigDecimal(participantEmails.size());

        // 10.00 / 3 = 3.33 (round down to cents)
        BigDecimal share = total.divide(numberOfPeople, 2, RoundingMode.DOWN);

        // 10.00 - (3.33 * 3) = 0.01 left over
        BigDecimal leftover = total.subtract(share.multiply(numberOfPeople));

        Map<String, BigDecimal> shares = new HashMap<>();
        for (String email : participantEmails) {
            shares.put(email, share);
        }

        // the first person pays the leftover cent(s)
        String firstPerson = participantEmails.get(0);
        shares.put(firstPerson, share.add(leftover));

        return shares;
    }
}
