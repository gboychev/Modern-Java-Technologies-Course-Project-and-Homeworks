package bg.sofia.uni.fmi.mjt.race.track;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RaceTrackTest {

    @Test
    void testManualButInJUnit() throws InterruptedException {
        RaceTrack raceTrack = new RaceTrack(1);
        Car car0 = new Car(0, 4, raceTrack);
        Car car1 = new Car(1, 7, raceTrack);

        Thread th = new Thread(car0);
        Thread th2 = new Thread(car1);

        th.start();
        th2.start();

        th.join();
        th2.join();

        assertEquals(2, raceTrack.getFinishedCarsIds().size());
        assertEquals(2, raceTrack.getNumberOfFinishedCars());
    }


}