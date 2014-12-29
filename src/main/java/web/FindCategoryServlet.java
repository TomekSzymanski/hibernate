package web;

import model.ProductCategory;
import serviceapi.ShoppingService;
import serviceimpl.SimpleShop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created on 2014-12-18.
 */
public class FindCategoryServlet extends HttpServlet {

    private ShoppingService service = SimpleShop.getShopInstance().getShoppingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryNamePattern = req.getParameter("namePattern");
        String returnAllString = req.getParameter("returnAll");

        // no search parameters specified for the servlet, print search form
        if ((categoryNamePattern == null) && (returnAllString == null)) {
            printCategorySearchForm(req, resp);
            return;
        }

        List<ProductCategory> matchingCategories;
        if ((categoryNamePattern != null) && (!categoryNamePattern.equals(""))) { // there was some serach pattern specified
            matchingCategories = service.getCategories()
                    .stream()
                    .filter(category -> Pattern.matches(categoryNamePattern, category.getName()))
                    .collect(Collectors.toList());
        } else if (returnAllString.equals("true")) {
            matchingCategories = service.getCategories();
        } else {
            throw new IllegalArgumentException("Servlet must be called with either returnAll=true, or speyfying not null namePattern");
        }

        ResponseView view = ResponseViewFactory.getResponseView(req.getSession(false));
        view.addHeaderAndMenu();

        String tableCaption = "Product categories";
        List<String> tableHeaders = Arrays.asList(new String[]{"Category name", "Category aliases"});

        List<List<String>> tableCells = new ArrayList<>();

        for (ProductCategory category : matchingCategories) {
            List<String> oneCategoryValues = new ArrayList<>();
            oneCategoryValues.add(category.getName());
            oneCategoryValues.add(setToCommaSeparatedList(category.getAliasesStrings()));
            tableCells.add(oneCategoryValues);
        }

        view.addElement(new HTMLTable(tableCaption, tableHeaders, tableCells));

        resp.setContentType(view.getContentType());
        PrintWriter writer = resp.getWriter();
        view.print(writer);
        writer.close();
    }

    private void printCategorySearchForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseView view = ResponseViewFactory.getResponseView(req.getSession(false));
        view.addHeaderAndMenu();
        // view.addElement(new SearchForm(..));
        view.addMessage("Search form to be implemented");
        resp.setContentType(view.getContentType());
        PrintWriter writer = resp.getWriter();
        view.print(writer);
        writer.close();
    }

    private static String setToCommaSeparatedList(Set<String> set) {
        return set.stream().collect(Collectors.joining(", ")).toString();
    }
}
