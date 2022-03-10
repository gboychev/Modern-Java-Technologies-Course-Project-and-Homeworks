package bg.sofia.uni.fmi.mjt.logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DefaultLogParser implements LogParser {

    Path logFilePath;

    public DefaultLogParser(Path logsFilePath) {
        this.logFilePath = logsFilePath;
    }

    @Override
    public List<Log> getLogs(Level level) {
        if (level == null) {
            throw new IllegalArgumentException();
        }

        List<Log> logList = new ArrayList<>();
        if (Files.exists(logFilePath)) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(logFilePath)) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    final short dateTimePosition = 1;
                    final short packageNamePosition = 2;
                    final short messagePosition = 3;

                    String[] lineArr = line.split("\\|");

                    if (line.startsWith("[" + level.toString() + "]")) {
                        logList.add(new Log(level,
                                LocalDateTime.parse(lineArr[dateTimePosition],
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                lineArr[packageNamePosition],
                                lineArr[messagePosition]));
                    }
                }

            } catch (IOException e) {
                throw new IllegalStateException("A problem occurred while reading from a file in getlogs_level", e);
            }
        }
        return logList;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException();
        }

        List<Log> logList = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(logFilePath)) {
            String line;

            final short dateTimePosition = 1;
            final short packageNamePosition = 2;
            final short messagePosition = 3;

            while ((line = bufferedReader.readLine()) != null) {

                String[] lineArr = line.split("\\|");
                LocalDateTime logTimestamp = LocalDateTime.parse(lineArr[dateTimePosition],
                        DateTimeFormatter.ISO_DATE_TIME);

                if ((logTimestamp.isBefore(to) || logTimestamp.isEqual(to)) &&
                        (logTimestamp.isAfter(from) || logTimestamp.isEqual(from))) {

                    logList.add(new Log(Level.valueOf(lineArr[0].substring(1, lineArr[0].length() - 1)),
                            LocalDateTime.parse(lineArr[dateTimePosition],
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME), lineArr[packageNamePosition],
                            lineArr[messagePosition]));
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file in getlogs_fromto", e);
        }
        return logList;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        if (n == 0) {
            return new ArrayList<Log>();
        }
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        List<Log> logList = new ArrayList<>();
        int lines = 0;
        try (BufferedReader br = Files.newBufferedReader(logFilePath)) {
            String line;

            final short dateTimePosition = 1;
            final short packageNamePosition = 2;
            final short messagePosition = 3;

            while ((line = br.readLine()) != null) {
                String[] lineArr = line.split("\\|");
                lines++;
                logList.add(new Log(Level.valueOf(lineArr[0].substring(1, lineArr[0].length() - 1)),
                        LocalDateTime.parse(lineArr[dateTimePosition],
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME), lineArr[packageNamePosition],
                        lineArr[messagePosition]));
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occured while reading from a file in getlogs_tail", e);
        }
        List<Log> result = new ArrayList<>();
        if (logList.size() < n) {
            return logList;
        } else {
            for (int i = logList.size() - n; i < logList.size(); i++) {
                result.add(logList.get(i));
            }
        }
        return result;
    }
}
