package web;

import javax.servlet.http.HttpSession;

/**
 * Created on 2014-12-16.
 */
public class ResponseViewFactory {

    public static ResponseView getResponseView(HttpSession session) {
        String loggedUser = null;
        if (session!=null) {
            loggedUser = (String)session.getAttribute("user");
        }
        return new HTMLResponseView(loggedUser);
    }

}
