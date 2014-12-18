package serviceapi;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import serviceimpl.DBBasedAuthorizationService;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created on 2014-12-18.
 */
public class AuthorizationServiceTest {

    private static final String username = "Username";
    private static final AuthorizationService service = new DBBasedAuthorizationService();

    @BeforeClass
    public static void setUp() {
        service.register(username, "Password");
    }

    @Test
    public void registerUserAndCheckIfValid() {
        assertTrue(service.validate(username, "Password"));
    }

    @Test
    public void invalidUser() {} {
        assertFalse(service.validate("OtherUser", "Password"));
    }

    @Test
    public void invalidPassword() {
        assertFalse(service.validate(username, "PasswordBAD"));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void tryRegisterTheSameUserTwice() {
        service.register(username, "PasswordMayBeOther");
    }

    @AfterClass
    public static void cleanUp() {
        service.delete(username);
    }

}
