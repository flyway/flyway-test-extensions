/**
 * Copyright (C) 2011-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.test.sample.junit5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
 * Simple Test to show {@link FlywayTest} annotation together with {@link #org.junit.jupiter.api.BeforeAll}
 * annotation.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@PropertySource("classpath:flyway.properties ")

public class BeforeAllTest extends BaseDBHelper {
    private final Log logger = LogFactory.getLog(getClass());

    private static int customerCount = 2;

    @BeforeAll
    @FlywayTest(locationsForMigrate = {"loadmsql"})
    public static void beforeAll() {
    }

    @AfterEach
    public void afterEach(TestInfo testInfo) {
        customerCount++;
        logger.info(String.format("After %s test the customer count must be %d.", testInfo.getTestMethod(), customerCount));
    }

    @Test
    public void simpleCountWithoutAny(TestInfo testName) throws Exception {
        int res = countCustomer();

        assertThat("Customer count before add ", res, is(customerCount));

        addCustomer("simpleCountWithoutAny");

        res = countCustomer();

        assertThat("Count of customer after add ", res, is(customerCount + 1));

    }

    @Test
    public void additionalCountWithoutAny(TestInfo testName) throws Exception {
        int res = countCustomer();

        assertThat("Customer count before add ", res, is(customerCount));

        addCustomer("additionalCountWithoutAny");

        res = countCustomer();
        assertThat("Count of customer after add ", res, is(customerCount + 1));
    }

}
