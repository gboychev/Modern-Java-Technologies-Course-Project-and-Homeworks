package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

import java.util.Arrays;

public class UserLibrary implements Library {

    private UserPlaylist likedContent;
    private UserPlaylist[] playlists;
    private short lastPlaylistIndex;

    public UserLibrary() {
        likedContent = new UserPlaylist("Liked Content");
        playlists = new UserPlaylist[21];
        lastPlaylistIndex = -1;
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (playlist == null || playlist.getName().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (lastPlaylistIndex >= 20) {
            throw new LibraryCapacityExceededException();
        }
        lastPlaylistIndex++;
        playlists[lastPlaylistIndex] = (UserPlaylist) playlist;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (name.equals("Liked Content")) {
            throw new IllegalArgumentException();
        }
        if (lastPlaylistIndex < 0) {
            throw new EmptyLibraryException();
        }

        boolean foundPlaylist = false;

        for (int i = 0; i <= lastPlaylistIndex; i++) {
            if (playlists[i].getName().equals(name)) {
                foundPlaylist = true;
                for (int j = i; j < lastPlaylistIndex; j++) {
                    playlists[j] = playlists[j + 1];
                }
                playlists[lastPlaylistIndex] = null;
                lastPlaylistIndex--;
            }
        }
        if (!foundPlaylist) {
            throw new PlaylistNotFoundException();
        }

    }

    @Override
    public Playlist getLiked() {
        return likedContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLibrary)) return false;

        UserLibrary that = (UserLibrary) o;

        if (lastPlaylistIndex != that.lastPlaylistIndex) return false;
        if (!likedContent.equals(that.likedContent)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(playlists, that.playlists);
    }

    @Override
    public int hashCode() {
        int result = likedContent.hashCode();
        result = 31 * result + Arrays.hashCode(playlists);
        result = 31 * result + (int) lastPlaylistIndex;
        return result;
    }
}
