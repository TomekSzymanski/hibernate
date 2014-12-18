package serviceimpl;

import serviceapi.*;

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

    @Override
    public AuthorizationService getAuthorizationService() {
        return new DBBasedAuthorizationService(); // TODO use Spring bean
    }
}
