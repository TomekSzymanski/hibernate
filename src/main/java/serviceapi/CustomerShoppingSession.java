package serviceapi;

import model.Customer;
import model.Product;
import model.ShoppingCart;

/**
 * Created on 2014-12-11.
 */
public interface CustomerShoppingSession {
    /* returns Shopping cart after adding new product */
    ShoppingCart addToCart(Product product, int quantity);

    void addCustomerInfo(Customer customer);

    boolean makePayment();

    boolean placeOrder();

    /** described the stage of Shopping, from searching the products, to product selection, making payment and finally placing the order */
    public enum ShoppingStage {SHOPPING, GOODS_SELECTED, MAKING_PAYMENT, PAYMENT_ACCEPTED, ORDER_PLACED}
}
