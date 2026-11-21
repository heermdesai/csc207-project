package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * One shared cost in a group. People are identified by email, like in Group.
 */
public class Expense {
    private final String id;
    private final String groupId;
    private final String description;
    private final String payerEmail;
    private final List<String> participantEmails;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDate date;
    private final SplitStrategy splitStrategy;

    public Expense(String id, String groupId, String description, String payerEmail,
                   List<String> participantEmails, BigDecimal amount, String currency,
                   LocalDate date, SplitStrategy splitStrategy) {
        // Business rules: an expense can never be created without these
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (participantEmails.isEmpty()) {
            throw new IllegalArgumentException("An expense needs at least one participant.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.id = id;
        this.groupId = groupId;
        this.description = description;
        this.payerEmail = payerEmail;
        this.participantEmails = new ArrayList<>(participantEmails);
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.splitStrategy = splitStrategy;
    }

    /** What each person owes for this expense. */
    public Map<String, BigDecimal> getShares() {
        return splitStrategy.split(amount, participantEmails);
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public List<String> getParticipantEmails() {
        return new ArrayList<>(participantEmails);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDate() {
        return date;
    }
}
