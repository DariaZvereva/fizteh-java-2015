package ru.fizteh.fivt.students.DariaZvereva.MiniORM;

/**
 * Created by Dasha on 16.12.2015.
 */
public class DatabaseServiceException extends Exception {

    public DatabaseServiceException(String message) {
        super(message);
    }

    public DatabaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
