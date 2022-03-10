package bg.sofia.uni.fmi.mjt.splitwise.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super();
    }
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
