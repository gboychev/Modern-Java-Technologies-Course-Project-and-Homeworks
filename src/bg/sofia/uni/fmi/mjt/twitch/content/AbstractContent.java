package bg.sofia.uni.fmi.mjt.twitch.content;

import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamImplementation;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserAccount;

import java.time.Duration;

public abstract class AbstractContent implements Stream, Video {

    protected static int contentCounter = 0;
    protected int contentID;
    protected Metadata metadata;
    protected Duration duration;
    protected int views;

    public AbstractContent() {
        this.metadata = null;
        this.duration = null;
        contentID = contentCounter;
        contentCounter++;
        views = 0;
    }

    public AbstractContent(User user, Category cat, String name) {
        metadata = new Metadata(user, cat, name);
        duration = Duration.ZERO;
        contentID = contentCounter;
        contentCounter++;
        views = 0;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public void startWatching(User user) {
        if (user instanceof UserAccount userAcc) {
            userAcc.startWatching(this);
        }
        views++;
        if (this.getMetadata().getUser() instanceof UserAccount streamerAcc) {
            streamerAcc.receiveView();
        }
    }

    @Override
    public void stopWatching(User user) {
        if (this instanceof StreamImplementation) {
            views--;
        }
    }

    @Override
    public int getNumberOfViews() {
        return views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractContent)) return false;

        AbstractContent that = (AbstractContent) o;

        return contentID == that.contentID;
    }

    @Override
    public int hashCode() {
        return contentID;
    }

    @Override
    public String toString() {
        return "AbstractContent{" +
                "contentID=" + contentID +
                ", metadata=" + metadata +
                ", duration=" + duration +
                ", views=" + views +
                '}';
    }
}

