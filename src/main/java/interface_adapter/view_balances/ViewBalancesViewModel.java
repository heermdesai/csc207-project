package interface_adapter.view_balances;

import interface_adapter.ViewModel;

/**
 * The View Model for the Balances View.
 */
public class ViewBalancesViewModel extends ViewModel<ViewBalancesState> {

    public ViewBalancesViewModel() {
        super("balances");
        setState(new ViewBalancesState());
    }
}
