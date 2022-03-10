package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImplementation;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoImplementation;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.*;

public class Twitch implements StreamingPlatform {


    private UserService userService;
    private Set<Content> twitchContent;

    public Twitch(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException();
        }
        this.userService = userService;
        this.twitchContent = new HashSet<Content>();
    }

    private User checkForUserExceptions(String username, String mode)
            throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<String, User> users = new HashMap<>();
        users = userService.getUsers();
        if (!users.containsKey(username)) {
            throw new UserNotFoundException();
        }

        User user = users.get(username);
        if (mode.equals("SKIP_CHECKS")) {
            return user;
        }
        if (mode.equals("START") || mode.equals("WATCH")) {
            if (user.getStatus().equals(UserStatus.STREAMING)) {
                throw new UserStreamingException();
            }
        } else if (mode.equals("END")) {
            if (user.getStatus().equals(UserStatus.OFFLINE)) {
                throw new UserStreamingException();
            }
        }

        return user;
    }

    @Override
    public Stream startStream(String username, String title, Category category)
            throws UserNotFoundException, UserStreamingException {

        if (title == null || category == null || title.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User streamer = checkForUserExceptions(username, "START");
        streamer.setStatus(UserStatus.STREAMING);

        Stream stream = new StreamImplementation(streamer, category, title);
        twitchContent.add(stream);

        return stream;
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (stream == null) {
            throw new IllegalArgumentException();
        }

        User streamer = checkForUserExceptions(username, "END");
        streamer.setStatus(UserStatus.OFFLINE);
        if (streamer instanceof UserAccount streamerAcc) {
            streamerAcc.endStream(stream);
        }
        twitchContent.remove(stream);

        Video vidFromStream = new VideoImplementation((StreamImplementation) stream);
        twitchContent.add(vidFromStream);

        return vidFromStream;
    }

    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (content == null) {
            throw new IllegalArgumentException();
        }
        User user = checkForUserExceptions(username, "WATCH");
        //boolean foundContent = false;
        for (var i : twitchContent) {
            if (i.equals(content)) {
                content.startWatching(user);
                //foundContent = true;
                break;
            }
        }
    }

    @Override
    public User getMostWatchedStreamer() {

        Map<String, User> userMap = userService.getUsers();
        if (userMap.isEmpty()) {
            return null;
        }

        User mostWatchedUser = null;
        int mostViews = Integer.MIN_VALUE;
        for (var u : userMap.entrySet()) {
            if (u.getValue() instanceof UserAccount streamerAcc) {
                if (streamerAcc.getViews() > mostViews) {
                    mostWatchedUser = streamerAcc;
                    mostViews = streamerAcc.getViews();
                }
            }
        }
        return mostWatchedUser;
    }

    @Override
    public Content getMostWatchedContent() {
        if (twitchContent.isEmpty()) {
            return null;
        }

        Content mostWatchedContent = null;
        int mostViews = Integer.MIN_VALUE;

        for (var contentIterator : twitchContent) {
            if (contentIterator instanceof AbstractContent ac) {
                if (ac.getNumberOfViews() > mostViews) {
                    mostWatchedContent = ac;
                    mostViews = ac.getNumberOfViews();
                }
            }
        }

        return mostWatchedContent;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        User user = checkForUserExceptions(username, "SKIP_CHECKS");

        Content mostWatchedContentFromStreamer = null;
        int mostViews = Integer.MIN_VALUE;

        for (var contentIterator : twitchContent) {
            if (contentIterator instanceof AbstractContent ac) {
                if (ac.getNumberOfViews() > mostViews && ac.getMetadata().getUser().equals(user)) {
                    mostWatchedContentFromStreamer = ac;
                    mostViews = ac.getNumberOfViews();
                }
            }
        }

        return mostWatchedContentFromStreamer;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        User user = checkForUserExceptions(username, "SKIP_CHECKS");

        List<Category> userMostWatchedCategories = new ArrayList<>();
        if (user instanceof UserAccount ua) {
            userMostWatchedCategories = ua.getMostWatchedCategories();
        }

        return List.copyOf(userMostWatchedCategories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Twitch)) return false;

        Twitch twitch = (Twitch) o;

        if (!userService.equals(twitch.userService)) return false;
        return twitchContent.equals(twitch.twitchContent);
    }

    @Override
    public int hashCode() {
        int result = userService.hashCode();
        result = 31 * result + twitchContent.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Twitch{" +
                "userService=" + userService +
                ", twitchContent=" + twitchContent +
                '}';
    }
}
