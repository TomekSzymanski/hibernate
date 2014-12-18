package serviceapi;

import model.ProductCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.fail;

/**
 * Created on 2014-12-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/SpringBeans.xml"}) // TT: IDE wie ze to jest maven, z kolei dla mavena test/resources to jest root dla resources --
public class OfferManagementServiceTest {

    @Autowired
    private OfferManagementService service;

    @Before
    public void clear() {
        service.clearAllInventory();
    }

    @Test
    public void tryToAddTheSameCategoryTwice() {
        //given:
        ProductCategory category = new ProductCategory("Mobile Phones");
        service.addCategory(category);

        try {
            // when the same category added ince again
            service.addCategory(category);
            fail("Exception should have been thrown");
            // then
        } catch (ApplicationException e) {}
    }
}
