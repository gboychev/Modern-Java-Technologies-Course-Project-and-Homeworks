package bg.sofia.uni.fmi.mjt.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.Files.createDirectories;

public class DefaultLogger implements Logger {

    private LoggerOptions options;
    private int currentLogNum;

    public DefaultLogger(LoggerOptions options) {
        this.options = options;
        currentLogNum = 0;
    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {

        if (level == null || timestamp == null || message == null || message.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (level.getLevel() < this.options.getMinLogLevel().getLevel()) {
            return;
        }

        Log log = new Log(level, timestamp, options.getClazz().getPackageName(), message);
        //Path filePath = Path.of("log-0.txt");
        try {
            createDirectories(Path.of(options.getDirectory()));
        } catch (Exception e) {
            throw new LogException("there is a problem with creating the directories", e);
        }
        String currentLogPath = String.format(options.getDirectory() + File.separator + "logs-%d.txt", currentLogNum);
        if (Files.exists(Path.of(currentLogPath))) {
            try {
                if (Files.size(Path.of(currentLogPath)) > options.getMaxFileSizeBytes()) {
                    currentLogNum++;
                    currentLogPath = String.format(options.getDirectory() +
                            File.separator + "log-%d.txt", currentLogNum);
                }
            } catch (IOException e) {
                throw new LogException("There was an error in calculating the file size", e);
            }
        }
        try (var fileWriter = new FileWriter(currentLogPath, true)) {
            fileWriter.write("[" + log.level() + "]" + "|" +
                    log.timestamp().format(DateTimeFormatter.ISO_DATE_TIME) +  "|"
                    + log.packageName() + "|"
                    + log.message() + System.lineSeparator());
            fileWriter.flush();
        }
        catch (IOException e) {
            throw new LogException("A problem occurred while writing to a file", e);
        }
    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return Path.of(String.format(options.getDirectory() + File.separator + "logs-%d.txt", currentLogNum));
    }
}
