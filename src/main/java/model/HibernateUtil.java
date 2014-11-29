package model;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created on 2014-11-27.
 */
class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure()
                    .addAnnotatedClass(Customer.class).addAnnotatedClass(ProductCategory.class);

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

    static Query prepareQuery(String hql) {
        return getSessionFactory().openSession().createQuery(hql);
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
            trx.rollback();
            throw new ApplicationException("unable to delete objects of " + entityName + " class", e); // TODO too low level message
        } finally {
            session.close();
        }
    }

}
