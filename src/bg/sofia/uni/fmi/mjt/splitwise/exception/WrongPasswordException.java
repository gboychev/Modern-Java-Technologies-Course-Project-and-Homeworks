package bg.sofia.uni.fmi.mjt.splitwise.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super();
    }
    public WrongPasswordException(String message) {
        super(message);
    }
}
