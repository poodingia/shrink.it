package org.uetmydinh.appserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find original URL")
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String id) {
        super("Could not find URL " + id);
    }
}
