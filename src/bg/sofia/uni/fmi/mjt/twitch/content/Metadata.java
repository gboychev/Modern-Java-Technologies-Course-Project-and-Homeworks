package bg.sofia.uni.fmi.mjt.twitch.content;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

public class Metadata {

    private String name;
    private Category category;
    private User streamer;

    public Metadata(User user, Category cat, String name) {
        this.name = name;
        this.category = cat;
        this.streamer = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public User getUser() {
        return streamer;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", streamer=" + streamer +
                '}';
    }
}
