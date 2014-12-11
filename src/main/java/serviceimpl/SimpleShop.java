package serviceimpl;

import serviceapi.OfferManagementService;
import serviceapi.OrderExecutionService;
import serviceapi.Shop;
import serviceapi.ShoppingService;

/**
 * Created on 2014-11-26.
 */
public class SimpleShop implements Shop {

    private SimpleShop() {
    }

    public static Shop getShopInstance() {
        return new SimpleShop();
    }

    @Override
    public OfferManagementService getOfferManagementService() {
        return new OfferManagementImpl();
    }

    @Override
    public ShoppingService getShoppingService() {
        return new ShoppingServiceImpl();
    }

    @Override
    public OrderExecutionService getOrderExecutionService() {
        return new OrderProcessor();
    }
}
