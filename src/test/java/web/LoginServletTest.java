package web;

import org.junit.BeforeClass;
import org.junit.Test;
import serviceapi.AuthorizationService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created on 2014-12-17.
 */
public class LoginServletTest {

    private static LoginServlet servlet;

    @BeforeClass
    public static void setUp() {
        AuthorizationService authorizationServiceMock = mock(AuthorizationService.class);
        when(authorizationServiceMock.validate("USER", "PASSWORD")).thenReturn(true);
        servlet = new LoginServlet(authorizationServiceMock);
    }

    @Test
    public void authorizationSuccessful() throws ServletException, IOException {
        //given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter("user")).thenReturn("USER");
        when(requestMock.getParameter("password")).thenReturn("PASSWORD");

        HttpSession sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);

        RequestDispatcher dispatcherSpy = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherSpy);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));

        // when
        servlet.doPost(requestMock, responseMock);

        // then:
        // 1. session is created and user parameter is set
        HttpSession session = requestMock.getSession();
        assertNotNull(session);
        verify(session).setAttribute("user", "USER");
        // 2. we are redirected:
        verify(dispatcherSpy).forward(requestMock, responseMock);
    }

    @Test
    public void authorizationFail() throws ServletException, IOException {
        //given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        RequestDispatcher dispatcherSpy = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherSpy);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));

        // when
        servlet.doPost(requestMock, responseMock);

        // then:
        HttpSession session = requestMock.getSession();
        assertNull("There should be no session created", session);
        // and we are redirected:
        verify(dispatcherSpy).include(requestMock, responseMock);
    }

    @Test
    public void wrongPassword() throws ServletException, IOException {
        //given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter("user")).thenReturn("USER");
        when(requestMock.getParameter("password")).thenReturn("WRONG_PASSWORD");

        RequestDispatcher dispatcherSpy = mock(RequestDispatcher.class);
        when(requestMock.getRequestDispatcher(anyString())).thenReturn(dispatcherSpy);

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));

        // when
        servlet.doPost(requestMock, responseMock);

        // then:
        // 1. no session is created
        HttpSession session = requestMock.getSession();
        assertNull("There should be no session created", session);
        // and we are redirected:
        verify(dispatcherSpy).include(requestMock, responseMock);

    }
}
