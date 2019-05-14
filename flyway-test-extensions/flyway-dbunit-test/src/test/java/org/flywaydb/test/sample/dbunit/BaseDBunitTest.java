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
package org.flywaydb.test.sample.dbunit;


import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.dbunit.DBUnitSupport;
import org.flywaydb.test.dbunit.FlywayDBUnitTestExecutionListener;
import org.flywaydb.test.sample.helper.BaseDBHelper;

/**
 * Simple Test to show how the annotation {@link DBUnitSupport}
 * can be used together with {@link FlywayTest} inside test execution
 *
 * During startup we made a reset of the database and afterwards we
 * insert some special SQL statements with dbunit.
 *
 * @author florian
 *
 * @version 1.7
 * @version 2012-01-06
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		FlywayDBUnitTestExecutionListener.class })
@FlywayTest
@DBUnitSupport(loadFilesForRun = { "INSERT", "/dbunit/dbunit.cus1.xml" })
public class BaseDBunitTest extends BaseDBHelper {


	/**
	 * Normal test method nothing done per startup
	 */
	@Test
	public void dummyTestNoLoad() throws Exception {
		int res = countCustomer();

		Assert.assertEquals("Count of customer", 2, res);
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
	 * Made a clean init migrate and insert sql with dbunit
	 */
	@Test
	@FlywayTest
	@DBUnitSupport(loadFilesForRun = { "INSERT", "/dbunit/dbunit.cus1.xml" })
	public void loadDBUnitSQLs() throws Exception {
		int res = countCustomer();

		Assert.assertEquals("Count of customer", 2, res);
	}

	/**
	 * Made a clean init migrate with inserts from flyway and store it
	 * afterwards
	 */
	@Test
	@FlywayTest(locationsForMigrate = { "loadMultibleSQLs" })
	@DBUnitSupport(saveTableAfterRun = { "CUSTOMER", "select * from CUSTOMER" }, saveFileAfterRun = "target/dbunitresult/customer1.xml")
	public void storeDBUnitSQLs() throws Exception {
		int res = countCustomer();

		Assert.assertEquals("Count of customer", 2, res);
	}

}
