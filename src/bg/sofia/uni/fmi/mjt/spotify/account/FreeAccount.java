package bg.sofia.uni.fmi.mjt.spotify.account;

import bg.sofia.uni.fmi.mjt.spotify.library.Library;

public class FreeAccount extends Account{


    public FreeAccount(){
        super();
    }
    public FreeAccount(String email, Library library){
        super(email, library);
    }
    @Override
    public int getAdsListenedTo() {
        return adsListenedTo;
    }

    @Override
    public AccountType getType() {
        return AccountType.FREE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreeAccount)) return false;

        FreeAccount account = (FreeAccount) o;

        return email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
