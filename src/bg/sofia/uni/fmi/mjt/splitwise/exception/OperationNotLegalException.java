package bg.sofia.uni.fmi.mjt.splitwise.exception;

public class OperationNotLegalException extends RuntimeException {
    public OperationNotLegalException() {
        super();
    }
    public OperationNotLegalException(String message) {
        super(message);
    }
}
