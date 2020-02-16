/**
 * Copyright (C) 2011-2020 the original author or authors.
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

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
@ExtendWith(SpringExtension.class)
@ExtendWith(FlywayTestExtension.class)
@FlywayTest
public class FlywayTestExtensionTest extends BaseDBHelper {
    private int beforeTestCount;

    @BeforeEach
    public void setup() throws Exception {
        super.setup();
        beforeTestCount = countCustomer();
    }

    /**
     * Normal test method nothing done per startup
     */
    @Test
    public void dummyTestNoLoad() throws Exception {
        int res = countCustomer();

        assertEquals("This test must runs without an error, because we can not guarantee that this test method run as "
                             + "first. ", beforeTestCount, res);
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
    @FlywayTest(locationsForMigrate = { "basetest", "loadMultibleSQLs" }, overrideLocations = true)
    public void loadMultibleSQLsOverrideLocations() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest(locationsForMigrate = { "loadMultibleSQLs" })
    public void loadMultibleSQLsLocations() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);
    }
}
