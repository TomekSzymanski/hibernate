package serviceimpl;

import model.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import serviceapi.ApplicationException;

import java.util.List;

/**
 * Created on 2014-11-27.
 */
class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure()
                    .addAnnotatedClass(Customer.class) // TODO: how not to specify all
                    .addAnnotatedClass(ProductCategory.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(OrderItem.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable e) {
            throw new ApplicationException("unable to load configuration", e);
        }
    }

    static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    static List prepareQuery(String hql) {
        List queryResults;
        Session session = getSessionFactory().openSession();
        // TODO http://stackoverflow.com/questions/1957588/why-i-have-to-declare-each-and-every-class-in-my-hibernate-cfg-xml-when-using-an
        // TODO http://stackoverflow.com/questions/13499450/spring-hibernate-auto-discover-annotated-classes
        Query query = session.createQuery(hql);
        queryResults = query.list();
        session.close();
        return queryResults;
    }

    static void deleteAll(String entityName) {
        Session session = getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;
        try {
            trx = session.beginTransaction();
            Query deleteQuery = session.createQuery(String.format("DELETE FROM %s", entityName));
            deleteQuery.executeUpdate();
            trx.commit();
        } catch (HibernateException e) {
            if(trx!=null) trx.rollback();
            throw new ApplicationException("unable to delete objects of " + entityName + " class", e); // TODO too low level message
        } finally {
            session.close();
        }
    }

}
