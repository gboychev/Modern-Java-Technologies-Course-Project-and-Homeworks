package bg.sofia.uni.fmi.mjt.spotify.playable;

public class Audio extends AbstractPlayable implements Playable {

    public Audio() {
        super();
    }

    public Audio(String title, String artist, int year, double duration) {
        super(title, artist, year, duration);
    }

    @Override
    public String play() {
        this.plays++;
        return ("Currently playing audio content: " + this.title);
    }
}
