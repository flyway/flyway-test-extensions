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
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;


/**
 * Simple Test to show how the annotation can be used inside test execution.</p>
 *
 * The test class use the annotation during test class setup.
 *
 * @author florian
 *
 */
@ExtendWith({SpringExtension.class})
@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		FlywayTestExecutionListener.class })
@FlywayTest
public class Spring5JUnitTest extends BaseDBHelper {
    private final Log logger = LogFactory.getLog(getClass());

    @AfterEach
    public void after(TestInfo testName) {
        Flyway flyway = context.getBean(Flyway.class);

        testName.getTestMethod() //
         .ifPresent( s -> logger.info( String.format("\t***** AFTER %s **********", s.getName())) );

       MigrationInfoService
               info = flyway.info();
        System.out.println(info);
        MigrationInfo[] mig = info.all();
        for ( MigrationInfo mi : mig ) {
            logger.info(String.format("\t%s\t%s\t%s", mi.getVersion(), mi.getScript(), mi.getType()));
        }
        List list = Arrays.asList(info.all());
        logger.info("\t***** AFTER **********");
    }

	/**
	 * Normal test method nothing done per startup.
	 * All startup code is be done during class setup.
	 */
	@Test
	public void dummyTestNoLoad() throws Exception {
		int res = countCustomer();

        assertThat("This test must runs without an error, because we can not guarantee that this test method run as first. " + res, res , greaterThan(0)  );
    }

	/**
	 * Made a clean init migrate usage before execution of test method.
	 * SQL statements will be loaded from the default location.
	 */
	@Test
	@FlywayTest
	public void dummyTestMethodLoad() throws Exception {
		int res = countCustomer();

		assertThat("Count of customer", res, is(0));
	}

	/**
	 * Made a clean init migrate usage before execution of test method and
	 * load SQL statements from two directories.
	 */
	@Test
	@FlywayTest(locationsForMigrate = {"loadmsql"})
	public void loadMultibleSQLs() throws Exception {
		int res = countCustomer();

		assertThat("Count of customer", res, is(2));
	}

    /**
     * Made a clean init migrate usage before execution of test method.
     * SQL statements will be loaded from the default location.
     */
    @Test
    @FlywayTest(invokeBaselineDB=true)
    public void testMethodLoadWithBaseline() throws Exception {
        int res = countCustomer();

        assertThat("Count of customer", res, is(0));
    }
}
