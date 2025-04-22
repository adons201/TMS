package ru.tms.exceptions;

public class InvalidCommentDataException extends RuntimeException {
    public InvalidCommentDataException(String message) {
        super(message);
    }
}
