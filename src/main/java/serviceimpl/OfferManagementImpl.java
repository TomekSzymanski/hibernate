package serviceimpl;

import model.Product;
import model.ProductCategory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import serviceapi.ApplicationException;
import serviceapi.OfferManagementService;

/**
 * Created on 2014-12-11.
 */
@Repository("dbOrderManagement") // bo Repository robili wrappowanie wyjatkow low level na jeden DataAccessException.
public class OfferManagementImpl implements OfferManagementService {

    @Override
    public void addCategory(ProductCategory productCategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;

        try {
            trx = session.beginTransaction();
            session.save(productCategory);
            trx.commit();

        } catch (ConstraintViolationException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("Category with name '" + productCategory.getName() + "' already exists", e);
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("Unable to add new product category", e);
        } finally {
            session.close();
        }
    }


    @Override
    public void addProduct(Product product, int quantity) { // TODO so quantity should not sit in product
        product.setAmountOffered(quantity);

        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;

        try {
            trx = session.beginTransaction();
            session.save(product); // TODO: should not we rather keep in this class a collection of products, customers, orders, etc, operate on those collections, and Session.save them on every change? (or dirty checking will save them?)
            trx.commit();
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("unable to add new product", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void clearAllInventory() {
        HibernateUtil.deleteAll("Product");
        HibernateUtil.deleteAll("ProductCategoryAlias");
        HibernateUtil.deleteAll("ProductCategory");
    }

    /** resets this shop to totally empty: no products, no product categories, no orders, etc. No historical infomarmation */
    // needed for unit tests only
    public void resetToEmpty() { //TODO: how to hide it from client, but still have in unit integration tests that are from different package? Or have in unit tests clear shop accessible somehow?
        clearAllOrders();
        clearAllCustomers();
        clearAllInventory();
    }

    private void clearAllCustomers() {
        HibernateUtil.deleteAll("Customer");
    }

    private void clearAllOrders() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
//            List<Order> allOrdersToDelete = session.createQuery("FROM Order").list();
//            for(Order order : allOrdersToDelete) {
//                order.getOrderedGoods().forEach(i -> session.delete(i)); // TODO: why cascading delete from Order does not work? nie dziala wlasnie bo mam te cudaczne kolekcje a nie encje
//                session.delete(order);
//            }
            session.createQuery("DELETE FROM OrderItem").executeUpdate(); // TODO: why we manually delete both??
            session.createQuery("DELETE FROM Order").executeUpdate();
            trx.commit();
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("unable to clear all orders", e);
        } finally {
            session.close();
        }

    }
}
