package web;

/**
 * Created on 2014-12-18.
 */
public class AddNewProductCategoryForm implements Printable {
    private static StringBuilder out = new StringBuilder();

    private static void initialize() {
        out.append("<div>");
        out.append("<form action=\"AddNewProductCategory\" method=\"POST\">");
        out.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"form.css\" media=\"screen\" />");
        out.append("<fieldset>");
        out.append("<label>Category name:");
        out.append("<input type=\"text\" name=\"categoryName\" required maxlength=\"30\" placeholder=\"Category name\"/>");
        out.append("</label>");

        out.append("<label>Category aliases:");
        out.append("<textarea id=\"categoryAliases\" name=\"categoryAliases\" maxlength=\"200\" placeholder=\"Category other names (aliases), comma separated\">");
        out.append("</textarea>");
        out.append("</label>");

        out.append("<input class=\"button\" type=\"submit\" value=\"Add category\">");
        out.append("</fieldset>");
        out.append("</form>");
        out.append("</div>");
    }

    static {
        initialize();
    }

    @Override
    public String toString() {
        return out.toString();
    }
}
