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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

/**
 * Simple Test to show how the annotation can be used with together with before class annotation.
 *
 * @author florian
 * @version 2017-12-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
public class BaseJunitBeforeClassTest extends BaseDBHelper {

    @BeforeClass
    @FlywayTest(locationsForMigrate = {"loadMultibleSQLs"})
    public static void beforeClass() throws Exception {
    }

    @Before
    public void before() throws Exception {
        super.setup();
    }

    @Test
    public void firstTest() throws Exception {
        int res = countCustomer();

        assertEquals("Count of customer", 2, res);

        deleteCustomer();

        res = countCustomer();

        assertEquals("Count of customer", 0, res);
    }

    public void deleteCustomer() throws SQLException {
        Statement stmt = con.createStatement();

        try {
            stmt.execute("delete from customer");
            con.commit();

        } finally {
            stmt.close();
        }
    }
}
