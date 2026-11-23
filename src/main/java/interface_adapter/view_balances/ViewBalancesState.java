package interface_adapter.view_balances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Display state for the balances screen.
 */
public class ViewBalancesState {

    private List<String> balanceLines = new ArrayList<>();
    private boolean allSettledUp;
    private String error;

    public List<String> getBalanceLines() {
        return Collections.unmodifiableList(balanceLines);
    }

    public void setBalanceLines(List<String> balanceLines) {
        this.balanceLines = new ArrayList<>(balanceLines);
    }

    public boolean isAllSettledUp() {
        return allSettledUp;
    }

    public void setAllSettledUp(boolean allSettledUp) {
        this.allSettledUp = allSettledUp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
