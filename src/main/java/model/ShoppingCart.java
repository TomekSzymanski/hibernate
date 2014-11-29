package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2014-11-26.
 */
public class ShoppingCart {
    private int id;
    private Map<Product, Integer> products = new HashMap<>();

    public void add(Product product, int quantity) {
        products.put(product, quantity);
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}
