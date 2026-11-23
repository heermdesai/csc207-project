package use_case.view_balances;

import entity.Group;
import entity.User;

/**
 * Data needed by the View Balances use case.
 */
public interface ViewBalancesDataAccessInterface {
    
    /**
     * Returns the group with the specified ID.
     * @param groupId the ID of the group to retrieve
     * @return the group with the specified ID
     */
    Group getGroup(String groupId);

    /**
     * Returns the current user.
     * @return the current user
     */
    User getCurrentUser();
    
}
