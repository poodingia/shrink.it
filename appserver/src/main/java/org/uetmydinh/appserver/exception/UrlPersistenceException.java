package org.uetmydinh.appserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Could not persist URL")
public class UrlPersistenceException extends RuntimeException {
    public UrlPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
