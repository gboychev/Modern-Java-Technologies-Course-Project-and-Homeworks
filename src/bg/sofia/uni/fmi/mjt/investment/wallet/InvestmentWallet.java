package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AcquisitionOfAsset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AbstractAsset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AssetType;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Crypto;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.util.*;

public class InvestmentWallet implements Wallet {
    private double balance;
    private QuoteService quoteService;
    private Collection<Asset> assetCollection;
    private Collection<Acquisition> acquisitions;

    public InvestmentWallet() {
        balance = 0.0;
        quoteService = null;
        assetCollection = null;
        acquisitions = null;
    }

    public InvestmentWallet(QuoteService quoteService) {
        balance = 0.0;
        this.quoteService = quoteService;
        assetCollection = new ArrayList<Asset>();
        acquisitions = new LinkedHashSet<Acquisition>();
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0.0) {
            throw new IllegalArgumentException();
        }
        balance += cash;
        return balance;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0.0) {
            throw new IllegalArgumentException();
        }
        if (balance - cash < 0.0) {
            throw new InsufficientResourcesException();
        } else {
            balance -= cash;
        }
        return balance;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {

        checkExceptionsSellBuy(asset, quantity, maxPrice, "BUY");

        double price = (quoteService.getQuote(asset).askPrice());
        if (price * quantity > balance) {
            throw new InsufficientResourcesException();
        }

        Acquisition currentAcquisition = new AcquisitionOfAsset(asset, quantity);
        ((AcquisitionOfAsset) currentAcquisition).setPrice(quoteService.getQuote(asset).askPrice());

        acquisitions.add(currentAcquisition);
        for (int i = 0; i < quantity; i++) {
            assetCollection.add(asset);
        }
        return currentAcquisition;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        checkExceptionsSellBuy(asset, quantity, minPrice, "SELL");

        List<Asset> assetList = new ArrayList<Asset>();
        for (var i : assetCollection) {
            if (i.getType() == asset.getType()) {
                assetList.add(i);
            }
        }
        if (assetList.size() < quantity) {
            throw new InsufficientResourcesException();
        }

        int sellCounter = 0;
        for (var i : assetList) {
            assetCollection.remove(i);
            sellCounter++;
            if (sellCounter == quantity) {
                break;
            }
        }

        double soldValue = 0.0;
        soldValue += quantity * quoteService.getQuote(asset).bidPrice();
        balance += soldValue;

        return soldValue;
    }

    private void checkExceptionsSellBuy(Asset asset, int quantity, double checkPrice, String mode)
            throws OfferPriceException, UnknownAssetException {
        if (checkPrice < 0 || quantity < 0 || asset == null) {
            throw new IllegalArgumentException();
        }
        if (quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException();
        }
        if (mode.equals("BUY")) {
            if (quoteService.getQuote(asset).askPrice() > checkPrice) {
                throw new OfferPriceException();
            }
        }
        if (mode.equals("SELL")) {
            if (quoteService.getQuote(asset).bidPrice() < checkPrice) {
                throw new OfferPriceException();
            }
        }
    }

    @Override
    public double getValuation() {
        double valuation = 0.0;
        for (var i : assetCollection) {
            valuation += quoteService.getQuote(i).bidPrice();
        }
        return valuation;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException();
        }

        boolean foundAsset = false;
        double assetPrice = -1.0;
        double valuation = 0.00;
        for (var i : assetCollection) {
            if (i.getType().equals(asset.getType())) {
                if (!foundAsset) {
                    if (quoteService.getQuote(i) == null) {
                        throw new UnknownAssetException();
                    }
                    assetPrice = quoteService.getQuote(i).bidPrice();
                    foundAsset = true;
                }
                valuation += assetPrice;
            }
        }

        if (!foundAsset) {
            throw new UnknownAssetException();
        }

        return valuation;
    }

    @Override
    public Asset getMostValuableAsset() {
        if (assetCollection == null) {
            return null;
        }
        final int assetTypeCount = 4;
        double[] assetValues = new double[assetTypeCount];
        Arrays.fill(assetValues, Double.MIN_VALUE);
        final short crypto = 0;
        final short fiat = 1;
        final short gold = 2;
        final short stock = 3;
        Asset result = null;

        for (var i : assetCollection) {
            double currentAssetEval = Double.MIN_VALUE;
            currentAssetEval = quoteService.getQuote(i).bidPrice();

            switch (i.getType()) {
                case CRYPTO -> assetValues[crypto] += currentAssetEval;
                case FIAT -> assetValues[fiat] += currentAssetEval;
                case GOLD -> assetValues[gold] += currentAssetEval;
                case STOCK -> assetValues[stock] += currentAssetEval;
            }
        }

        int maxIndex = -1;
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < assetTypeCount; i++) {
            if (assetValues[i] > maxValue) {
                maxValue = assetValues[i];
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case crypto -> result = findAssetType(AssetType.CRYPTO);
            case fiat -> result = findAssetType(AssetType.FIAT);
            case gold -> result = findAssetType(AssetType.GOLD);
            case stock -> result = findAssetType(AssetType.STOCK);
        }

        return result;
    }

    private Asset findAssetType(AssetType assetType) {
        if (assetType == null) throw new IllegalArgumentException();

        for (var i : assetCollection) {
            if (i.getType().equals(assetType)) {
                return i;
            }
        }

        return null;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return Set.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        Set<Acquisition> result = new LinkedHashSet<Acquisition>();
        Acquisition[] acquisitionsArray = new Acquisition[acquisitions.size()];
        acquisitionsArray = acquisitions.toArray(acquisitionsArray);

        int acquisitionsAddedToResult = 0;
        for (int i = acquisitionsArray.length - 1; i > -1; i--) {
            result.add(acquisitionsArray[i]);
            acquisitionsAddedToResult++;
            if (acquisitionsAddedToResult == n) {
                break;
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentWallet)) return false;
        InvestmentWallet that = (InvestmentWallet) o;
        return Double.compare(that.balance, balance) == 0 && Objects.equals(assetCollection, that.assetCollection)
                && Objects.equals(acquisitions, that.acquisitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, assetCollection, acquisitions);
    }

    @Override
    public String toString() {
        return "InvestmentWallet{" +
                "balance=" + balance +
                ", quoteService=" + quoteService +
                ", assetCollection=" + assetCollection +
                ", acquisitions=" + acquisitions +
                '}';
    }
}
