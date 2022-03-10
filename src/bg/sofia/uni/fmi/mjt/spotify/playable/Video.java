package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Video extends AbstractPlayable implements Playable {

    public Video() {
        super();
    }

    public Video(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        this.plays++;
        return ("Currently playing video content: " + this.title);
    }
}
