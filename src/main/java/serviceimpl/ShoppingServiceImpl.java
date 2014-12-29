package serviceimpl;

import model.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import serviceapi.ApplicationException;
import serviceapi.InventoryTooLowException;
import serviceapi.ShoppingService;

import java.util.Iterator;
import java.util.List;

/**
* Created on 2014-11-30.
*/
@Service
class ShoppingServiceImpl implements ShoppingService {

    @Override
    public List<ProductCategory> getCategories() {
        List queryResults;
        Session session = HibernateUtil.getSessionFactory().openSession();
        queryResults = session.createQuery("FROM ProductCategory").list();
        session.close();
        return queryResults;
    }

    @Override
    public List<Product> getProducts(final ProductCategory category) {
        List queryResults;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Product WHERE productCategoryId = :category");
        query.setParameter("category", category.getId()); // TODO: why we have to provide ID. Why cannot it associate Product in DB by providing just instance of ProductCategory (it is not SQL of course)
        queryResults = query.list();
        session.close();
        return queryResults;
    }

    @Override
    public Iterator<Product> getProducts(ProductCategory category, ProductFeatures filterFeatures) {
        return null;
    }

    @Override
    public void placeOrder(Order order) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;

        try {
            trx = session.beginTransaction();

            // 1. decrease offered inventory amounts for all ordered products
            for (OrderItem orderItem : order.getOrderedGoods()) {
                int requestedQuantity = orderItem.getQuantity();
                Product productInInventory = (Product) session.createQuery("FROM Product WHERE id = :id") // TODO replace query for every element with one query for all
                        .setParameter("id", orderItem.getProduct().getId())
                        .uniqueResult();
                int quantityOffered = productInInventory.getAmountOffered();
                if (requestedQuantity > quantityOffered) {
                    throw new InventoryTooLowException(productInInventory, requestedQuantity, quantityOffered,
                            "cannot order " + requestedQuantity + " of product " + productInInventory + " . Only " + quantityOffered + " available");
                }
                productInInventory.setAmountOffered(productInInventory.getAmountOffered() - requestedQuantity);
            }
            // productInInventory will auto save at end of transaction

            // 2. and save the order
            session.save(order);
            trx.commit();
//        } catch (LockAcquisitionException lockException) { // TODO: correct exception handling??
//            if (lockException.getCause().getClass().equals(com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException.class)) {
//                // log and try restarting transaction
//                if (trx!=null) trx.rollback();
//                Logger.getLogger("serviceimpl.ShoppingServiceImpl").log(Level.WARNING, "MySQLTransactionRollbackException in thread " + Thread.currentThread().getName()
//                        + " when saving order " + order + " , retrying");
//                session.clear();
//                placeOrder(order); // retry
//            }
//            else {
//                if (trx!=null) trx.rollback();
//                throw new ApplicationException("unable to place order: " + order, lockException);
//            }
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("unable to place order: " + order, e);
        } finally {
            session.close();
        }
    }

    @Override
    public SimpleCustomerShoppingSession createCustomerSession() {
        return new SimpleCustomerShoppingSession(this);
    }

}
