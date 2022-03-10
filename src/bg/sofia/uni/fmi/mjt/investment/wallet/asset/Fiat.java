package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Fiat extends AbstractAsset implements Asset {
    public Fiat(String id, String name) {
        super(id, name);
        assetType = AssetType.FIAT;
    }
}
