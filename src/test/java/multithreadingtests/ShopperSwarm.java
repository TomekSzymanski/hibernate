package multithreadingtests;


import model.Product;
import model.ProductCategory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import serviceapi.Shop;
import serviceapi.ShoppingService;
import serviceimpl.SimpleShop;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertTrue;

public class ShopperSwarm {

    private static final Shop shop = SimpleShop.getShopInstance();

    static {
        shop.getOfferManagementService().resetToEmpty();
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
    }

    /**
     * initializes Shop with big number of inventory
     */
    @BeforeClass
    public static void setUp() throws IOException {
        initializeShopInventory(shop, 10, 100);
    }

    @Test
    public void doSwarm() throws ExecutionException, InterruptedException {
        int maxOrders = 3;
        int maxConcurrentShoppers = 2;
        int numberOfShoppers = 4;
        doSwarm(shop, maxOrders, numberOfShoppers, maxConcurrentShoppers);
    }


    void doSwarm(Shop shop, int maxOrders, int numberOfShoppers, int maxConcurrentShoppers) throws InterruptedException, ExecutionException {
        // one ShoppingService for all Shoppers
        ShoppingService shoppingService = shop.getShoppingService();

        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("Shopper-%d").build();

        ExecutorService executors = Executors.newFixedThreadPool(maxConcurrentShoppers, threadFactory);

        CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(executors);

        // submit numberOfShoppers to start shopping
        for (int i = 0; i<numberOfShoppers; i++) {
            Shopper shopper = new RandomShopper(shoppingService, maxOrders);
            completionService.submit(shopper);
        }

        int finishedShoppersCount = 0;
        while (finishedShoppersCount < numberOfShoppers) {
            boolean shoppingSuccessful = completionService.take().get();
            finishedShoppersCount++;
            assertTrue(shoppingSuccessful);
        }
    }

    static void initializeShopInventory(Shop shop, int numberOfCategories, int numberOfProductsPerCathegory) throws IOException {
        Random random = new Random();

        Path dictionaryPath = Paths.get("src" + File.separator + "test" + File.separator + "resources" + File.separator + "multithreadingtests" + File.separator + "dictionary.US.txt");
        List<String> dictionary = Files.readAllLines(dictionaryPath);
        // first add cathegories
        for (int i = 0; i < numberOfCategories; i++) {
            ProductCategory category = new ProductCategory(dictionary.get(random.nextInt(dictionary.size())));
            shop.getOfferManagementService().addCategory(category);
            addProductsToCategory(shop, dictionary, category, numberOfProductsPerCathegory);
        }
    }

    static void addProductsToCategory(Shop shop, List<String> dictionary, ProductCategory category, int numberOfProductsPerCathegory) {
        final int QUANTITY_PER_PRODUCT = 10000;
        final BigDecimal PRICE_PER_PRODUCT = BigDecimal.TEN;
        Random random = new Random();
        for (int i = 0; i < numberOfProductsPerCathegory; i++) {
            String productName = category.getName() + "-" + dictionary.get(random.nextInt(dictionary.size()));
            shop.getOfferManagementService().addProduct(new Product(productName, category, PRICE_PER_PRODUCT), QUANTITY_PER_PRODUCT);
        }
    }
}
