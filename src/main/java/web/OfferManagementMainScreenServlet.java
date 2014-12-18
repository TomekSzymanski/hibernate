package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created on 2014-12-18.
 */
public class OfferManagementMainScreenServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseView view = ResponseViewFactory.getResponseView(req.getSession(false));
        view.addHeaderAndMenu();
        PrintWriter writer = resp.getWriter();
        view.print(writer);
        writer.close();

    }
}
