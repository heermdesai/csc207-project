package use_case.view_balances;

/**
 * Input boundary for viewing balances.
 */
public interface ViewBalancesInputBoundary {

    /**
     * Executes the use case for viewing balances.
     * @param viewBalancesInputData
     */
    void execute(ViewBalancesInputData viewBalancesInputData);
}
