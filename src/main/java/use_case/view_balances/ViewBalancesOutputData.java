package use_case.view_balances;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output Data for the View Balances Use Case.
 */
public class ViewBalancesOutputData {

    private final List<BalanceRow> balances;
    private final boolean allSettledUp;

    public ViewBalancesOutputData(List<BalanceRow> balances, boolean allSettledUp) {
        this.balances = Collections.unmodifiableList(new ArrayList<>(balances));
        this.allSettledUp = allSettledUp;
    }

    public List<BalanceRow> getBalances() {
        return balances;
    }

    public boolean isAllSettledUp() {
        return allSettledUp;
    }

    public static class BalanceRow {
        private final String memberEmail;
        private final BigDecimal amount;
        private final String currencyCode;

        public BalanceRow(String memberEmail, BigDecimal amount, String currencyCode) {
            this.memberEmail = memberEmail;
            this.amount = amount;
            this.currencyCode = currencyCode;
        }

        public String getMemberEmail() {
            return memberEmail;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }
    }
}
