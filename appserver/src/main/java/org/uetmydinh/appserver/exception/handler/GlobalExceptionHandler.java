package org.uetmydinh.appserver.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.uetmydinh.appserver.exception.ConnectionException;
import org.uetmydinh.appserver.exception.UrlNotFoundException;
import org.uetmydinh.appserver.exception.UrlPersistenceException;

import java.util.Date;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<?> handleUrlNotFoundException(UrlNotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UrlPersistenceException.class)
    public ResponseEntity<?> handleUrlPersistenceException(UrlPersistenceException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<?> handleConnectionException(ConnectionException ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<?> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }

}
