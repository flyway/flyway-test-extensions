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
package org.flywaydb.test.sample.junit;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.annotation.FlywayTests;
import org.flywaydb.test.sample.helper.BaseDBHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

/**
 * Simple Test to show how the annotation can be used inside test execution and use a
 * flywayContainerContext and {@link BeforeClass}.
 *
 * @author Florian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context/flywayContainerContext.xml", "/context/secondFlywayContainerContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
public class BaseJUnitFlywaysBeforeClassTest extends BaseDBHelper {

    @BeforeClass
    @FlywayTests(value = {
            @FlywayTest(flywayName = "flyway"),
            @FlywayTest(locationsForMigrate = {"basetest2", "loadMultipleSQLs2"}, overrideLocations = true, flywayName = "flyway2")
    })
    public static void beforeClass() {
    }


    /**
     * Made a clean init migrate usage before execution of test methods
     */
    @Test
    public void dummyTestMethodLoad() throws Exception {
        int res = countCustomer();
        int res2 = countCustomer2();

        assertEquals("Count of customer", 0, res);
        assertEquals("Count of customer2", 2, res2);
    }

    private int countCustomer2() throws Exception {
        DataSource ds = (DataSource) context.getBean("dataSourceRef2");
        int result = -1;

        Statement stmt = ds.getConnection().createStatement();
        String query = "select count(*) from Customer2";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        Long cnt = rs.getLong(1);
        result = cnt.intValue();

        rs.close();
        stmt.close();

        return result;
    }

}
