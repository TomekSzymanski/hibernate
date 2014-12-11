package serviceapi;

import model.Product;

/**
 * Created on 2014-11-29.
 */
public class InventoryTooLowException extends ApplicationException {
    private Product requestedProduct;

    private int requestedAmount;
    private int amountAvailable;

    public InventoryTooLowException(Product requestedProduct, int requestedAmount, int amountAvailable, String message) {
        super(message);
        this.requestedProduct = requestedProduct;
        this.requestedAmount = requestedAmount;
        this.amountAvailable = amountAvailable;
    }

    public Product getRequestedProduct() {
        return requestedProduct;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

}
