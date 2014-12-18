package web;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created on 2014-12-16.
 */
public interface ResponseView {
    void addHeaderAndMenu();

    void addMessage(String s);

    void addFooter();

    void print(PrintWriter writer) throws IOException;

    String quote(String s);

    String getContentType();

    void addElement(Printable printable);
}
