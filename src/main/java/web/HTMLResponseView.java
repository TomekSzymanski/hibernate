package web;

import java.io.IOException;
import java.io.PrintWriter;

/**
  not thread safe
 */
public class HTMLResponseView implements ResponseView {

    private StringBuilder out = new StringBuilder();
    private String loggedUser;

    HTMLResponseView(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public void addHeaderAndMenu() {
        out.append("<div class=\"header\">\n");
        out.append("<h2 id=\"ShopLogo\">My Best Shop</h2>\n");

        if (loggedUser!=null) {
            out.append("<p id=\"loggedAs\">Logged as: <span id=\"loggedUser\">" + loggedUser + "</span><a id=\"logoutLink\" href=\"Logout\">Logout</a></p>\n");
        }

        out.append("</div>\n");
        out.append("<div class=\"clear\"></div>\n");
        out.append("<div class=\"menu\">\n");
        out.append("<ul>\n");
        out.append("<li><a href=\"#\">Categories</a>\n");
        out.append("<ul>\n");
        out.append("<li><a href=\"#\">Find</a></li>\n");
        out.append("<li><a href=\"#\">Browse All</a></li>\n");
        out.append("<li><a href=\"AddNewProductCategory\">Add New</a></li>\n");
        out.append("</ul>\n");
        out.append("</li>\n");
        out.append("<li><a href=\"#\">Products</a>\n");
        out.append("<ul>\n");
        out.append("<li><a href=\"#\">Find</a></li>\n");
        out.append("<li><a href=\"#\">Browse All</a></li>\n");
        out.append("<li><a href=\"#\">Add New</a></li>\n");
        out.append("</ul>\n");
        out.append("</li>\n");
        out.append("</ul>\n");
        out.append("</div>\n");
        out.append("<div class=\"clear\"></div>\n");
    }

    @Override
    public void addMessage(String message) {
        out.append("<div class=\"message\">" + message + "</div>\n");
    }

    @Override
    public void addFooter() {
    }

    @Override
    public void print(PrintWriter writer) throws IOException {
        writer.println(getHTMLHeader());
        writer.println(out.toString());
        writer.println(getHTMLClose());
    }

    @Override
    public String quote(String s) {
        return "<i>" + s + "</i>";
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void addElement(Printable printable) {
        out.append(printable.toString());
    }


    private static String getHTMLHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head lang=\"en\">\n");
        builder.append("<meta charset=\"UTF-8\">\n");
        builder.append("<title></title>\n");
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"menu.css\" media=\"screen\" />\n");
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"form.css\" media=\"screen\" />\n");
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"header.css\" media=\"screen\" />\n");
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\" media=\"screen\" />\n");
        builder.append("</head>\n");
        builder.append("<body>\n");
        return builder.toString();
    }

    private static String getHTMLClose() {
        return "</body>\n</html>\n";
    }

//    public static void main(String[] args) throws IOException {
//        HTMLResponseView v = new HTMLResponseView();
//        v.addHeaderAndMenu();
//        v.addMessage("toemk Sz");
//        PrintWriter pw = new PrintWriter(System.out);
//        v.print(pw);
//        pw.close();
//    }
}
