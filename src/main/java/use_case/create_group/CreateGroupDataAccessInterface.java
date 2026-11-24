package use_case.create_group;

import entity.Group;

/**
 * What Create Group needs from storage.
 */
public interface CreateGroupDataAccessInterface {
    boolean existsByJoinCode(String joinCode);

    void save(Group group);
}