package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QuoteFinder implements QuoteService {
    Map<Asset, Quote> assetQuotes;

    public QuoteFinder() {
        assetQuotes = new HashMap<Asset, Quote>();
    }

    @Override
    public Quote getQuote(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException();
        }
        return assetQuotes.get(asset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuoteFinder)) return false;

        QuoteFinder that = (QuoteFinder) o;

        return Objects.equals(assetQuotes, that.assetQuotes);
    }

    @Override
    public int hashCode() {
        return assetQuotes != null ? assetQuotes.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "QuoteFinder{" +
                "assetQuotes=" + assetQuotes +
                '}';
    }
}
