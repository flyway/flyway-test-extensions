package org.flywaydb.test.sample.junit5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Simple Test to show {@link FlywayTest} annotation together with {@link #org.junit.jupiter.api.BeforeEach}
 * annotation.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@PropertySource("classpath:flyway.properties ")
public class BeforeEachTest extends BaseDBHelper {
    private final Log logger = LogFactory.getLog(getClass());

    @BeforeEach
    @FlywayTest(locationsForMigrate = {"loadmsql"})
    public void beforeEach() {

    }


    @AfterEach
    public void after(TestInfo testName) throws Exception {
        logger.info(String.format("End of test '%s' with customer count %d.",
                testName.getTestMethod().toString(),
                countCustomer()));
    }

    @Test
    public void simpleCountWithoutAny(TestInfo testName) throws Exception {
        int res = countCustomer();

        assertThat("Customer count before add ", res, is(2));

        addCustomer("simpleCountWithoutAny");

        res = countCustomer();

        assertThat("Count of customer after add ", res, is(3));

    }

    @Test
    public void additionalCountWithoutAny(TestInfo testName) throws Exception {
        int res = countCustomer();

        assertThat("Customer count before add ", res, is(2));

        addCustomer("additionalCountWithoutAny");

        res = countCustomer();
        assertThat("Count of customer after add ", res, is(3));
    }

}