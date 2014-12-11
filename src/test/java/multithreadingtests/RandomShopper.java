package multithreadingtests;

import model.Customer;
import model.Product;
import model.ProductCategory;
import serviceapi.CustomerShoppingSession;
import serviceapi.ShoppingService;

import java.util.List;
import java.util.Random;

/**
 * Created on 2014-11-30.
 */
public class RandomShopper implements Shopper {

    private ShoppingService shoppingService;

    private int maxOrders;

    public RandomShopper(ShoppingService shoppingService, int maxOrders) {
        this.shoppingService = shoppingService;
        this.maxOrders = maxOrders;
    }

    /**
     * Randomly chooses products from offer and places order.
     * Does shopping till reaches maxOrders amount.
     * It creates new Shopping session every time it starts Shopping
     */
    @Override
    public boolean shop() {
        boolean allShoppingSessionsSuccessful = true;
        for (int i = 0; i < maxOrders; i++) {
            allShoppingSessionsSuccessful &= doOneShop();
        }
        return allShoppingSessionsSuccessful;
    }

    private boolean doOneShop() {
        Random random = new Random();
        CustomerShoppingSession shoppingSession = shoppingService.createCustomerSession();
        List<ProductCategory> categories = shoppingService.getCategories();

        // select catetory on random
        ProductCategory selectedCategory = categories.get(random.nextInt(categories.size()));

        // select several products on rando
        List<Product> products = shoppingService.getProducts(selectedCategory);

        int numberOfproductsToShop = 1 + random.nextInt(3); // choose between 1 and 3 products

        // add random products to cart
        for (int i = 0; i < numberOfproductsToShop; i++) {
            // select product on random:
            int randomProductIdx = random.nextInt(products.size());
            int numberOfItems = 1 + random.nextInt(3); // choose between 1 and 3 items
            shoppingSession.addToCart(products.get(randomProductIdx), numberOfItems);
        }
        shoppingSession.addCustomerInfo(new Customer("some customer"));
        shoppingSession.makePayment();
        return shoppingSession.placeOrder();
    }

    @Override
    public Boolean call() throws Exception {
        return shop();
    }
}
