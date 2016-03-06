/**
 * Copyright (C) 2011-2016 the original author or authors.
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
package org.flywaydb.test.sample.dbunit;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.dbunit.DBUnitSupport;
import org.flywaydb.test.dbunit.FlywayDBUnitTestExecutionListener;
import org.flywaydb.test.sample.helper.BaseDBHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Sample test to verify Issue 16 leaking database connection.
 *
 * @author Florian
 * @version 2.2.1
 * @since 2.2.1
 * @version 2013-09-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context/dbunit_issue16_connection_applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        FlywayDBUnitTestExecutionListener.class })
@FlywayTest
public class Issue16DbUnitTestExecutionListenerDbUnitTest extends BaseDBHelper {
    /**
     * Normal test method nothing done per startup
     */
    @Test
    @Repeat(10) // 10 executions are enough
    @FlywayTest
    @DBUnitSupport(loadFilesForRun = { "INSERT", "/dbunit/dbunit.cus1.xml" })
    public void repeatLoad() throws Exception {
        int res = countCustomer();

        Assert.assertEquals("Count of customer", 2, res);
    }

    /**
     * Made a clean init migrate with inserts from flyway and store it
     * afterwards
     */
    @Test
    @Repeat(10) // 10 executions are enough
    @FlywayTest(locationsForMigrate = { "loadMultibleSQLs" })
    @DBUnitSupport(saveTableAfterRun = { "CUSTOMER", "select * from CUSTOMER" }, saveFileAfterRun = "target/dbunitresult/Issue16DbCustomer.xml")
    public void storeRepeat() throws Exception {
        int res = countCustomer();

        Assert.assertEquals("Count of customer", 2, res);
    }

}