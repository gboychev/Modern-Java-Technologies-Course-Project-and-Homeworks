package bg.sofia.uni.fmi.mjt.investment.wallet.asset;

public abstract class AbstractAsset implements Asset {
    protected String id;
    protected String name;
    protected AssetType assetType;

    public AbstractAsset() {
        id = null;
        name = null;
        assetType = null;
    }

    public AbstractAsset(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AssetType getType() {
        return assetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAsset)) return false;

        AbstractAsset that = (AbstractAsset) o;

        return id.equals(that.id) && assetType.equals(that.assetType);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "AbstractAsset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", assetType=" + assetType +
                '}';
    }
}
