package entity;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Works out each member's net balance, keyed by email.
 * Positive = the member is owed money. Negative = the member owes money.
 */
public class BalanceCalculator {

    /**
     * Calculates the net balance for each member of a group.
     * Every member starts at zero, so members with no expenses still appear.
     *
     * @param memberEmails the emails of the group's members
     * @param expenses the group's expenses
     * @param settlements the group's recorded settlements
     * @return a map of each member's email to their net balance
     */
    public Map<String, BigDecimal> calculate(List<String> memberEmails, List<Expense> expenses,
                                             List<Settlement> settlements) {
        final Map<String, BigDecimal> balances = new LinkedHashMap<>();
        for (String email : memberEmails) {
            balances.put(email, BigDecimal.ZERO);
        }

        for (Expense expense : expenses) {
            balances.merge(expense.getPayerEmail(), expense.getAmount(), BigDecimal::add);
            for (Map.Entry<String, BigDecimal> share : expense.getShares().entrySet()) {
                balances.merge(share.getKey(), share.getValue().negate(), BigDecimal::add);
            }
        }

        for (Settlement settlement : settlements) {
            balances.merge(settlement.getFromEmail(), settlement.getAmount(), BigDecimal::add);
            balances.merge(settlement.getToEmail(), settlement.getAmount().negate(), BigDecimal::add);
        }
        return balances;
    }
}
