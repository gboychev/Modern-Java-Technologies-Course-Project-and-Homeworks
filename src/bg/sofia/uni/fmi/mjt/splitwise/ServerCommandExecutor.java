package bg.sofia.uni.fmi.mjt.splitwise;

import bg.sofia.uni.fmi.mjt.splitwise.exception.OperationNotLegalException;

import java.util.Map;
import java.util.Set;

public class ServerCommandExecutor {

    private Storage storage;

    public ServerCommandExecutor(Storage storage) {
        this.storage = storage;
    }

    public String execute(String[] command) {
        return switch (command[0]) {
            case "add-friend" -> addFriend(command[1], Integer.parseInt(command[2]));
            case "create-group" -> createGroup(command[1], Integer.parseInt(command[2]));
            case "split" -> split(command[1], Integer.parseInt(command[2]));
            case "split-group" -> splitGroup(command[1], Integer.parseInt(command[2]));
            case "receive-payment" -> receivePayment(command[1], Integer.parseInt(command[2]));
            case "login" -> login(command[1], Integer.parseInt(command[2]));
            case "register" -> register(command[1]);
            case "get-status" -> getStatus(Integer.parseInt(command[2]));
            default -> "Unknown command. Available commands at the moment are: " +
                    "add-friend, create-group, split, split-group, receive-payment, login, register";
        };
    }

    private String register(String args) {
        String[] argArray = verifyArgumentCount(args, 2);
        if (argArray.length == 0) {
            return "Incorrect number of arguments. Must be 2 - register <username> <password>.";
        }
        final String username = argArray[0];
        final String password = argArray[1];

        if (storage.getUserPassword(username) != null) {
            return "Username already taken. Please choose a different username.";
        }

        return storage.storeUser(username, password);
    }

    private String login(String args, Integer hash) {
        String[] argArray = verifyArgumentCount(args, 2);
        if (argArray.length == 0) {
            return "Incorrect number of arguments. Must be 2 - login <username> <password>.";
        }
        final String username = argArray[0];
        final String password = argArray[1];

        if (storage.getUserPassword(username) == null) {
            return "An account with this username does not exist.";
        }

        if (!password.equals(storage.getUserPassword(username))) {
            return "Wrong password provided.";
        }

        if (storage.userLoginStatus(username)) {
            return "There is already a session running for this account. Login not allowed at the moment.";
        }

        return storage.logUserIn(username, hash);
    }

    //add-friend <username>
    public String addFriend(String args, Integer hash) {
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        String[] argArray = verifyArgumentCount(args, 1);
        String otherUser = argArray[0];
        String username = storage.getCallingUsername(hash);
        if (username == null || !storage.userLoginStatus(username)) {
            throw new OperationNotLegalException();
        }
        return storage.addFriendToUser(username, otherUser);
    }

    //create-group <group_name> <username> <username> ... <username>
    public String createGroup(String args, Integer hash) {
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        String[] argArray = args.split(" ");
        if (argArray.length < 1) {
            return "Group should have at least a name. The full format of the command is " +
                    "'create-group <group_name> <username> <username> ... <username>'";
        }
        String groupName = argArray[0];
        Map<String, Group> groups = storage.getGroups();
        Map<String, UserDataEntry> users = storage.getUsers();
        Map<Integer, String> hashes = storage.getSocketHashCodeUsername();
        if (groups.get(groupName) == null) {
            Group createdGroup = new Group(groupName, hashes.get(hash));
            users.get(hashes.get(hash)).getGroups().add(groupName);
            for (int i = argArray.length - 1; i > 0; i--) {
                createdGroup.addGroupMember(hashes.get(hash), argArray[i]);
                users.get(argArray[i]).getGroups().add(groupName);
                storage.addBalanceWithOtherUser(hashes.get(hash), argArray[i]);
                for (int j = i - 1; j > 0; j--) {
                    storage.addBalanceWithOtherUser(argArray[j], argArray[i]);
                }
            }
            groups.put(groupName, createdGroup);
        } else {
            return "A group with this name already exists. Did nothing.";
        }
        storage.incrementOpCount();
        return "Successfully created group.";
    }

    //split <amount> <username-of-a-friend> <reason_for_payment>
    private String split(String args, Integer hash) {
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        final int argCount = 2;
        String[] argArray = verifyArgumentCount(args, argCount);
        if (argArray.length == 0) {
            return "Incorrect number of arguments. Must be at least 2. Full command: " +
                    "'split <amount> <username-of-a-friend> <reason_for_payment>'";
        }

        double amount = Double.parseDouble(argArray[0]);
        String user = storage.getCallingUsername(hash);
        String friendUsername = argArray[1];

        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < argArray.length; i++) {
            reason.append(argArray[i]).append(" ");
        }
        if (!storage.getUsers().get(user).getFriends().contains(friendUsername)) {
            return friendUsername + " is not in the friends list of " + user;
        }

        double currentBalance = storage.getUsers().get(user).getMoneyOwedToOtherUsers().get(friendUsername);
        storage.getUsers().get(user).getMoneyOwedToOtherUsers().put(friendUsername, currentBalance + amount / 2);
        storage.getUsers().get(friendUsername).getMoneyOwedToOtherUsers().put(user, -currentBalance - amount / 2);

        storage.incrementOpCount();
        return "Updated balance for the relationship of users " + user + " and " + friendUsername + ".";
    }

    //split-group <amount> <group_name> <reason_for_payment>
    private String splitGroup(String args, Integer hash) {
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        final int argCount = 2;
        String[] argArray = verifyArgumentCount(args, argCount);
        if (argArray.length == 0) {
            return "Incorrect number of arguments. Must be at least 2. Full command: " +
                    "split-group <amount> <group_name> <reason_for_payment>";
        }
        double amount = Double.parseDouble(argArray[0]);

        String callingUser = storage.getCallingUsername(hash);
        String groupName = argArray[1];

        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < argArray.length; i++) {
            reason.append(argArray[i]).append(" ");
        }

        Set<String> groupMembers = storage.getGroups().get(groupName).getMembers();
        int groupMembersCount = groupMembers.size();
        for (var member : groupMembers) {
            if (member.equals(callingUser)) {
                groupMembers.stream().filter(g -> !g.equals(member)).forEach(u -> {
                    double currentBalance = storage.getUsers().get(member).getMoneyOwedToOtherUsers().get(u);
                    storage.getUsers().get(member).getMoneyOwedToOtherUsers()
                            .put(u, currentBalance + amount / groupMembersCount);
                });
            } else {
                double currentBalance = storage.getUsers().get(member).getMoneyOwedToOtherUsers().get(callingUser);
                storage.getUsers().get(member).getMoneyOwedToOtherUsers()
                        .put(callingUser, currentBalance - amount / groupMembersCount);
            }
        }
        storage.incrementOpCount();
        return "Completed split between group.";
    }

    //payed <amount> <username>
    private String receivePayment(String args, Integer hash) { //verify inputs what if i input receive negative payment
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        String[] argArray = verifyArgumentCount(args, 2);
        if (argArray.length == 0) {
            return "Incorrect number of arguments. Must be at least 2. Full command: 'payed <amount> <username>'";
        }
        double amount = Double.parseDouble(argArray[0]);
        String username = argArray[1];
        String callingUsername = storage.getCallingUsername(hash);
        double currentBalance = storage.getUsers().get(callingUsername).getMoneyOwedToOtherUsers().get(username);
        storage.getUsers().get(callingUsername).getMoneyOwedToOtherUsers().put(username, currentBalance - amount);
        storage.incrementOpCount();

        return "Successfully accepted payment.";
    }

    //get-status
    private String getStatus(Integer hash) {
        if (storage.getCallingUsername(hash) == null) {
            return "You are not logged in. You do not have access to this command unless you are logged in.";
        }
        StringBuffer sb = new StringBuffer();
        if (storage.getUsers().get(storage.getCallingUsername(hash)).getMoneyOwedToOtherUsers().isEmpty()) {
            return "You have no debt to pay or to collect from anyone.";
        }
        storage.getUsers().get(storage.getCallingUsername(hash)).getMoneyOwedToOtherUsers()
                .forEach((key, value) -> {
                    if (Double.compare(value, 0.0) > 0) {
                        sb.append(key).append(" owes you ").append(value).append("lv.\n");
                    } else if (Double.compare(value, 0.0) < 0) {
                        sb.append("You owe ").append(key).append(" ").append(value).append("lv.\n");
                    } else {
                        sb.append("Your financial relationship with ").append(key).append(" is balanced: 0.00lv\n");
                    }
                });
        return new String(sb);
    }
//sled twa test - done, sled twa failowete- done, sled twa test-done,
// sled twa izpip, sled twa test, sled twa paralelno, sled twa test
// sled twa test cases

    private String[] verifyArgumentCount(String args, int requirement) {
        String[] argArray = args.split(" ");
        if (argArray.length < requirement) {
            return new String[0];
        }
        return argArray;
    }

    public static String[] extractCommandAndArgs(byte[] buff, int hash) {
        String message = new String(buff);
        String[] words = message.split(" |\\n|null");
        StringBuffer arguments = new StringBuffer();
        for (int i = 1; i < words.length - 1; i++) {
            arguments.append(words[i]).append(" ");
        }
        final int resultNum = 3;
        String[] result = new String[resultNum];

        result[0] = words[0];
        result[1] = new String(arguments);
        result[2] = String.valueOf(hash);

        return result;
    }
}
