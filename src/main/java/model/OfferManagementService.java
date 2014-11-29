package model;

/**
 * Created on 2014-11-26.
 */
public interface OfferManagementService {
    void addCategory(ProductCategory productCategory);

    void addProduct(Product product, int quantity);

    /** removes all product categories and all products */
    void clearAllInventory();
}
