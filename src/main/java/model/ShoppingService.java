package model;

import java.util.Iterator;
import java.util.List;

/**
 * Interface for all shopping functions from end user
 */
public interface ShoppingService {

    List<ProductCategory> getCategories();

    List<Product> getProducts(ProductCategory category);

    /** returns list of products from given category, that fulfill all criteria defined in filterFeatures */
    Iterator<Product> getProducts(ProductCategory category, ProductFeatures filterFeatures);

    void placeOrder(Order order);

    public CustomerShoppingSession createCustomerSession();

}
