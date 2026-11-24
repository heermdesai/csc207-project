package data_access;

import entity.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileGroupDataAccessObjectTest {

    @TempDir
    Path tempDir;

    private String groupsFile() {
        return tempDir.resolve("groups.json").toString();
    }

    @Test
    void savedGroupCanBeFoundByJoinCode() {
        FileGroupDataAccessObject dao = new FileGroupDataAccessObject(groupsFile());
        dao.save(new Group("g1", "Roommates", "ABC234", "CAD", "a@mail.com"));

        assertTrue(dao.existsByJoinCode("ABC234"));
        assertEquals("Roommates", dao.getByJoinCode("ABC234").getName());
    }

    @Test
    void unknownJoinCodeDoesNotExist() {
        FileGroupDataAccessObject dao = new FileGroupDataAccessObject(groupsFile());

        assertEquals(false, dao.existsByJoinCode("ZZZ999"));
        assertNull(dao.getByJoinCode("ZZZ999"));
    }

    @Test
    void groupsAreStillThereAfterReopening() {
        FileGroupDataAccessObject first = new FileGroupDataAccessObject(groupsFile());
        Group group = new Group("g1", "Trip", "XYZ789", "USD", "a@mail.com");
        group.addMember("b@mail.com");
        first.save(group);

        // A new DAO reading the same file, like restarting the app
        FileGroupDataAccessObject reopened = new FileGroupDataAccessObject(groupsFile());
        Group loaded = reopened.getGroup("g1");

        assertEquals("Trip", loaded.getName());
        assertEquals("XYZ789", loaded.getJoinCode());
        assertEquals("USD", loaded.getBaseCurrency());
        assertEquals(List.of("a@mail.com", "b@mail.com"), loaded.getMemberEmails());
    }
}