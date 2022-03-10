package bg.sofia.uni.fmi.mjt.splitwise.exception;

public class UserAlreadyLoggedInException extends RuntimeException {
    public UserAlreadyLoggedInException() {
        super();
    }
    public UserAlreadyLoggedInException(String message) {
        super(message);
    }
}
