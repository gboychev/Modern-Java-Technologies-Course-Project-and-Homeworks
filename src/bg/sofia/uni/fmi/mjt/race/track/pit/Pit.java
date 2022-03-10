package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Pit {

    private AtomicInteger pitStopsCount = new AtomicInteger();
    private Set<PitTeam> pitTeams;
    private Set<Car> cars;
    private boolean raceFinished = false;
    long start;
	
    public Pit(int nPitTeams) {
        start = System.currentTimeMillis();
		cars = new LinkedHashSet<>();
        pitTeams = new HashSet<>();
        for (int i = 0; i < nPitTeams; i++) {
            pitTeams.add(new PitTeam(i, this));
        }
        for (var p : pitTeams) {
            p.start();
        }
    }

    public void submitCar(Car car) {
        synchronized (this) {
            this.cars.add(car);
        }
        pitStopsCount.getAndIncrement();
    }

    public synchronized Car getCar() {
        Car firstCar = null;
        for (var c : cars) {
            firstCar = c;
            break;
        }
        if (firstCar != null) {
            cars.remove(firstCar);
        }
        return firstCar;
    }

    public int getPitStopsCount() {
        return pitStopsCount.get();
    }

    public synchronized List<PitTeam> getPitTeams() {
        return new ArrayList<>(pitTeams);
    }

    public synchronized void finishRace() {
        raceFinished = true;
        synchronized (PitTeam.class) {
            PitTeam.class.notifyAll();
        }
		System.out.println(System.currentTimeMillis() - start);
    }

    synchronized Set<Car> getPitStoppedCars() {
        return cars;
    }

    synchronized boolean isRaceFinished() {
        return raceFinished;
    }

}