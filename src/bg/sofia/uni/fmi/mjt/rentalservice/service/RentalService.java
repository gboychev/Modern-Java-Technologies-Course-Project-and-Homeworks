package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.*;

public class RentalService implements RentalServiceAPI {

    private Vehicle[] registeredVehicles;
    private int regCount;
    private static final int VEHICLE_SOTS_INITIAL = 5;

    public RentalService() {
        registeredVehicles = new Vehicle[VEHICLE_SOTS_INITIAL];
        regCount = 0;
    }

    public RentalService(Vehicle[] vehicles) {
        registeredVehicles = vehicles.clone();
        for (Vehicle i : vehicles) {
            regCount++;
        }
    }

    public void addVehicle(Vehicle newVehicle) {
        int newVehIndex = regCount;
        if (regCount == registeredVehicles.length) {
            Vehicle[] newRegisteredVehicles = new Vehicle[registeredVehicles.length * 2];
            for (int i = 0; i < regCount; i++) {
                newRegisteredVehicles[i] = registeredVehicles[i];
            }
            registeredVehicles = newRegisteredVehicles.clone();
            registeredVehicles[newVehIndex] = newVehicle;
            regCount++;
        } else registeredVehicles[regCount] = newVehicle;
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        if (vehicle == null || until == null) return -1.0;
        for (Vehicle i : registeredVehicles) {
            if (i.getId().equals(vehicle.getId()) && i.getEndOfReservationPeriod().isBefore(LocalDateTime.now())
                    && LocalDateTime.now().isBefore(until)) {
                double price = 0.0;
                int minutes = (int) ChronoUnit.MINUTES.between(LocalDateTime.now(), until);
                price = (minutes * vehicle.getPricePerMinute()) * 1.0;
                i.setEndOfReservationPeriod(until);
                return price;
            }
        }
        return -1.0;
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        int nearestVehicleID = -1;
        double nearestDistance = Double.MAX_VALUE;
        Vehicle veh = null;
        for (Vehicle i : registeredVehicles) {
            if (!Objects.equals(type, i.getType()) || i.getEndOfReservationPeriod().isAfter(LocalDateTime.now()))
                continue;

            double coordX = i.getLocation().getX() - location.getX();
            double coordY = i.getLocation().getY() - location.getY();
            double iDistance = sqrt(coordX * coordX + coordY * coordY);
            if (iDistance < nearestDistance) {
                nearestDistance = iDistance;
                veh = i;
            }
        }
        if (nearestDistance <= maxDistance) {
            return veh;
        }
        return null;
    }

    public Vehicle[] getGarage() {
        return registeredVehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalService)) return false;

        RentalService that = (RentalService) o;

        if (regCount != that.regCount) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(registeredVehicles, that.registeredVehicles);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(registeredVehicles);
        result = 31 * result + regCount;
        return result;
    }
}
