package interface_adapter.view_balances;

import use_case.view_balances.ViewBalancesOutputBoundary;
import use_case.view_balances.ViewBalancesOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * The Presenter for the View Balances use case. It prepares the data for the view model and handles success and failure cases.
 */
public class ViewBalancesPresenter implements ViewBalancesOutputBoundary {

    private final ViewBalancesViewModel viewModel;

    public ViewBalancesPresenter(ViewBalancesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewBalancesOutputData outputData) {
        final ViewBalancesState state = new ViewBalancesState();
        final List<String> lines = new ArrayList<>();
        for (ViewBalancesOutputData.BalanceRow row : outputData.getBalances()) {
            lines.add(row.getMemberEmail() + ": " + row.getAmount().toPlainString()
                    + " " + row.getCurrencyCode());
        }
        state.setBalanceLines(lines);
        state.setAllSettledUp(outputData.isAllSettledUp());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final ViewBalancesState state = new ViewBalancesState();
        state.setError(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
