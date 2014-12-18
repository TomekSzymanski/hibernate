package serviceapi;

/**
 * Created on 2014-12-17.
 */
public interface AuthorizationService {
    void register(String login, String password);

    boolean validate(String login, String password);

    void delete(String user);


    /*
    add later:
    void deactivate(String user);

    void reactivate(String user);



    resetPassword(String user, String newPassword);

     */
}
