package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public class AcquisitionOfAsset implements Acquisition {
    private LocalDateTime acquisitionTime;
    private Asset asset;
    private int quantity;
    double price;

    public AcquisitionOfAsset() {
        acquisitionTime = null;
        asset = null;
        quantity = -1;
        price = -1.0;
    }

    public AcquisitionOfAsset(Asset asset, int quantity) {
        acquisitionTime = LocalDateTime.now();
        this.asset = asset;
        this.quantity = quantity;
        price = -1.0;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return acquisitionTime;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcquisitionOfAsset)) return false;

        AcquisitionOfAsset that = (AcquisitionOfAsset) o;

        if (quantity != that.quantity) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (!acquisitionTime.equals(that.acquisitionTime)) return false;
        return asset.equals(that.asset);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = acquisitionTime.hashCode();
        result = 31 * result + asset.hashCode();
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "AcquisitionOfAsset{" +
                "acquisitionTime=" + acquisitionTime +
                ", asset=" + asset +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
