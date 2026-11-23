package interface_adapter.view_balances;

import use_case.view_balances.ViewBalancesInputBoundary;
import use_case.view_balances.ViewBalancesInputData;

/**
 * Controller for the View Balances use case.
 */
public class ViewBalancesController {

    private final ViewBalancesInputBoundary viewBalancesUseCaseInteractor;

    public ViewBalancesController(ViewBalancesInputBoundary viewBalancesUseCaseInteractor) {
        this.viewBalancesUseCaseInteractor = viewBalancesUseCaseInteractor;
    }

    /**
     * Executes the View Balances use case with the provided group ID.
     * @param groupId
     */
    public void execute(String groupId) {
        viewBalancesUseCaseInteractor.execute(new ViewBalancesInputData(groupId));
    }
}
