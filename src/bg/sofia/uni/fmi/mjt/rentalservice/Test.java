package bg.sofia.uni.fmi.mjt.rentalservice;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalService;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Car;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        Car mazda = new Car("4234", new Location(3.0,4.0));
        Car toyota = new Car("9999954", new Location(31.0,42.0));
        Car volvo = new Car("66664", new Location(31.0,422.0));
        Bicycle mitsubishi = new Bicycle("433", new Location(35.0,4.0));

        Vehicle[] vehs = {mazda, mitsubishi};
        RentalService sashos = new RentalService(vehs);
        sashos.addVehicle(toyota);
        sashos.addVehicle(volvo);
        for (Vehicle i : sashos.getGarage()){
            System.out.println(i);
        }
        System.out.println("we are now searching");
        mazda.setEndOfReservationPeriod(LocalDateTime.MAX);
        System.out.println(sashos.findNearestAvailableVehicleInRadius("CAR",new Location(35,4),2500.0));
    }
}
