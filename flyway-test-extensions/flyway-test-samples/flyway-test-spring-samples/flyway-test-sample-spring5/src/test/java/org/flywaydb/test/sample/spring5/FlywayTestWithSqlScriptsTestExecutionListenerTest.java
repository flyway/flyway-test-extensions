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
package org.flywaydb.test.sample.spring5;


import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
@RunWith(SpringRunner.class)
@FlywayTest
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
public class FlywayTestWithSqlScriptsTestExecutionListenerTest extends BaseDBHelper {

    @FlywayTest(locationsForMigrate = "loadmsql")
    @Test
    public void laodFlywayTestInsertWithoutSqlScriptExecutionTest() throws Exception {
        int countCustomer = countCustomer();

        assertThat("Only Flyway customer should be loaded.", countCustomer, is(2));
    }

    @FlywayTest
    @Sql(scripts = "/testSqlFiles/loadSqlScriptExecutionsTest.sql")
    @Test
    public void loadSqlScriptExecutionsWithoutFlywayTestInsertTest() throws Exception {
        int countCustomer = countCustomer();

        assertThat("Only Sql script customer should be loaded.", countCustomer, is(1));
    }

    @FlywayTest(locationsForMigrate = "loadmsql")
    @Sql(scripts = "/testSqlFiles/loadSqlScriptExecutionsTest.sql")
    @Test
    public void loadSqlScriptExecutionsWithFlywayTestInsertTest() throws Exception {
        int countCustomer = countCustomer();

        assertThat("All customer (Flyway and Sql script) should be loaded.", countCustomer, is(3));
    }
}
