package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public class Bicycle extends AbstractVehicle implements Vehicle{

    public Bicycle(String id, Location location){
        this.id=id;
        this.location = location;
        this.resPeriod=LocalDateTime.now();
    }

    @Override
    public double getPricePerMinute() {
        return 0.20;
    }

    @Override
    public String getType() {
        return "BICYCLE";
    }

}
