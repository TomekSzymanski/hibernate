package model;

import javax.persistence.*;

/**
 * Created on 2014-12-18.
 */
@Entity
public class LoggedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column (nullable = false)
    private String login;

    @Column (nullable = false)
    private String password;

    public LoggedUser() {
    }

    public LoggedUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoggedUser that = (LoggedUser) o;

        if (!login.equals(that.login)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
