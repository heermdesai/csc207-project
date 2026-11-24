package use_case.view_balances;

/**
 * Input data for the View Balances Use Case.
 */
public class ViewBalancesInputData {

    private final String groupId;

    public ViewBalancesInputData(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }
}
