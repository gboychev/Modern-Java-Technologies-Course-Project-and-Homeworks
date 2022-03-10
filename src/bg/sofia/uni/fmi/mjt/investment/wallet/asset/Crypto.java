package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public class Crypto extends AbstractAsset implements Asset {
    public Crypto(String id, String name) {
        super(id, name);
        this.assetType = AssetType.CRYPTO;
    }

}
