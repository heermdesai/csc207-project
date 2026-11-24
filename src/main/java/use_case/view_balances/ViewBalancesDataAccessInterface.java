package use_case.view_balances;

import entity.Expense;
import entity.Group;
import entity.Settlement;

import java.util.List;

/**
 * Data needed by the View Balances use case.
 */
public interface ViewBalancesDataAccessInterface {

    /**
     * Returns the group with the specified ID.
     * @param groupId the ID of the group to retrieve
     * @return the group with the specified ID, or null if it does not exist
     */
    Group getGroup(String groupId);

    /**
     * Returns every expense recorded for the group.
     * @param groupId the ID of the group
     * @return the group's expenses (empty if there are none)
     */
    List<Expense> getExpenses(String groupId);

    /**
     * Returns every settlement recorded for the group.
     * @param groupId the ID of the group
     * @return the group's settlements (empty if there are none)
     */
    List<Settlement> getSettlements(String groupId);

    /**
     * Returns the email of the current user.
     * @return the current user's email, or null if nobody is logged in
     */
    String getCurrentEmail();

}
