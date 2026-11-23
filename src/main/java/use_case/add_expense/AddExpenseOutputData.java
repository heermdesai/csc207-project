package use_case.add_expense;

import java.math.BigDecimal;
import java.util.Map;

/**
 * What the presenter needs to show the new expense in the list.
 */
public class AddExpenseOutputData {
    private final String description;
    private final BigDecimal amount;
    private final String currency;
    private final String payerEmail;
    private final Map<String, BigDecimal> shares;

    public AddExpenseOutputData(String description, BigDecimal amount, String currency,
                                String payerEmail, Map<String, BigDecimal> shares) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.payerEmail = payerEmail;
        this.shares = shares;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public Map<String, BigDecimal> getShares() {
        return shares;
    }
}
