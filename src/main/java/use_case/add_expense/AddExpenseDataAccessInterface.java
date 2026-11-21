package use_case.add_expense;

import entity.Expense;
import entity.Group;

/**
 * What Add Expense needs from storage. Whatever class stores groups
 * (in memory, a file, ...) implements these two methods.
 */
public interface AddExpenseDataAccessInterface {
    /** Returns the group with this id, or null if it doesn't exist. */
    Group getGroup(String groupId);

    void saveExpense(Expense expense);
}
