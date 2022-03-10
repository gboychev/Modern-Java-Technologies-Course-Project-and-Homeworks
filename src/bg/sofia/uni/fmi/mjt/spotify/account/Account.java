package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public abstract class Account {

    protected String email;
    protected Library library;
    protected double totalListenTime;
    protected int adsListenedTo;
    protected int contentConsumed;
    protected AccountType type;

    public Account() {
        email = null;
        library = null;
        totalListenTime = 0.0;
        adsListenedTo = 0;
        contentConsumed = 0;
        if (this instanceof FreeAccount) {
            type = AccountType.FREE;
        } else type = AccountType.PREMIUM;
    }

    public Account(String email, Library library) {
        this();
        this.email = email;
        this.library = library;
    }


    /**
     * Returns the number of ads listened to.
     * - Free accounts get one ad after every 5 pieces of content played
     * - Premium accounts get no ads
     */
    public abstract int getAdsListenedTo();

    /**
     * Returns the account type as an enum with possible values FREE and PREMIUM
     */
    public abstract AccountType getType();

    /**
     * Simulates listening of the specified content.
     * This should increment the total number of content listened and the total listen time for this account.
     *
     * @param playable
     */
    public void listen(Playable playable) {
        if (playable == null || playable.getTitle().isEmpty()) {
            throw new IllegalArgumentException();
        }
        playable.play();
        totalListenTime += playable.getDuration();
        contentConsumed++;

        if (this.type == AccountType.FREE && contentConsumed % 5 == 0) {
            adsListenedTo++;
        }

    }

    /**
     * Returns the library for this account.
     */
    public Library getLibrary() {
        return library;
    }

    /**
     * Returns the total listen time for this account. The time for any ads listened is not included.
     */
    public double getTotalListenTime() {
        return totalListenTime;
    }
}
