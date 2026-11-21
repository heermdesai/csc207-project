package entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A way of dividing an expense between people.
 * Equal split is the only one for now; a percentage split could be added later
 * as another class without changing Expense.
 */
public interface SplitStrategy {
    Map<String, BigDecimal> split(BigDecimal total, List<String> participantEmails);
}
