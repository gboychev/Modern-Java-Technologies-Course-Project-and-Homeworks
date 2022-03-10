package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class StreamImplementation extends AbstractContent {

    protected final LocalDateTime startTime;

    public StreamImplementation() {
        super();
        startTime = null;
    }

    public StreamImplementation(User user, Category cat, String name) {
        super(user, cat, name);
        startTime = LocalDateTime.now();
    }

    @Override
    public Duration getDuration() {
        return Duration.between(startTime, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "StreamImplementation{" +
                super.toString() +
                ", startTime=" + startTime +
                '}';
    }
}
