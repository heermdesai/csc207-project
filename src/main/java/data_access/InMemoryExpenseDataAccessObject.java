package data_access;

import entity.Expense;
import entity.Group;
import use_case.add_expense.AddExpenseDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps groups and expenses in memory (lost when the app closes).
 * Temporary until the team's file storage for groups exists.
 */
public class InMemoryExpenseDataAccessObject implements AddExpenseDataAccessInterface {
    private final Map<String, Group> groups = new HashMap<>();
    private final List<Expense> expenses = new ArrayList<>();

    /** Adds a group. Create Group will do this in the real app. */
    public void addGroup(Group group) {
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    @Override
    public void saveExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
