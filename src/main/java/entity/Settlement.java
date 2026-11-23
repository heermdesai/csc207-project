package entity;

import java.time.LocalDate;

/**
 * A recorded payment from one member to another. Immutable.
 */
public class Settlement {

    private final String id;
    private final User from;
    private final User to;
    // private final Money amount;
    private final LocalDate date;

    public Settlement(String id, User from, User to, LocalDate date) {
        // if (amount.signum() <= 0) {
        //     throw new IllegalArgumentException("Settlement amount must be positive.");
        // }
        if (from.equals(to)) {
            throw new IllegalArgumentException("A member cannot pay themselves.");
        }
        this.id = id;
        this.from = from;
        this.to = to;
        //this.amount = amount;
        this.date = date;
    }

    public String getId() { return id; }
    public User getFrom() { return from; }
    public User getTo() { return to; }
    // public Money getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}
