package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.FreeAccount;
import bg.sofia.uni.fmi.mjt.spotify.account.PremiumAccount;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.library.UserLibrary;
import bg.sofia.uni.fmi.mjt.spotify.playable.*;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class SpotifyTest {
    public static void main(String[] args) throws PlaylistCapacityExceededException, LibraryCapacityExceededException {
        Audio song = new Audio("song1","pe6o",2000, 1.0);
        UserPlaylist testPlaylist = new UserPlaylist("playlistName");
        testPlaylist.add(song);
        UserLibrary lib = new UserLibrary();
        lib.add(testPlaylist);
        FreeAccount gosho = new FreeAccount("@gmail",lib);
        gosho.listen(song);
        double time = gosho.getTotalListenTime();
        System.out.println(time);
        gosho.getType();
        Account[] accArr = new Account[10];
        Playable[] playArr = new Playable[10];
        accArr[0]=gosho;
        for(int i = 0; i<5; i++) {
            gosho.listen(song);
        }
        PremiumAccount sasho = new PremiumAccount("ele", lib);
        sasho.listen(song);
        playArr[0]=song;
        accArr[1]=sasho;
        Spotify spot = new Spotify(accArr,playArr);
        spot.getTotalPlatformRevenue();
    }
}
