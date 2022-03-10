package bg.sofia.uni.fmi.mjt.twitch.user.service;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.HashMap;
import java.util.Map;

public class UserServiceMap implements UserService {

    Map<String, User> userMap;

    public UserServiceMap() {
        userMap = new HashMap<>();
    }

    public void addUser(User user) {
        userMap.put(user.getName(), user);
    }

    @Override
    public Map<String, User> getUsers() {
        return userMap;
    }

}
