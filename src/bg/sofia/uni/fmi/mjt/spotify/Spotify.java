package bg.sofia.uni.fmi.mjt.spotify;

import bg.sofia.uni.fmi.mjt.spotify.account.Account;
import bg.sofia.uni.fmi.mjt.spotify.account.AccountType;
import bg.sofia.uni.fmi.mjt.spotify.account.FreeAccount;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlayableNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.StreamingServiceException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Audio;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class Spotify implements StreamingService {

    private Account[] accounts;
    private Playable[] playableContent;

    public Spotify(Account[] accounts, Playable[] playableContent) {
        this.accounts = accounts;
        this.playableContent = playableContent;
    }

    @Override
    public void play(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException {
        if (account == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException();
        }
        boolean foundAcc = false;
        boolean foundPlayable = false;
        for (Account i : accounts) {
            if (i.equals(account)) {
                foundAcc = true;
                try {
                    Playable p = findByTitle(title);
                    i.listen(p);
                } catch (PlayableNotFoundException e) {
                    throw e;
                }
            }
        }
        if (!foundAcc) {
            throw new AccountNotFoundException();
        }
    }

    @Override
    public void like(Account account, String title) throws AccountNotFoundException, PlayableNotFoundException, StreamingServiceException {
        if (account == null || title == null || title.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            Account acc = findAccount(account);
            Playable p = findByTitle(title);
            acc.getLibrary().getLiked().add(p);
        } catch (PlaylistCapacityExceededException e) {
            throw new StreamingServiceException();
        }
//        for (Account i : accounts) {
//            if (i.equals(account)) {
//                foundAcc = true;
//                try {
//                    Playable p = findByTitle(title);
//                    i.getLibrary().getLiked().add(findByTitle(title));
//                } catch (PlayableNotFoundException e) {
//                    throw e;
//                } catch (PlaylistCapacityExceededException ex) {
//                    ex.printStackTrace();
//                    throw new StreamingServiceException();
//                }
//            }
//        }
    }

    @Override
    public Playable findByTitle(String title) throws PlayableNotFoundException {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException();
        }
        boolean foundPlayable = false;
        for (Playable p : playableContent) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        throw new PlayableNotFoundException();
    }

    private Account findAccount(Account account) throws AccountNotFoundException {
        if (account == null) {
            throw new IllegalArgumentException();
        }
        boolean foundAcc = false;
        for (Account i : accounts) {
            if (i.equals(account)) {
                return i;
            }
        }
        throw new AccountNotFoundException();
    }

    @Override
    public Playable getMostPlayed() {
        if (playableContent == null) return null;
        int currentMax = -1;
        Playable cur = null;
        for (Playable p : playableContent) {
            if (p.getTotalPlays() > currentMax) {
                currentMax = p.getTotalPlays();
                cur = p;
            }
        }
        if(cur.getTotalPlays() == 0) {
            return null;
        }
        return cur;
    }

    @Override
    public double getTotalListenTime() {
        double totalListenTime = 0.0;
        for (Account a : accounts) {
            totalListenTime += a.getTotalListenTime();
        }
        return totalListenTime;
    }

    @Override
    public double getTotalPlatformRevenue() {
        double revenue = 0.0;

        for (Account a : accounts) {
            if (a.getType() == AccountType.FREE) {
                revenue += ((FreeAccount) a).getAdsListenedTo() * 0.10;
            } else {
                revenue += 25;
            }
        }
        return revenue;
    }

}
