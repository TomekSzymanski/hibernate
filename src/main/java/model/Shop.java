package model;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Created on 2014-11-26.
 */
public class Shop implements OfferManagementService, OrderExecution {

    private Shop() {
    }

    public static Shop getShopInstance() {
        return new Shop();
    }

    public CustomerShoppingSession createCustomerSession() {
        return new CustomerShoppingSession(getShoppingService());
    }

    @Override
    public void addCategory(ProductCategory productCategory) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;

        try {
            trx = session.beginTransaction();
            // check first if such category does not exist
            // TODO: should we do it this way or rely in unique constraint and then handle it in exception:
            // Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Smart TV' for key 'name'
            Query query = session.createQuery("SELECT 1 FROM ProductCategory WHERE id=:id");
            query.setParameter("id", productCategory.getId());
            if (query.uniqueResult() != null) {
                trx.rollback();
                throw new ApplicationException("Category " + productCategory.getName() + " already exists.");
            }
            session.save(productCategory);
            trx.commit();
        } catch (HibernateException e) { // TODO check if there is concreate child of Hibernate exception
            trx.rollback();
            throw new ApplicationException("unable to add new product", e);
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
            trx.rollback();
            throw new ApplicationException("unable to add new product", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void clearAllInventory() {
        HibernateUtil.deleteAll("Product");
        HibernateUtil.deleteAll("ProductCategory");
    }

    @Override
    public List<Order> getAllOrders() {
        Query allOrdersQuery = HibernateUtil.prepareQuery("FROM Order");
        return allOrdersQuery.list();
    }

    public ShoppingService getShoppingService() {
        return new ShoppingServiceImpl();
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
            trx.rollback();
            throw new ApplicationException("unable to clear all orders", e);
        } finally {
            session.close();
        }

    }

    private class ShoppingServiceImpl implements ShoppingService {

        @Override
        public List<ProductCategory> getCategories() {
            Query query = HibernateUtil.prepareQuery("FROM ProductCategories");
            return query.list();
        }

        @Override
        public List<Product> getProducts(final ProductCategory category) {
            Query query = HibernateUtil.prepareQuery("FROM Product WHERE productCategoryId = :category");
            query.setParameter("category", category.getId()); // TODO: why we have to provide ID. Why cannot it associate Product in DB by providing just instance of ProductCategory (it is not SQL of course)
            return query.list();
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
                    Product productInInventory = (Product)session.createQuery("FROM Product WHERE id = :id") // TODO replace query for every element with one query for all
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
            } catch (HibernateException e) {
                trx.rollback();
                throw new ApplicationException("unable to place order: " + order, e);
            } finally {
                session.close();
            }
        }

    }

}
