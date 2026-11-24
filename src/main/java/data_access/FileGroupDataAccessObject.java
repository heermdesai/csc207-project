package data_access;

import entity.Group;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.create_group.CreateGroupDataAccessInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores groups in a JSON file so they are still there after the app closes.
 */
public class FileGroupDataAccessObject implements CreateGroupDataAccessInterface {
    private final Path file;
    private final Map<String, Group> groupsById = new LinkedHashMap<>();

    public FileGroupDataAccessObject(String filePath) {
        this.file = Path.of(filePath);
        if (Files.exists(file)) {
            load();
        }
    }

    @Override
    public boolean existsByJoinCode(String joinCode) {
        return getByJoinCode(joinCode) != null;
    }

    @Override
    public void save(Group group) {
        groupsById.put(group.getId(), group);
        writeFile();
    }

    /** Returns the group with this id, or null if there isn't one. */
    public Group getGroup(String groupId) {
        return groupsById.get(groupId);
    }

    /** Returns the group with this join code, or null if there isn't one. */
    public Group getByJoinCode(String joinCode) {
        for (Group group : groupsById.values()) {
            if (group.getJoinCode().equals(joinCode)) {
                return group;
            }
        }
        return null;
    }

    private void load() {
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            JSONArray groupsJson = new JSONArray(text);
            for (int i = 0; i < groupsJson.length(); i++) {
                JSONObject groupJson = groupsJson.getJSONObject(i);
                JSONArray membersJson = groupJson.getJSONArray("members");

                // The first member is the creator; the rest are added after
                Group group = new Group(groupJson.getString("id"), groupJson.getString("name"),
                        groupJson.getString("joinCode"), groupJson.getString("baseCurrency"),
                        membersJson.getString(0));
                for (int j = 1; j < membersJson.length(); j++) {
                    group.addMember(membersJson.getString(j));
                }
                groupsById.put(group.getId(), group);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not read " + file, ex);
        }
    }

    private void writeFile() {
        JSONArray groupsJson = new JSONArray();
        for (Group group : groupsById.values()) {
            JSONObject groupJson = new JSONObject();
            groupJson.put("id", group.getId());
            groupJson.put("name", group.getName());
            groupJson.put("joinCode", group.getJoinCode());
            groupJson.put("baseCurrency", group.getBaseCurrency());

            JSONArray membersJson = new JSONArray();
            List<String> members = group.getMemberEmails();
            for (String email : members) {
                membersJson.put(email);
            }
            groupJson.put("members", membersJson);
            groupsJson.put(groupJson);
        }
        try {
            Files.writeString(file, groupsJson.toString(2), StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            throw new RuntimeException("Could not write " + file, ex);
        }
    }
}