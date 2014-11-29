package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent all product fetures, like dimension, power consumption, weight, screen size, sound power, whatever
 */

public class ProductFeatures {

    private Map<String, Object> features = new HashMap<>();

    public void add(String key, Object value) {
        features.put(key, value);
    }

    public Object get(String key) {
        return features.get(key);
    }
}
