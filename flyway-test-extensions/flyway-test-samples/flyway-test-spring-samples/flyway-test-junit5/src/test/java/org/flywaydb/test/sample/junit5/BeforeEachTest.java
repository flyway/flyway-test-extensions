/**
 * Copyright (C) 2011-2019 the original author or authors.
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