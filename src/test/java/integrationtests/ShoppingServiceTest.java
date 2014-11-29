package integrationtests;

import model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertFalse;

public class ShoppingServiceTest {

    @BeforeClass
    public static void setUp() {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
    }

    @Test
    public void getAllProductsByCategory() {
        // given
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory);
        Product smartTvTwo = new Product("Sony 4343", smartTvCategory);

        shop.addCategory(smartTvCategory);
        shop.addProduct(smartTvOne, 1);
        shop.addProduct(smartTvTwo, 1);

        // when
        ShoppingService shoppingService = shop.getShoppingService();
        Iterator<Product> selectedProducts = shoppingService.getProducts(smartTvCategory).iterator();

        assertTrue(selectedProducts.hasNext());
        assertEquals(smartTvOne, selectedProducts.next());
        assertEquals(smartTvTwo, selectedProducts.next());
        assertFalse(selectedProducts.hasNext());
    }

    @Test
    public void purchaseProduct() {
        // given
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory);
        Product smartTvTwo = new Product("Sony 4343", smartTvCategory);

        ProductCategory mobilePhoneCategory = new ProductCategory("Mobile Phones");
        Product nokiaMobile = new Product("Nokia Lumia 1000", mobilePhoneCategory);

        shop.addCategory(smartTvCategory);
        shop.addCategory(mobilePhoneCategory);
        shop.addProduct(smartTvOne, 10);
        shop.addProduct(smartTvTwo, 10);
        shop.addProduct(nokiaMobile, 10);

        // when: select one TV and one mobile:
        ShoppingService shoppingService = shop.getShoppingService();
        List<Product> smartTVs = shoppingService.getProducts(smartTvCategory);
        List<Product> mobiles =  shoppingService.getProducts(mobilePhoneCategory);

        Product selectedTV = smartTVs.get(0);
        Product selectedMobile = mobiles.get(0);

        // when you add to cart one TV and 2 mobile phones
        CustomerShoppingSession shoppingSession = shop.createCustomerSession();
        shoppingSession.addToCart(selectedTV, 1);
        shoppingSession.addToCart(selectedMobile, 2);

        //when we have made the payment and placed the order
        assertTrue("payment should be successful", shoppingSession.makePayment());
        shoppingSession.addCustomerInfo(new Customer("tomek"));
        assertTrue("upon making payment (we assume payment was successful), placeOrder() should return true"
                , shoppingSession.placeOrder());

        // then, shopping personel is able to see your order
        List<Order> orders = shop.getAllOrders();
        assertEquals(1, orders.size());

        Map<Product, Integer> orderedGoods = orders.get(0).getOrderedGoods();

        assertTrue(orderedGoods.containsKey(selectedTV));
        assertTrue(orderedGoods.containsKey(selectedMobile));

        assertEquals(1, orderedGoods.get(selectedTV).intValue());
        assertEquals(2, orderedGoods.get(selectedMobile).intValue());

        assertEquals("tomek", orders.get(0).getCustomer().getName());
    }

    @Test
    public void productOfferedAmountDepletedByOrderedAmount() {
        // given: there is a shop with 10 TVs offered
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory);

        shop.addCategory(smartTvCategory);
        shop.addProduct(smartTvOne, 10);

        // when some customer makes order for 2 TVs
        CustomerShoppingSession shoppingSession = shop.createCustomerSession();
        shoppingSession.addToCart(smartTvOne, 2);
        shoppingSession.makePayment();
        shoppingSession.addCustomerInfo(new Customer("tomek"));
        shoppingSession.placeOrder();

        // then there should be 8 left in the offer
        List<Product> productsOffered = shop.getShoppingService().getProducts(smartTvCategory);
        Assert.assertEquals(8, productsOffered.get(0).getAmountOffered());
    }

    @Test
    public void notPossibleToOrderWhenInventoryTooLow() {
        // given: there is a shop with 1 TVs offered
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory);

        shop.addCategory(smartTvCategory);
        shop.addProduct(smartTvOne, 1);

        // when some customer makes order for 2 TVs
        CustomerShoppingSession shoppingSession = shop.createCustomerSession();
        shoppingSession.addToCart(smartTvOne, 2);
        shoppingSession.makePayment();
        shoppingSession.addCustomerInfo(new Customer("tomek"));
        try {
            shoppingSession.placeOrder(); // inventory checked only at this stage, not when adding to the cart. // TODO add also the other check
        } catch (InventoryTooLowException e) {
            assertEquals(1, e.getAmountAvailable());
            assertEquals(2, e.getRequestedAmount());
            assertEquals(smartTvOne, e.getRequestedProduct());
        }
        fail("Exception on stock too low should be thrown");
    }
}