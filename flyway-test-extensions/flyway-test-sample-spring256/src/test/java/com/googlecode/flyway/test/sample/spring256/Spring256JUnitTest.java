/**
 * Copyright (C) 2011-2014 the original author or authors.
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
package com.googlecode.flyway.test.sample.spring256;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.googlecode.flyway.test.junit.FlywayTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple Test to show how the annotation can be used inside test execution.
 *
 * In this example it will be used spring version 2.5.6
 *
 * @author florian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context/simple_applicationContext_spring256.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		FlywayTestExecutionListener.class })
@FlywayTest // Attention: this will be done nothing with spring 2.5.6
public class Spring256JUnitTest extends BaseDBHelper {

	/**
	 * Normal test method nothing done per startup. With Spring 2.5.6 we
	 * have no database setup during test class setup.
	 */
	@Test
	public void dummyTestNoLoad() throws Exception {
        int res = countCustomer();

        assertTrue("This test must runs without an error, because we can not guarantee that this test method run as first. " + res, true);
    }

	/**
	 * Made a clean init migrate usage before execution of test method.
	 */
	@Test
	@FlywayTest
	public void dummyTestMethodLoad() throws Exception {
		int res = countCustomer();

		assertEquals("Count of customer", 0, res);
	}

	/**
	 * Made a clean init migrate usage before execution of test method.
	 * It will be invoke two migration steps, one for <b>sampletest256</b> and than
	 * <b>loadmsql</b>.
	 */
	@Test
	@FlywayTest(locationsForMigrate = { "sampletest256", "loadmsql" })
	public void loadMultibleSQLs() throws Exception {
		int res = countCustomer();

		assertEquals("Count of customer", 2, res);
	}

}
