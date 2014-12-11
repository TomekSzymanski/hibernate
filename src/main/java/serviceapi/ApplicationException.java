package serviceapi;

/**
 * Created on 2014-11-27.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String s, Throwable cause) {
        super(s, cause);
    }
    public ApplicationException(String s) {
        super(s);
    }
}
