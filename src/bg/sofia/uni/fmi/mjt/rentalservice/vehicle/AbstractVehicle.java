package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractVehicle implements Vehicle {
    protected String id;
    protected Location location;
    protected LocalDateTime resPeriod;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {
        if (this.resPeriod.isBefore(LocalDateTime.now())) {
            return LocalDateTime.now();
        } else return this.resPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.resPeriod = until;
    }

    @Override
    public String toString() {
        return String.format("Vehicle ID: " + this.getId() + "\nLocation: X = " + this.getLocation().getX() +
                "; Y = " + this.getLocation().getY() + "\nRented Until: " + this.getEndOfReservationPeriod().toString()) + "\n";
    }

    @Override
    public boolean equals(Object vehicle) {
        if (this == vehicle) {
            return true;
        }
        if (!(vehicle instanceof AbstractVehicle)) {
            return false;
        }
        AbstractVehicle that = (AbstractVehicle) vehicle;
        return (this.id == that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, location, resPeriod);
    }


}
