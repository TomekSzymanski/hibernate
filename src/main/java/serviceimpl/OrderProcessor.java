package serviceimpl;

import model.Order;
import serviceapi.OrderExecutionService;

import java.util.List;

/**
 * Created on 2014-12-11.
 */
public class OrderProcessor implements OrderExecutionService {

    @Override
    public List<Order> getAllOrders() {
        return HibernateUtil.prepareQuery("FROM Order");
    }
}
