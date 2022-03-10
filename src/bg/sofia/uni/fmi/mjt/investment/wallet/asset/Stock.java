package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Stock extends AbstractAsset implements Asset {
    public Stock(String id, String name) {
        super(id, name);
        assetType = AssetType.STOCK;
    }
}
