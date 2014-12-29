package web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created on 2014-12-17.
 */
public class ForceLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)servletRequest).getSession(false);


        String servletPath = ((HttpServletRequest) servletRequest).getServletPath();
        if (((session != null) && (session.getAttribute("user") != null)) // user logged in, continue
            || ( servletPath.endsWith("LoginPage.html") || servletPath.endsWith("Login") || servletPath.endsWith("allStyles.css") ) ) { //we are redirecting to login page itself // TODO: security hole servletPath.endsWith("LoginPage.html") || servletPath.endsWith("Login") : do exact match
            filterChain.doFilter(servletRequest, servletResponse);
        } else { // redirect to the login page
            ((HttpServletResponse)servletResponse).sendRedirect("LoginPage.html");
            // TODO chenge to dispatcher forward?
        }
    }

    @Override
    public void destroy() {

    }
}
