package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

import java.util.Arrays;

public class UserPlaylist implements Playlist {
    private String name;
    private Playable[] playlist;
    private int lastPlayableIndex;

    public UserPlaylist() {
        name = "";
        playlist = new Playable[20];
        lastPlayableIndex = 0;
    }

    public UserPlaylist(String name) {
        this.name = name;
        playlist = new Playable[20];
        lastPlayableIndex = 0;
    }

    public UserPlaylist(Playlist playlist) {
        this.name = playlist.getName();
        this.lastPlayableIndex = ((UserPlaylist) playlist).lastPlayableIndex;
        for (int i = 0; i < lastPlayableIndex; i++) {
            this.playlist[i] = ((UserPlaylist) playlist).playlist[i];
        }
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if(playable == null || playable.getTitle().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (lastPlayableIndex == 20) {
            throw new PlaylistCapacityExceededException();
        }
        playlist[lastPlayableIndex++] = playable;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPlaylist)) return false;

        UserPlaylist that = (UserPlaylist) o;

        if (lastPlayableIndex != that.lastPlayableIndex) return false;
        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(playlist, that.playlist);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(playlist);
        result = 31 * result + lastPlayableIndex;
        return result;
    }
}
