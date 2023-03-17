package bg.sofia.uni.fmi.mjt.splitwise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Group {
    private String name;
    private Set<String> members;
    private Map<String, Map<String, Double>> owedInGroup;

    public Group(String name, String creatorName) {
        this.name = name;
        members = new HashSet<>();
        members.add(creatorName);
        owedInGroup = new HashMap<>();
        owedInGroup.put(creatorName, new HashMap<String, Double>());
    }

    public void addGroupMember(String userDoingTheAdding, String addedUser) {
        if (members.contains(userDoingTheAdding)) {
            Map<String, Double> owed = new HashMap<String, Double>();
            members.forEach(m -> this.owedInGroup.get(m).put(addedUser, 0.0));
            members.forEach(m -> owed.put(m, 0.0));
            members.add(addedUser);
            owedInGroup.put(addedUser, owed);
        }
    }

    public Map<String, Map<String, Double>> getOwedInGroup() {
        return owedInGroup;
    }

    public Set<String> getMembers() {
        return members;
    }

    public String getMember() {
        for (String s : members) {
            return s;
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String requestingUser, String newName) {
        if (members.contains(requestingUser)) {
            name = newName;
        }
    }
}
