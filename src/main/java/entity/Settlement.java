package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A recorded payment from one member to another. Immutable.
 *  People are identified by email, like in Group and Expense.
 */
public class Settlement {

    private final String id;
    private final String groupId;
    private final String fromEmail;
    private final String toEmail;
    private final BigDecimal amount;
    private final LocalDate date;

    public Settlement(String id, String groupId, String fromEmail, String toEmail,
                      BigDecimal amount, LocalDate date) {
        if (fromEmail.equals(toEmail)) {
            throw new IllegalArgumentException("A member cannot pay themselves.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Settlement amount must be positive.");
        }
        this.id = id;
        this.groupId = groupId;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.amount = amount;
        this.date = date;
    }

    public String getId() { return id; }
    public String getGroupId() { return groupId; }
    public String getFromEmail() { return fromEmail; }
    public String getToEmail() { return toEmail; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}
