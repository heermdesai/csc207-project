package use_case.view_balances;

/**
 * Output boundary for the View Balances Use Case.
 */
public interface ViewBalancesOutputBoundary {

    /**
     * Prepares the success view for the View Balances Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ViewBalancesOutputData outputData);

    /**
     * Prepares the failure view for the View Balances Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
