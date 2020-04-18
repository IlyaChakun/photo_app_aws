package by.chekun.exception;

public class ServiceException extends RuntimeException {

    private final int statusCode;

    public ServiceException(int statusCode) {
        this.statusCode = statusCode;
    }

    public ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ServiceException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
