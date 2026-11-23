package use_case.create_group;

import entity.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateGroupInteractorTest {
    private static final String ANAHITA = "anahita@mail.com";

    private FakeGroupStorage storage;
    private FakePresenter presenter;
    private CreateGroupInteractor interactor;

    /** Pretend storage: keeps saved groups in a list. */
    private static class FakeGroupStorage implements CreateGroupDataAccessInterface {
        private final List<Group> savedGroups = new ArrayList<>();
        private int codesToRejectFirst = 0;
        private int existsCalls = 0;

        @Override
        public boolean existsByJoinCode(String joinCode) {
            existsCalls++;
            return existsCalls <= codesToRejectFirst;
        }

        @Override
        public void save(Group group) {
            savedGroups.add(group);
        }
    }

    /** Pretend presenter: remembers what the interactor told it. */
    private static class FakePresenter implements CreateGroupOutputBoundary {
        private CreateGroupOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(CreateGroupOutputData outputData) {
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @BeforeEach
    void setUp() {
        storage = new FakeGroupStorage();
        presenter = new FakePresenter();
        interactor = new CreateGroupInteractor(storage, presenter);
    }

    private void createGroup(String name) {
        interactor.execute(new CreateGroupInputData(name, ANAHITA, "CAD"));
    }

    @Test
    void validGroupIsSavedWithCreatorAsMember() {
        createGroup("Trip to Montreal");

        assertNull(presenter.errorMessage);
        assertEquals(1, storage.savedGroups.size());
        Group saved = storage.savedGroups.get(0);
        assertEquals("Trip to Montreal", saved.getName());
        assertEquals(List.of(ANAHITA), saved.getMemberEmails());
        assertEquals("CAD", saved.getBaseCurrency());
    }

    @Test
    void successViewGetsNameAndJoinCode() {
        createGroup("Roommates");

        assertEquals("Roommates", presenter.successData.getGroupName());
        assertEquals(storage.savedGroups.get(0).getJoinCode(), presenter.successData.getJoinCode());
    }

    @Test
    void joinCodeIsSixAllowedCharacters() {
        createGroup("Roommates");

        String code = presenter.successData.getJoinCode();
        assertEquals(6, code.length());
        assertTrue(code.matches("[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{6}"));
    }

    @Test
    void nameIsTrimmed() {
        createGroup("   Roommates   ");

        assertEquals("Roommates", storage.savedGroups.get(0).getName());
    }

    @Test
    void takenJoinCodeIsReplaced() {
        storage.codesToRejectFirst = 2;

        createGroup("Roommates");

        assertEquals(3, storage.existsCalls);
        assertEquals(1, storage.savedGroups.size());
    }

    @Test
    void emptyNameFailsAndNothingIsSaved() {
        createGroup("   ");

        assertEquals("Group name cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertTrue(storage.savedGroups.isEmpty());
    }

    @Test
    void nullNameFails() {
        createGroup(null);

        assertEquals("Group name cannot be empty.", presenter.errorMessage);
        assertTrue(storage.savedGroups.isEmpty());
    }

    @Test
    void twoGroupsGetDifferentIds() {
        createGroup("Group A");
        createGroup("Group B");

        assertNotEquals(storage.savedGroups.get(0).getId(), storage.savedGroups.get(1).getId());
    }
}
