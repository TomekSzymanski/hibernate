package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 Represents one customer oder
 */
public class Order {

    private int id;

    /** customer owning this order */
    private Customer customer;

    Order() {
        dateOrderCreated = new Date();
    }

    Order(ShoppingCart cart, Customer customer) {
        orderedGoods.putAll(cart.getProducts());
        dateOrderCreated = new Date();
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private OrderState state = OrderState.NEW;

    private Date dateOrderCreated; // TODO replace with LocalDate, integrate class implementig Enhanced user type

    private Map<Product, Integer> orderedGoods = new HashMap<>(); // products and their quantity. Price is within Product.

    public Date getDateOrderCreated() {
        return dateOrderCreated;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    void setDateOrderCreated(Date dateOrderCreated) {
        this.dateOrderCreated = dateOrderCreated;
    }

    public OrderState getState() {
        return state;
    }

    void setState(OrderState state) {
        this.state = state;
    }

    public Map<Product, Integer> getOrderedGoods() {
        return orderedGoods;
    }

    void setOrderedGoods(Map<Product, Integer> orderedGoods) {
        this.orderedGoods = orderedGoods;
    }
}
