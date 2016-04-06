package ca.douglascollege.flamingdodos.database.exceptions;

/**
 * Wrapper for exceptions of different implementations to have simpler exception handling
 */
public class DatabaseException extends Exception {
    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
