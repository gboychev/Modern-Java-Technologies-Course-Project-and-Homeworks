package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.exception.UsernameNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Storage {

    private final int maxOperationsBeforeSaveFileIsUpdated = 1;
    private String fileName = "userData.txt";

    private Map<Integer, String> socketHashCodeUsername;
    private Map<String, UserDataEntry> users;
    private Map<String, Group> groups; //how to change group name?
    private Map<String, Boolean> usernameLoggedIn;

    private int opCount;

    public Storage() {
        users = new HashMap<>();
        usernameLoggedIn = new HashMap<>();
        socketHashCodeUsername = new HashMap<>();
        groups = new HashMap<>();
        opCount = 0;
        loadDataFromFile();
    }

    public String getUserPassword(String username) {
        if (users.get(username) == null) {
            return null;
        }
        return users.get(username).getPassword();
    }

    public String storeUser(String username, String password) {
        users.put(username, new UserDataEntry(username, password));
        usernameLoggedIn.put(username, false);
        incrementOpCount();
        return "Successfully registered user " + username + ".";
    }

    public String logUserIn(String username, Integer hash) {
        usernameLoggedIn.put(username, true);
        socketHashCodeUsername.put(hash, username);
        return "Successfully logged in as user " + username + ".";
    }

    public boolean userLoginStatus(String username) {
        return usernameLoggedIn.get(username);
    }

    public String getCallingUsername(Integer hash) {
        return socketHashCodeUsername.get(hash);
    }

    public String addFriendToUser(String callingUser, String addedUser) {
        String result = addBalanceWithOtherUser(callingUser, addedUser);
        users.get(callingUser).getFriends().add(addedUser);
        users.get(addedUser).getFriends().add(callingUser);
        return result + " and created a friendship.";
    }

    public String addBalanceWithOtherUser(String callingUser, String addedUser) {
        if (!(users.containsKey(addedUser) && users.containsKey(callingUser))) {
            throw new UsernameNotFoundException();
        }
        if (!users.get(callingUser).getMoneyOwedToOtherUsers().containsKey(addedUser)) {
            users.get(callingUser).getMoneyOwedToOtherUsers().put(addedUser, 0.0);
            users.get(addedUser).getMoneyOwedToOtherUsers().put(callingUser, 0.0);
        } else {
            return "User already is a friend. Nothing was altered.";
        }
        incrementOpCount();
        return callingUser + " successfully created a balance with user " + addedUser;
    }


    public Map<String, Group> getGroups() {
        return groups;
    }

    public Map<Integer, String> getSocketHashCodeUsername() {
        return socketHashCodeUsername;
    }

    public Map<String, UserDataEntry> getUsers() {
        return users;
    }

    public Map<String, Boolean> getUsernameLoggedIn() {
        return usernameLoggedIn;
    }

    public void incrementOpCount() {
        opCount++;
        if (opCount == maxOperationsBeforeSaveFileIsUpdated) {
            opCount = 0;
            saveDataToFile();
        }
    }

    public void saveDataToFile() {
        try {
            new File(fileName).createNewFile();
            BufferedWriter bfw = Files.newBufferedWriter(Path.of(fileName), StandardOpenOption.TRUNCATE_EXISTING);
            for (var u : users.entrySet()) {
                StringBuffer sb = new StringBuffer();
                AtomicBoolean deleteLastTwoSymbols = new AtomicBoolean(false);
                sb.append(u.getValue().getUsername()).append("; ").append(u.getValue().getPassword()).append("; ");
                u.getValue().getMoneyOwedToOtherUsers().keySet().forEach(k -> sb.append(k).append(", "));
                sb.append("; ");
                u.getValue().getMoneyOwedToOtherUsers().values().forEach(v -> sb.append(v).append(", "));
                sb.append("; ");
                u.getValue().getFriends().forEach(f -> {
                    sb.append(f).append(", ");
                    deleteLastTwoSymbols.set(true);
                });
                sb.append("; ");
                u.getValue().getGroups().forEach(g -> {
                    sb.append(g).append(", ");
                    deleteLastTwoSymbols.set(true);
                });
                sb.append("; ");
                if (deleteLastTwoSymbols.get()) {
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                }
                bfw.write(new String(sb));
                bfw.write("\n");
                bfw.flush();
            }
        } catch (IOException e) {
            writeToErrorFile(e);
        }
    }

    public void loadDataFromFile() {
        if (!Files.exists(Path.of(fileName))) {
            return;
        }
        try (BufferedReader bf = Files.newBufferedReader(Path.of(fileName))) {
            ArrayList<UserDataEntry> uda = new ArrayList<>(bf.lines().map(UserDataEntry::of).toList());
            for (var u : uda) {
                users.put(u.getUsername(), u);
                usernameLoggedIn.put(u.getUsername(), false);
                u.getGroups().forEach(g -> {
                    if (groups.get(g) == null) {
                        groups.put(g, new Group(g, u.getUsername()));
                    } else {
                        Group group = groups.get(g);
                        group.addGroupMember(group.getMember(), u.getUsername());
                    }
                });
            }
        } catch (IOException e) {
            writeToErrorFile(e);
        }
    }

    private void writeToErrorFile(Exception e) {
        try {
            new File("errorLog.txt").createNewFile();
            BufferedWriter bfw = Files.newBufferedWriter(Path.of("errorLog.txt"),
                    StandardOpenOption.TRUNCATE_EXISTING);
            PrintWriter pw = new PrintWriter(bfw);
            e.printStackTrace(pw);
            pw.close();
        } catch (IOException ex) {
            //forgot or didn't know at all what to do
        }
    }

    public void setFileName(String newFileName) {
        fileName = newFileName;
    }

}

