/**
 * Copyright (C) 2011-2012 the original author or authors.
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
package com.googlecode.flyway.test.junit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.test.ExecutionListenerHelper;
import com.googlecode.flyway.test.annotation.FlywayTest;

/**
 * Spring test execution listener to get annotation {@link FlywayTest} up and
 * running
 *
 * <p>
 * If the annotation {@link FlywayTest} used on class level a clean , init ,
 * migrate cycle is done during class load.<br>
 * If the annotation
 * {@link FlywayTest} used on test method level a clean , init , migrate cycle
 * is done before test execution.
 * </p>
 *
 * <p>Important if the annotation {@link FlywayTest} are used the system properties
 * for <code>jdbc.url</code>, <code>jdbc.driver</code>,
 * <code>jdbc.username</code>, and <code>jdbc.password</code> should be set.</p>
 * Also the test application context should contains code like this:
 *
 * <pre>
 * {@code
 * <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
 *   <property name="location" value="dbc.properties"/>
 *   <property name="ignoreResourceNotFound" value="true"/>
 * </bean>
 *
 * <bean id="flyway" class="com.googlecode.flyway.core.Flyway" depends-on="data.source.id">
 *   <property name="dataSource" ref="data.source.id"/>
 *   <property name="baseDir" value="oracle"/>
 * </bean>
 *
 *     <!-- H2 Setup
 *     -Djdbc.driver=org.h2.Driver
 *  -Djdbc.url=jdbc:h2:./db/testCaseDb
 *  -Djdbc.username=OC_MORE_TEST
 *  -Djdbc.password=OC_MORE_TEST
 *      -->
 *  <bean id="dataSourceRef" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
 *        <property name="driverClassName"><value>$jdbc.driver</value></property>
 *        <property name="url"><value>$jdbc.url</value></property>
 *        <property name="username"><value>$jdbc.username</value></property>
 *        <property name="password"><value>$jdbc.password</value></property>
 *  </bean>
 * }
 * </pre>
 *
 * If this setup is used exist the possibility to run test again different
 * database such as H2 or Oracle.
 * <p/>
 * Usage inside the test class
 *
 * <pre>
 * &#064;RunWith(SpringJUnit4ClassRunner.class)
 * &#064;ContextConfiguration(locations={"/context/simple_applicationContext.xml"})
 * &#064;TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
 *    FlywayTestExecutionListener.class})
 * &#064;FlywayTest
 * public class SimpleLoadTest
 * </pre>
 *
 * </p>
 * <b>Notes:</b>
 * <ul>
 * <li>If you using spring framework version lower than 3.x the
 * annotation {@link FlywayTest} wont work at class level.</li>
 * <li>For spring
 * framework version 2.5.6 use simple_applicationContext_spring256.xml as
 * application context example</li>
 * <li>If you using the annotation
 * {@link FlywayTest} more than one time in test classes than <b>do not</b> use
 * parallel execution in surefire plugin.</bR> With this option you will setup
 * your database in more than one thread parallel!</li>
 * </ul>
 *
 * </p>
 *
 * @author Florian
 *
 * @version 2011-12-10
 * @version 1.0
 *
 */
public class FlywayTestExecutionListener implements TestExecutionListener {

   /**
	 * Used for logging inside test executions.
	 */
	// @@ Construction
	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * Allocates new <code>AbstractDbSpringContextTests</code> instance.
	 */
	public FlywayTestExecutionListener() {
	}

	/**
	 * @return the instance of logger.
	 */
	protected Log getLogger() {
		return logger;
	}

	/**
	 * Invoke this method before test class will be created.</p>
	 *
	 * <b>Attention:</b> This will be only invoked if spring version &gt;= 3.x
	 * are used.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 *
	 * @throws Exception if any error occurred
	 */
	public void beforeTestClass(final TestContext testContext) throws Exception {
		// no we check for the DBResetForClass
		final Class<?> testClass = testContext.getTestClass();

		final Annotation annotation = testClass.getAnnotation(FlywayTest.class);

		dbResetWithAnotation(testContext, (FlywayTest) annotation);
	}

	/**
	 * no implementation for annotation {@link FlywayTest} needed.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 *
	 * @throws Exception if any error occurred
	 */
	public void prepareTestInstance(final TestContext testContext)
			throws Exception {
	}

	/**
	 * Called from spring before a test method will be invoked.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 *
	 * @throws Exception if any error occurred
	 */
	public void beforeTestMethod(final TestContext testContext)
			throws Exception {
		final Method testMethod = testContext.getTestMethod();

		final Annotation annotation = testMethod
				.getAnnotation(FlywayTest.class);

		dbResetWithAnotation(testContext, (FlywayTest) annotation);
	}

	/**
	 * no implementation for annotation {@link FlywayTest} needed.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 *
	 * @throws Exception if any error occurred
	 */
	public void afterTestMethod(final TestContext testContext) throws Exception {
	}

	/**
	 * no implementation for annotation {@link FlywayTest} needed.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 *
	 * @throws Exception if any error occurred
	 */
	public void afterTestClass(final TestContext testContext) throws Exception {
	}

	/**
	 * Test the annotation an reset the database.
	 *
	 * @param testContext
	 *            default test context filled from spring
	 * @param annotation
	 *            founded
	 */
	private void dbResetWithAnotation(final TestContext testContext,
			final FlywayTest annotation) {
		if (annotation != null) {
			Flyway flyWay = null;

			final ApplicationContext appContext = testContext
					.getApplicationContext();

			final String executionInfo = ExecutionListenerHelper
					.getExecutionInformation(testContext);

			if (appContext != null) {
				flyWay = getBean(appContext, Flyway.class);

				if (flyWay != null) {
					// we have a fly way configuration no lets try
					if (logger.isInfoEnabled()) {
						logger.info("---> Start reset database for  '"
								+ executionInfo + "'.");
					}
					if (annotation.invokeCleanDB()) {
						if (logger.isDebugEnabled()) {
							logger.debug("******** Clean database for  '"
									+ executionInfo + "'.");
						}
						flyWay.clean();
					}
					if (annotation.invokeInitDB()) {
						if (logger.isDebugEnabled()) {
							logger.debug("******** Init database  for  '"
									+ executionInfo + "'.");
						}
						flyWay.init();
					}
					if (annotation.invokeMigrateDB()) {
						String[] baseDirs = annotation.baseDirsForMigrate();
						if (baseDirs == null || baseDirs.length == 0) {
							if (logger.isDebugEnabled()) {
								logger.debug("******** Default migrate database for  '"
										+ executionInfo + "'.");
							}
							flyWay.migrate();
						} else {
							// Store setting of the old base directory from
							// outside
							// configuration
							// and reset it afterwards so default annotation
							// will still work
							String oldBaseDir = flyWay.getBaseDir();
							try {

								for (int i = 0; i < baseDirs.length; i++) {
									String baseDir = baseDirs[i];

									if (logger.isDebugEnabled()) {
										logger.debug("******** Start migration from base directory '"
												+ baseDir
												+ "'  for  '"
												+ executionInfo + "'.");

									}

									flyWay.setBaseDir(baseDir);
									flyWay.migrate();
								}
							} finally {
								// now reset the old base dir value
								flyWay.setBaseDir(oldBaseDir);
							}

						}
					}
					if (logger.isInfoEnabled()) {
						logger.info("<--- Finished reset database  for  '"
								+ executionInfo + "'.");
					}

					return;
				}
				// in this case we have not the possibility to reset the
				// database

				throw new IllegalArgumentException("Annotation "
						+ annotation.getClass()
						+ " was set, but no Flyway configuration was given.");
			}
			// in this case we have not the possibility to reset the database

			throw new IllegalArgumentException("Annotation "
					+ annotation.getClass()
					+ " was set, but no configuration was given.");
		}
	}

	/**
	 * Wrapper to get a method
	 * <code>ApplicationContext.getBean(Class _class)</code> like in spring 3.0.
	 * It will returns allways the first instance of the founded class.
	 *
	 * @param context
	 *            from which the bean should be retrieved
	 * @param classType
	 *            class type that should be retireved from the configuration file.
	 *
	 * @return a object of the type or <code>null</code>
	 */
	private Flyway getBean(final ApplicationContext context,
			final Class<?> classType) {
		Flyway result = null;

		String[] names = context.getBeanNamesForType(classType);

		if (names != null && names.length > 0) {
			// we always return the bean with the first name

			result = (Flyway) context.getBean(names[0]);
		}

		return result;
	}
}
