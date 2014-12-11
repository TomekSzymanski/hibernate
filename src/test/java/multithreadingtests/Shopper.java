package multithreadingtests;

import java.util.concurrent.Callable;

/**
 * Created on 2014-11-30.
 */
public interface Shopper extends Callable<Boolean> {

    /** does shopping: selects products and places order. Returns true if (all) shopping was successful */
    public boolean shop();
}
