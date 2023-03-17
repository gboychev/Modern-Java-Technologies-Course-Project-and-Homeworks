package bg.sofia.uni.fmi.mjt.splitwise.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException() {
        super();
    }
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }
}
