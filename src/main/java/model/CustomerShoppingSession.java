package model;

/**
 * Created on 2014-11-26.
 */
public class CustomerShoppingSession {
    private int id;
    private ShoppingCart cart = new ShoppingCart();

    /** described the stage of Shopping, from searching the products, to product selection, making payment and finally placing the order */
    public enum ShoppingStage {SHOPPING, GOODS_SELECTED, MAKING_PAYMENT, PAYMENT_ACCEPTED, ORDER_PLACED}

    private ShoppingStage stage = ShoppingStage.SHOPPING;

    private ShoppingService shoppingService;

    CustomerShoppingSession(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    /** the end customer doing this shopping session */
    private Customer customer;

    /* returns Shopping cart after adding new product */
    public ShoppingCart addToCart(Product product, int quantity)  {
        cart.add(product, quantity);
        return cart;
    }

    /** returns Shopping cart after removal */
    ShoppingCart removeFromCart(Product product)  {
        cart.remove(product);
        return cart;
    }

    public void addCustomerInfo(Customer customer) {
        this.customer = customer;
    }

    public boolean makePayment() {
        stage = ShoppingStage.PAYMENT_ACCEPTED;
        return true;
    }

    /**
     *
     * @return true if order was placed successfully
     */
    public boolean placeOrder() {
        if (stage == ShoppingStage.PAYMENT_ACCEPTED) {
            shoppingService.placeOrder(new Order(cart, customer));
            return true;
        }
        return false;
    }

}
