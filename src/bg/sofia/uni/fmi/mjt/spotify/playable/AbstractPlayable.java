package bg.sofia.uni.fmi.mjt.spotify.playable;

import java.util.Objects;

public abstract class AbstractPlayable implements Playable {

    protected int plays;
    protected int year;
    protected double duration;
    protected String title;
    protected String artist;

    public AbstractPlayable() {
        plays = 0;
        year = 0;
        duration = 0.00;
        title = "";
        artist = "";
    }

    public AbstractPlayable(String title, String artist, int year, double duration) {
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.duration = duration;
        plays = 0;
    }

    public abstract String play();

    @Override
    public int getTotalPlays() {
        return plays;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public double getDuration() {
        return duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(plays, year, duration, title, artist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPlayable)) return false;

        AbstractPlayable that = (AbstractPlayable) o;

        if (plays != that.plays) return false;
        if (year != that.year) return false;
        if (Double.compare(that.duration, duration) != 0) return false;
        if (!title.equals(that.title)) return false;
        return artist.equals(that.artist);
    }
}
