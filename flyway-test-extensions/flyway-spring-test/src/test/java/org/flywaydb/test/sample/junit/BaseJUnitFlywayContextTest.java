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
package org.flywaydb.test.sample.junit;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.sample.helper.BaseDBHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple Test to show how the annotation can be used inside test execution and use a
 * flywayContainerContext.
 *
 * @author florian
 * @version 2.0
 * @version 2013-04-06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context/flywayContainerContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@FlywayTest
public class BaseJUnitFlywayContextTest extends BaseDBHelper {

    /**
     * Normal test method nothing done per startup
     */
    @Test
    public void dummyTestNoLoad() throws Exception {
        int res = countCustomer();

        assertTrue("This test must runs without an error, because we can not guarantee that this test method run as first. " + res, true);
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest
    public void dummyTestMethodLoad() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 0, res);
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest(locationsForMigrate = {"basetest", "loadMultibleSQLs"}, overrideLocations = true)
    public void loadMultibleSQLsOverrideLocations() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest(locationsForMigrate = {"loadMultibleSQLs"})
    public void loadMultibleSQLsLocations() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);
    }

}
