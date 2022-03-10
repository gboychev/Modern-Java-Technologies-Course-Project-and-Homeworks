package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImplementation;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class VideoImplementation extends AbstractContent {
    public VideoImplementation() {
        super();
    }
    public VideoImplementation(Category cat, User user, String name) {
        super(user, cat, name);
    }

    public VideoImplementation(StreamImplementation stream) {
        super(stream.getMetadata().getUser(), stream.getMetadata().getCategory(), stream.getMetadata().getName());
        duration = stream.getDuration();
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "Video{" +
                super.toString() +
                "}";
    }
}
