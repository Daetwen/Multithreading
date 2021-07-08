package edu.epam.task4.exception;

public class FerryCarException extends Exception {
    public FerryCarException() {
    }

    public FerryCarException(String message) {
        super(message);
    }

    public FerryCarException(String message, Throwable cause) {
        super(message, cause);
    }

    public FerryCarException(Throwable cause) {
        super(cause);
    }
}
