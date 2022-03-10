package bg.sofia.uni.fmi.mjt.splitwise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserDataEntry {
    private String username;
    private String password;
    private Set<String> friends;
    private Map<String, Double> moneyOwedToOtherUsers;
    private Set<String> groups;

    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final int OWED_USER_INDEX = 2;
    private static final int OWED_USER_BALANCE_INDEX = 3;
    private static final int FRIENDS_INDEX = 4;
    private static final int GROUPS_INDEX = 5;

    public UserDataEntry(String username, String password) {
        this.username = username;
        this.password = password;
        moneyOwedToOtherUsers = new HashMap<>();
        groups = new HashSet<>();
        friends = new HashSet<>();
    }

    public static UserDataEntry of(String line) {
        String[] tokens = line.split(";");
        String username = tokens[USERNAME_INDEX].trim();
        String password = tokens[PASSWORD_INDEX].trim();

        UserDataEntry result = new UserDataEntry(username, password);
        String owedUsers = tokens[OWED_USER_INDEX];
        String owedAmounts = tokens[OWED_USER_BALANCE_INDEX];
        String friends = tokens[FRIENDS_INDEX];
        String groups = tokens[GROUPS_INDEX];

        if (friends.length() > 1) {
            String[] friendsTokens = friends.split(", | ");
            for (String s : friendsTokens) {
                if (!s.isEmpty()) {
                    result.getFriends().add(s);
                }
            }
        }
        if (groups.length() > 1) {
            String[] groupTokens = groups.split(", | ");
            for (String s : groupTokens) {
                if (!s.isEmpty()) {
                    result.getGroups().add(s);
                }
            }
        }

        if (owedUsers.length() > 1 && owedAmounts.length() > 1) {
            String[] owedUsernamesTokens = owedUsers.split(", | ");
            String[] owedAmountTokens = owedAmounts.split(", | ");
            for (int i = 0; i < owedAmountTokens.length; i++) {
                if (!owedAmountTokens[i].isEmpty() && !owedAmountTokens[i].isEmpty()) {
                    result.getMoneyOwedToOtherUsers()
                            .put(owedUsernamesTokens[i], Double.parseDouble(owedAmountTokens[i]));
                }
            }
        }

        return result;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Double> getMoneyOwedToOtherUsers() {
        return moneyOwedToOtherUsers;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMoneyOwedToOtherUsers(Map<String, Double> moneyOwedToOtherUsers) {
        this.moneyOwedToOtherUsers = moneyOwedToOtherUsers;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public Set<String> getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDataEntry)) return false;

        UserDataEntry userData = (UserDataEntry) o;

        return username.equals(userData.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
