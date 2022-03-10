package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.PitTeam;

import java.util.Random;

public class Car implements Runnable {

    private static final Random RANDOM = new Random();

    private int id;
    private int pitStops;
    private Track track;
    private final int sleepTime = RANDOM.nextInt(700);

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.pitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        ((RaceTrack)track).beginRaceForCar(this);
        while (pitStops > 0) {
            try {
                Thread.sleep(sleepTime);
                track.enterPit(this);
                synchronized (PitTeam.class) {
                    PitTeam.class.notify();
                }
                synchronized (this) {
                    this.wait();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pitStops--;
        }
        System.out.println("Car with id " + id + " FINISHED");
        ((RaceTrack)track).addFinishedCar(this);
    }

    public int getCarId() {
        return id;
    }

    public int getNPitStops() {
        return pitStops;
    }

    public Track getTrack() {
        return track;
    }
}