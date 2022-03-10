package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class PremiumAccount extends Account {


    public PremiumAccount() {
        super();
    }

    public PremiumAccount(String email, Library library) {
        super(email, library);
    }

    @Override
    public int getAdsListenedTo() {
        return adsListenedTo;
    }

    @Override
    public AccountType getType() {
        return AccountType.PREMIUM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PremiumAccount)) return false;

        PremiumAccount account = (PremiumAccount) o;

        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
