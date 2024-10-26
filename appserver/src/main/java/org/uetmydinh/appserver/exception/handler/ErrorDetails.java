package org.uetmydinh.appserver.exception.handler;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {
}