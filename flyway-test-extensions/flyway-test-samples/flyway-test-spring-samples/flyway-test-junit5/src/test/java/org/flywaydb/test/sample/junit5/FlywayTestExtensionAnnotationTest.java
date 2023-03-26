/**
 * Copyright (C) 2011-2023 the original author or authors.
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
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
@FlywayTestExtension
@FlywayTest
public class FlywayTestExtensionAnnotationTest extends BaseDBHelper {
    private int beforeTestCount;

    @BeforeEach
    public void beforeEach() throws Exception {
        super.baseBeforeEach();
        beforeTestCount = countCustomer();
    }


    /**
     * Normal test method nothing done per startup
     */
    @Test
    public void dummyTestNoLoad() throws Exception {
        int res = countCustomer();

        assertThat(
                "This test must runs without an error, because we can not guarantee that this test method run as first.",
                res,
                is(beforeTestCount));
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest
    public void dummyTestMethodLoad() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(0));
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest(locationsForMigrate = { "sampletest5", "loadmsqlbefore" }, overrideLocations = true)
    public void loadMultibleSQLsOverrideLocations() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(3));
    }

    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    @FlywayTest(locationsForMigrate = { "loadmsql" })
    public void loadMultibleSQLsLocations() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(2));
    }

}