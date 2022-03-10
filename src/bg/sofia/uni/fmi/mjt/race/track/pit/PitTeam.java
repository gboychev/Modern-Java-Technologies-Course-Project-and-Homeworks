package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PitTeam extends Thread {


    private static final Random RANDOM = new Random();

    private int id;
    private Pit pitStop;
    private final int sleepTime = RANDOM.nextInt(200);
    private AtomicInteger pitStoppedCars;

    public PitTeam(int id, Pit pitStop) {
        this.id = id;
        this.pitStop = pitStop;
        pitStoppedCars = new AtomicInteger();
    }

    @Override
    public void run() {
        while (!pitStop.isRaceFinished()) {
            while (getPitStoppedCarsSize() == 0 && !pitStop.isRaceFinished()) {
                waitForCar();
            }
            Car currentCar;
            synchronized (this) {
                currentCar = pitStop.getCar();
            }
            if (currentCar != null) {
                try {
                    pitStoppedCars.getAndIncrement();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (currentCar) {
                    currentCar.notify();
                }
            }
        }
    }

    private void waitForCar() {
        if (!pitStop.isRaceFinished()) {
            try {
                synchronized (PitTeam.class) {
                    PitTeam.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized int getPitStoppedCarsSize() {
        return pitStop.getPitStoppedCars().size();
    }

    public int getPitStoppedCars() {
        return pitStoppedCars.get();
    }

}