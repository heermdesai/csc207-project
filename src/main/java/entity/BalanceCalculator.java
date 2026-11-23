package entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Works out each member's net balance.
 * Positive = the member is owed money. Negative = the member owes money.
 *
 */
public class BalanceCalculator {

    private final Group group;

    public BalanceCalculator(Group group) {
        this.group = group;
    }

    /***
     * Calculates the net balance for each member of the group.
     * @param expenses 
     * @param settlements
     * @return A map of each member to their net balance.
     */
    // public Map<User, Money> calculate(List<Expense> expenses, List<Settlement> settlements) {
    //     final Map<User, Money> net = new LinkedHashMap<>();

    //     for (Expense expense : expenses) {
    //         net.merge(expense.getPayer(), expense.getAmount(), Money::add);
    //         for (Map.Entry<User, Money> share : expense.getShares().entrySet()) {
    //             net.merge(share.getKey(), share.getValue().negate(), Money::add);
    //         }
    //     }

    //     for (Settlement settlement : settlements) {
    //         net.merge(settlement.getFrom(), settlement.getAmount(), Money::add);
    //         net.merge(settlement.getTo(), settlement.getAmount().negate(), Money::add);
    //     }
    //     return net;
    // }

    public Map<User, Money> calculate() {
        return calculate(group.getExpenses(), group.getSettlements());
    }
}