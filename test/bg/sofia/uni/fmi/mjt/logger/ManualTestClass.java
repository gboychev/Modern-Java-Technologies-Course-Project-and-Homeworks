package bg.sofia.uni.fmi.mjt.logger;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Car;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ManualTestClass {
    public static void main(String[] args) {
//        Path filePath = Path.of("fileTestFile.txt");
//        String text = "Write this string to my file" + System.lineSeparator();
//        try (var bufferedWriter = Files.newBufferedWriter(filePath)) {
//            bufferedWriter.write(text);
//            bufferedWriter.flush();
//        } catch (Exception e) {
//            throw new IllegalStateException("A problem occurred while writing to a file", e);
//        }
        Car car = new Car("1", new Location(1,2));
        try {
            Files.deleteIfExists(Path.of("/logs/log-0.txt"));
        } catch (IOException e) {
            throw new LogException("error in main with deleting log-0.txt");
        }
        Logger logger = new DefaultLogger(new LoggerOptions(car.getClass(), "/logs"));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        LocalDateTime beforeSecond = LocalDateTime.now();
        System.out.println("now have assigned before second");
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(), String.format("Bought asset with ID %s", 1));
        logger.log(Level.INFO, LocalDateTime.now(),
                String.format("Bought asset with2 ID %s", 1));
        logger.log(Level.DEBUG, LocalDateTime.now(), "so deep");
        LocalDateTime afterThird = LocalDateTime.now();

        System.out.println("all is ok");
        System.out.println("and here");

        LogParser logParser = new DefaultLogParser(Path.of("\\logs\\logs-0.txt"));
        System.out.println("successful creation of logparser");
        List<Log> logList = logParser.getLogs(Level.INFO);
        System.out.println(logList);
        System.out.println(logParser.getLogs(beforeSecond, afterThird));

        System.out.println(logParser.getLogsTail(4));
    }
}
