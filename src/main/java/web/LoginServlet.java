package web;

import serviceapi.AuthorizationService;
import serviceimpl.SimpleShop;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created on 2014-12-17.
 */
public class LoginServlet extends HttpServlet {

    private AuthorizationService authorizationService;

    public LoginServlet() {
        authorizationService = SimpleShop.getShopInstance().getAuthorizationService();
    }

    LoginServlet(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("user");
        String password = req.getParameter("password");

        if ( (user == null) || (user.equals("")) || (password == null) || (password.equals(""))) {
            resp.sendRedirect("LoginPage.html");
        }

        if (authorizationService.validate(user, password)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/OfferManagement/AddNewProductCategory");
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            dispatcher.forward(req, resp);
        } else {
            ResponseView responseView = ResponseViewFactory.getResponseView(req.getSession(false));
            responseView.addMessage("Invalid username or password");

            resp.setContentType(responseView.getContentType());

            PrintWriter writer = resp.getWriter();
            responseView.print(writer);

            RequestDispatcher dispatcher = req.getRequestDispatcher("LoginPage.html");
            dispatcher.include(req, resp);

            writer.close();
        }

    }
}
