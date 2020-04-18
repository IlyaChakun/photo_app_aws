package by.chekun.exception;

import com.amazonaws.AmazonServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<?> handleS3Exception(AmazonServiceException ex) {
        return new ResponseEntity<>(
                new ErrorMessage(ex.getErrorMessage()),
                HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(ServiceException ex) {
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleOthersException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Internal server error."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
