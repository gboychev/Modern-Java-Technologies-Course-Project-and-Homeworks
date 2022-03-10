package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AcquisitionOfAsset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Crypto;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Fiat;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Gold;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Stock;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteFinder;

public class PleaseWork {
    public static void main(String[] args) {
        InvestmentWallet wallet = new InvestmentWallet(new QuoteFinder());
        Crypto c = new Crypto("313", "damn");
        Fiat f = new Fiat("324234", "sam");
        Gold g = new Gold("GOLD BEYBE", "god");
        Stock s = new Stock("Stock beybe", "s");

        final int numberOfAcquisitions = 4;
        AcquisitionOfAsset a = new AcquisitionOfAsset(c, numberOfAcquisitions);

        System.out.println("we have reached the destination");
        final int testDeposit = 40000;
        wallet.deposit(testDeposit);
        Quote price = new Quote(2, 2);
        System.out.println(a.toString());
    }
}
