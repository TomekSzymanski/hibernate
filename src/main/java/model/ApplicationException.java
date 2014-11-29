package model;

/**
 * Created on 2014-11-27.
 */
public class ApplicationException extends RuntimeException {
    ApplicationException(String s, Throwable cause) {
        super(s, cause);
    }
    ApplicationException(String s) {
        super(s);
    }
}
