/**
 * Copyright (C) 2010-2011 the original author or authors.
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

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.googlecode.flyway.test.junit.FlywayTestExecutionListener;

/**
 * Simple Test to show how the annotation can be used inside test execution
 * 
 * @author florian
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context/simple_applicationContext_spring256.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		FlywayTestExecutionListener.class })
@FlywayTest // attention this does not work
public class Spring256JUnitTest extends BaseDBHelper {

	/**
	 * Normal test method nothing done per startup
	 */
	@Test
	public void dummyTestNoLoad() throws Exception {
		//int res = countCustomer();

		// we cant made a test here because the result depends from previous test
		//		Assert.assertEquals("Count of customer", 0, res);
	}

	/**
	 * Made a clean init migrate usage before execution of test methodes
	 */
	@Test
	@FlywayTest
	public void dummyTestMethodLoad() throws Exception {
		int res = countCustomer();

		Assert.assertEquals("Count of customer", 0, res);
	}

	/**
	 * Made a clean init migrate usage before execution of test methodes
	 */
	@Test
	@FlywayTest(baseDirsForMigrate = { "sampletest256", "loadmsql" })
	public void loadMultibleSQLs() throws Exception {
		int res = countCustomer();

		Assert.assertEquals("Count of customer", 2, res);
	}

}
