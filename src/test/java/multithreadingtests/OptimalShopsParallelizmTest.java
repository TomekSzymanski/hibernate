package multithreadingtests;

import serviceapi.Shop;
import org.junit.Ignore;
import org.junit.Test;
import serviceimpl.SimpleShop;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Ignore
public class OptimalShopsParallelizmTest {

    @Test
    public void runPerformanceBenchmarks() throws ExecutionException, InterruptedException, IOException {
        Shop shop = SimpleShop.getShopInstance();
        ShopperSwarm swarm = new ShopperSwarm();
        ShopperSwarm.initializeShopInventory(shop, 10, 500);

        int maxOrders = 8;
        int numberOfShoppersInTest = 64;

//        int MIN_TESTED_CONCURRENT_SHOPPERS = 1;
//        int MAX_TESTED_CONCURRENT_SHOPPERS = 8;
        int [] concurrenciesToTest = {1,2,3,4}; // TODO with higher numbers, there are mysql concurrency problems: how to work around them?

        int TEST_ITERATIONS = 5; // you cannot rely on one test only, you have to repeat N times and extract average

        Map<Integer, Long> durationPerConcurrencyMap = new HashMap<>();

//        for (int concurrency = MIN_TESTED_CONCURRENT_SHOPPERS; concurrency <= MAX_TESTED_CONCURRENT_SHOPPERS; concurrency++) {
        for (int concurrency : concurrenciesToTest) {
            long durationSum = 0;
            for (int i = 0; i< TEST_ITERATIONS; i++) {
                long start = System.currentTimeMillis();
                swarm.doSwarm(shop, maxOrders, numberOfShoppersInTest, concurrency);
                long duration = System.currentTimeMillis() - start;
                durationSum += duration;
            }
            durationPerConcurrencyMap.put(concurrency, durationSum / TEST_ITERATIONS);
        }

        System.out.println("Results: number of threads, execution time millis");
        System.out.println(durationPerConcurrencyMap);

        Map.Entry<Integer, Long> bestParallelism = durationPerConcurrencyMap.entrySet().stream()
                                    .sorted(Comparator.comparing(e -> e.getValue())).findFirst().get();
        System.out.println("Best parallelism for " + bestParallelism.getKey() + " threads . I took on average " + bestParallelism.getValue() + " millis");
    }
}
