package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;
import bg.sofia.uni.fmi.mjt.race.track.pit.PitTeam;

import java.util.*;

public class RaceTrack implements Track {

    private Set<Car> carsOnTrack;
    private Set<Car> finishedCars;
    private Pit pit;

    public RaceTrack(int teamCount) {
        pit = new Pit(teamCount);
        carsOnTrack = new HashSet<>();
        finishedCars = new HashSet<>();
    }

    @Override
    public void enterPit(Car car) {
        pit.submitCar(car);
    }

    @Override
    public int getNumberOfFinishedCars() {
        return getFinishedCarsIds().size();
    }

    @Override
    public List<Integer> getFinishedCarsIds() {
        return new ArrayList<>(finishedCars.stream().map(Car::getCarId).toList());
    }

    @Override
    public Pit getPit() {
        return pit;
    }

    public synchronized void beginRaceForCar(Car car) {
        carsOnTrack.add(car);
    }

    public synchronized void addFinishedCar(Car car) {
        carsOnTrack.remove(car);
        this.finishedCars.add(car);
        if (carsOnTrack.isEmpty()) {
            pit.finishRace();
        }
    }
}
