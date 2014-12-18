package serviceimpl;

import model.Customer;
import model.Order;
import model.Product;
import model.ShoppingCart;
import serviceapi.CustomerShoppingSession;
import serviceapi.ShoppingService;

/**
 * Created on 2014-11-26.
 */
public class SimpleCustomerShoppingSession implements CustomerShoppingSession {
    private int id;
    private ShoppingCart cart = new ShoppingCart();

    private ShoppingStage stage = ShoppingStage.SHOPPING;

    private ShoppingService shoppingService;

    public SimpleCustomerShoppingSession(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    /** the end customer doing this shopping session */
    private Customer customer;

    /* returns Shopping cart after adding new product */
    @Override
    public ShoppingCart addToCart(Product product, int quantity)  {
        cart.add(product, quantity, product.getPrice());
        return cart;
    }

    /** returns Shopping cart after removal */
    ShoppingCart removeFromCart(Product product)  {
        cart.remove(product);
        return cart;
    }

    @Override
    public void addCustomerInfo(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean makePayment() {
        stage = ShoppingStage.PAYMENT_ACCEPTED;
        return true;
    }

    /**
     *
     * @return true if order was placed successfully
     */
    @Override
    public boolean placeOrder() {
        if (stage == ShoppingStage.PAYMENT_ACCEPTED) {
            shoppingService.placeOrder(new Order(cart, customer));
            return true;
        }
        return false;
    }

}
