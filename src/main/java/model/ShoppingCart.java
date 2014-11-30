package model;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created on 2014-11-26.
 */
public class ShoppingCart {
    private int id;
    private Set<OrderItem> products = new LinkedHashSet<>();

    public void add(Product product, int quantity, BigDecimal unitPrice) {
        products.add(new OrderItem(product, quantity, unitPrice, null));
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public Set<OrderItem> getProducts() {
        return products;
    }
}
