package web;

import model.ProductCategory;
import serviceapi.OfferManagementService;
import serviceimpl.SimpleShop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductCategoryManagementServletOld extends HttpServlet {

    public static final String ADD_NEW_PRODUCT_CATEGORY = "addNewProductCategory";
    public static final String PRODUCT_CATEGORY_NAME_PARAM = "productCategoryName";

    private final OfferManagementService offerManagementService = SimpleShop.getShopInstance().getOfferManagementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String actionParameter = req.getParameter("action");

        if (ADD_NEW_PRODUCT_CATEGORY.equals(actionParameter)) {
            handleProductCategoryAdd(req, resp);
            return;
        }

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<HTML><h2>Product Category management</h2></HTML>");
        writer.println("Actions:");
        writer.println("<ul>");
        writer.println("<li><a href=\"ProductCategoryManagement?action=" + ADD_NEW_PRODUCT_CATEGORY + "\">Add new product category</a></li>");
        writer.println("</ul>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productCathegoryName = req.getParameter(PRODUCT_CATEGORY_NAME_PARAM);
        resp.getWriter().println("you provided " + productCathegoryName);
    }

    private void handleProductCategoryAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String productCathegoryName = req.getParameter(PRODUCT_CATEGORY_NAME_PARAM);
        if (productCathegoryName!=null) {
            offerManagementService.addCategory(new ProductCategory(productCathegoryName));
            resp.getWriter().println("New product category <i>" + productCathegoryName + "</i> added.");
        } else { // display/redisplay the input form
            resp.getWriter().println("<html> <body>" +
                    "<form action=\"ProductCategoryManagement?action=" + ADD_NEW_PRODUCT_CATEGORY + "\" method=\"POST\">" +
                    "<br />" +
                    "Product name: <input type=\"text\" name=\"" + PRODUCT_CATEGORY_NAME_PARAM + "\" />" +
                    "<input type=\"submit\" value=\"Add Category\" />" +
                    "</form>" +
                    "</body></html>");
        }
    }
}
