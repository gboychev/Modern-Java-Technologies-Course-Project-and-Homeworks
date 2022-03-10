package bg.sofia.uni.fmi.mjt.race.track;

import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ManualTestRaceTrack {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        RaceTrack raceTrack = new RaceTrack(20);
        Car car0 = new Car(0, 0, raceTrack);
        Car car1 = new Car(1, 1, raceTrack);

        Car car2 = new Car(2, 2, raceTrack);

        Car car3 = new Car(3, 3, raceTrack);
        Car car4 = new Car(4, 4, raceTrack);
        Car car5 = new Car(5, 5, raceTrack);
        Car car6 = new Car(6, 6, raceTrack);
        Car car7 = new Car(7, 7, raceTrack);
        Car car8 = new Car(8, 8, raceTrack);
        Car car9 = new Car(9, 9, raceTrack);


        Thread th = new Thread(car0);
        Thread th2 = new Thread(car1);

        Thread th3 = new Thread(car2);
        Thread th4 = new Thread(car3);
        Thread th5 = new Thread(car4);
        Thread th6 = new Thread(car5);
        Thread th7 = new Thread(car6);

        Thread th8 = new Thread(car7);
        Thread th9 = new Thread(car8);
        Thread th10 = new Thread(car9);

        th.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();
        th6.start();
        th7.start();
        th8.start();
        th9.start();
        th10.start();

        th.join();
        th2.join();
        th3.join();
        th4.join();
        th5.join();
        th6.join();
        th7.join();
        th8.join();
        th9.join();
        th10.join();
        long fin = System.currentTimeMillis();
        assertEquals(10, raceTrack.getFinishedCarsIds().size());
        assertEquals(10, raceTrack.getNumberOfFinishedCars());
        System.out.println(fin - start);
    }
}