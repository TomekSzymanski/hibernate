package serviceapi;

/**
 * Created on 2014-12-18.
 */
public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String s, Throwable cause) {
        super(s, cause);
    }
}
