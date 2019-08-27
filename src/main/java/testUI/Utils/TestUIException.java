package testUI.Utils;

public class TestUIException extends RuntimeException {
    public TestUIException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public TestUIException(String errorMessage) {
        super(errorMessage);
    }
}
