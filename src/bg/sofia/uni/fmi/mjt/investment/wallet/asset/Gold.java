package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Gold extends AbstractAsset implements Asset {
    public Gold(String id, String name) {
        super(id, name);
        assetType = AssetType.GOLD;
    }
}
