package serviceimpl;

import model.LoggedUser;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import serviceapi.ApplicationException;
import serviceapi.AuthorizationService;
import serviceapi.UserAlreadyExistsException;

/**
 * Created on 2014-12-18.
 */
public class DBBasedAuthorizationService implements AuthorizationService {

    @Override
    public void register(String login, String password) {
        LoggedUser newUser = new LoggedUser(login, hashPassword(password));

        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;
        try {
            trx = session.beginTransaction();
            session.save(newUser);
            trx.commit();
        } catch (ConstraintViolationException e) {
            if (trx != null) trx.rollback();
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists", e);
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("Unable to add new user", e);
        } finally {
            session.close();
        }
    }

    private String hashPassword(String password) {
        String salt = "someSalt34t435";
        byte[] passHash = org.apache.commons.codec.digest.DigestUtils.sha512(password + salt);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< passHash.length ;i++) {
            sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    public boolean validate(String login, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery("FROM LoggedUser WHERE login = :login AND password = :hashedPassword");
        query.setParameter("login", login);
        query.setParameter("hashedPassword", hashPassword(password));

        return (query.uniqueResult() != null);
    }

    @Override
    public void delete(String userLogin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction trx = null;
        try {
            trx = session.beginTransaction();
            Query query = session.createQuery("DELETE LoggedUser WHERE login = :login");
            query.setParameter("login", userLogin);
            query.executeUpdate();
            trx.commit();
        } catch (HibernateException e) {
            if (trx!=null) trx.rollback();
            throw new ApplicationException("Unable to delete user " + userLogin, e);
        } finally {
            session.close();
        }
    }
}
