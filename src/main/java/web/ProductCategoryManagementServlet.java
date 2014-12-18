package web;

import model.ProductCategory;
import serviceapi.ApplicationException;
import serviceapi.OfferManagementService;
import serviceimpl.SimpleShop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

// @Service
public class ProductCategoryManagementServlet extends HttpServlet {

    // TODO make thread safe

    //@Autowired
    //@Qualifier("dbOrderManagement")
    private OfferManagementService offerMgmtService;

    private static final String CATEGORY_ALIASES_SEPARATOR = ",";

    public ProductCategoryManagementServlet() {
        offerMgmtService = SimpleShop.getShopInstance().getOfferManagementService();
    }


    // TODO for UT only
    public ProductCategoryManagementServlet(OfferManagementService offerMgmtService) {
        this.offerMgmtService = offerMgmtService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String  categoryName = req.getParameter("categoryName");

        if (categoryName==null) {
            printAddNewProductCategoryForm(req, resp);
            return;
        }

        ProductCategory newCategory = new ProductCategory(categoryName);

        String categoryAliases = req.getParameter("categoryAliases");
        if ((categoryAliases!=null) && (!categoryAliases.equals(""))) {
            String [] categoryAliasesStrings = categoryAliases.split(CATEGORY_ALIASES_SEPARATOR);
            Set<String> categoryAliasesSet = Arrays.stream(categoryAliasesStrings).map(String::trim).collect(Collectors.toSet());
            newCategory.setAliases(categoryAliasesSet);
        }

        ResponseView responseView = ResponseViewFactory.getResponseView(req.getSession(false));

        responseView.addHeaderAndMenu();

        try {
            offerMgmtService.addCategory(newCategory);
            responseView.addMessage("New category " + responseView.quote(categoryName) + " added");
        } catch (ApplicationException e) {
            responseView.addMessage("Unable to add new category " + responseView.quote(categoryName) + ". " + e.getMessage());
        } finally {
            responseView.addFooter();
        }

        resp.setContentType(responseView.getContentType());
        PrintWriter writer = resp.getWriter();
        responseView.print(writer);
        writer.close();
    }

    private void printAddNewProductCategoryForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ResponseView view = ResponseViewFactory.getResponseView(req.getSession(false));
        view.addHeaderAndMenu();
        view.addElement(new AddNewProductCategoryForm());
        PrintWriter writer = resp.getWriter();
        view.print(writer);
        writer.close();
    }
}
