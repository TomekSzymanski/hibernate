package serviceapi;

/**
 * Created on 2014-12-11.
 */
public interface Shop {
    OfferManagementService getOfferManagementService();

    ShoppingService getShoppingService();

    OrderExecutionService getOrderExecutionService();

}
