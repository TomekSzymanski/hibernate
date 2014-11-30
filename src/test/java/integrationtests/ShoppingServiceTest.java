package integrationtests;

import model.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.*;
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
        shop.resetToEmpty(); // TODO: do not export this method, move it to test, make it as a script. use in memory database, initialize it with your scripts (DDLs)
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory, BigDecimal.valueOf(2000));
        Product smartTvTwo = new Product("Sony 4343", smartTvCategory, BigDecimal.valueOf(2300));

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
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory, BigDecimal.valueOf(2000));
        Product smartTvTwo = new Product("Sony 4343", smartTvCategory, BigDecimal.valueOf(2300));

        ProductCategory mobilePhoneCategory = new ProductCategory("Mobile Phones");
        Product nokiaMobile = new Product("Nokia Lumia 1000", mobilePhoneCategory, BigDecimal.valueOf(760));

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

        Order firstOrder = orders.get(0);

        assertTrue(firstOrder.containsProduct(selectedTV));
        assertTrue(firstOrder.containsProduct(selectedMobile));

        assertEquals(1, firstOrder.getProductQuantity(selectedTV));
        assertEquals(2, firstOrder.getProductQuantity(selectedMobile));

        assertEquals("tomek", orders.get(0).getCustomer().getName());
    }

    @Test
    public void productOfferedAmountDepletedByOrderedAmount() {
        // given: there is a shop with 10 TVs offered
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory, BigDecimal.valueOf(2000));

        shop.addCategory(smartTvCategory);
        shop.addProduct(smartTvOne, 10);

        // when some customer makes order for 2 TVs
        CustomerShoppingSession shoppingSession = shop.createCustomerSession();
        shoppingSession.addToCart(smartTvOne, 2);
        shoppingSession.makePayment();
        shoppingSession.addCustomerInfo(new Customer("tomek"));
        shoppingSession.placeOrder();

        // then there should be 8 left in the offer
        List<Product> productsStillOfferred = shop.getShoppingService().getProducts(smartTvCategory);
        Assert.assertEquals(8, productsStillOfferred.get(0).getAmountOffered());
    }

    @Test
    public void notPossibleToOrderWhenInventoryTooLow() {
        // given: there is a shop with 1 TVs offered
        Shop shop = Shop.getShopInstance();
        shop.resetToEmpty();
        ProductCategory smartTvCategory = new ProductCategory("Smart TV");
        Product smartTvOne = new Product("Samsung ER345L", smartTvCategory, BigDecimal.valueOf(2000));

        shop.addCategory(smartTvCategory);
        shop.addProduct(smartTvOne, 1);

        // when some customer makes order for 2 TVs
        CustomerShoppingSession shoppingSession = shop.createCustomerSession();
        shoppingSession.addToCart(smartTvOne, 2);
        shoppingSession.makePayment();
        shoppingSession.addCustomerInfo(new Customer("tomek"));
        try {
            shoppingSession.placeOrder(); // inventory checked only at this stage, not when adding to the cart. // TODO add also the other check
            fail("Exception on stock too low should be thrown");
        } catch (InventoryTooLowException e) {
            assertEquals(1, e.getAmountAvailable());
            assertEquals(2, e.getRequestedAmount());
            assertEquals(smartTvOne, e.getRequestedProduct());
        }
    }
}