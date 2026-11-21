package use_case.add_expense;

import data_access.InMemoryExpenseDataAccessObject;
import entity.EqualSplitStrategy;
import entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddExpenseInteractorTest {
    private static final String ALICE = "alice@mail.com";
    private static final String BOB = "bob@mail.com";
    private static final String CHEN = "chen@mail.com";

    private InMemoryExpenseDataAccessObject dataAccessObject;
    private FakePresenter presenter;
    private AddExpenseInteractor interactor;

    /** Remembers what the interactor told it, so the test can check. */
    private static class FakePresenter implements AddExpenseOutputBoundary {
        private AddExpenseOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(AddExpenseOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @BeforeEach
    void setUp() {
        dataAccessObject = new InMemoryExpenseDataAccessObject();
        Group group = new Group("g1", "Roommates", "ABC123", "CAD", ALICE);
        group.addMember(BOB);
        group.addMember(CHEN);
        dataAccessObject.addGroup(group);

        presenter = new FakePresenter();
        interactor = new AddExpenseInteractor(dataAccessObject, presenter, new EqualSplitStrategy());
    }

    private void addExpense(String description, String amount, String payer, List<String> participants) {
        interactor.execute(new AddExpenseInputData("g1", description, amount, payer, participants));
    }

    @Test
    void validExpenseIsSavedAndSplitEqually() {
        addExpense("Dinner", "60", ALICE, List.of(ALICE, BOB, CHEN));

        assertNull(presenter.errorMessage);
        assertEquals(new BigDecimal("60.00"), presenter.successData.getAmount());
        assertEquals(new BigDecimal("20.00"), presenter.successData.getShares().get(BOB));
        assertEquals(1, dataAccessObject.getExpenses().size());
    }

    @Test
    void leftoverCentGoesToFirstPerson() {
        addExpense("Pizza", "10", BOB, List.of(ALICE, BOB, CHEN));

        assertEquals(new BigDecimal("3.34"), presenter.successData.getShares().get(ALICE));
        assertEquals(new BigDecimal("3.33"), presenter.successData.getShares().get(BOB));
        assertEquals(new BigDecimal("3.33"), presenter.successData.getShares().get(CHEN));
    }

    @Test
    void emptyDescriptionFails() {
        addExpense("  ", "10", ALICE, List.of(ALICE));
        assertEquals("Description cannot be empty.", presenter.errorMessage);
    }

    @Test
    void emptyAmountFails() {
        addExpense("Dinner", "", ALICE, List.of(ALICE));
        assertEquals("Amount cannot be empty.", presenter.errorMessage);
    }

    @Test
    void amountThatIsNotANumberFails() {
        addExpense("Dinner", "ten", ALICE, List.of(ALICE));
        assertEquals("Amount must be a number.", presenter.errorMessage);
    }

    @Test
    void zeroOrNegativeAmountFails() {
        addExpense("Dinner", "0", ALICE, List.of(ALICE));
        assertEquals("Amount must be greater than zero.", presenter.errorMessage);

        addExpense("Dinner", "-5", ALICE, List.of(ALICE));
        assertEquals("Amount must be greater than zero.", presenter.errorMessage);
    }

    @Test
    void noPayerFails() {
        addExpense("Dinner", "10", null, List.of(ALICE));
        assertEquals("Please choose who paid.", presenter.errorMessage);
    }

    @Test
    void noParticipantsFails() {
        addExpense("Dinner", "10", ALICE, List.of());
        assertEquals("Please choose at least one person involved.", presenter.errorMessage);
    }

    @Test
    void nonMemberIsRejectedAndNothingIsSaved() {
        addExpense("Dinner", "10", "mallory@mail.com", List.of(ALICE));
        assertEquals("mallory@mail.com is not a member of this group.", presenter.errorMessage);

        addExpense("Dinner", "10", ALICE, List.of(ALICE, "mallory@mail.com"));
        assertEquals("mallory@mail.com is not a member of this group.", presenter.errorMessage);

        assertTrue(dataAccessObject.getExpenses().isEmpty());
    }

    @Test
    void unknownGroupFails() {
        interactor.execute(new AddExpenseInputData("nope", "Dinner", "10", ALICE, List.of(ALICE)));
        assertEquals("Group not found.", presenter.errorMessage);
    }
}
