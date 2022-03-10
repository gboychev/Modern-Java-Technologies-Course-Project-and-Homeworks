package bg.sofia.uni.fmi.mjt.logger;

public class LogException extends RuntimeException {

    public LogException() {
        super();
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Exception e) {
        super(message, e);
    }
}
