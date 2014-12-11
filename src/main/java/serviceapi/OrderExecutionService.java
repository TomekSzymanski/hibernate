package serviceapi;

import model.Order;

import java.util.List;

/**
 * Created on 2014-11-27.
 */
public interface OrderExecutionService {
    List<Order> getAllOrders();
}
