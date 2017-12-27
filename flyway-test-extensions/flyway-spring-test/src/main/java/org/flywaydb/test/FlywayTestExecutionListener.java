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
package org.flywaydb.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.annotation.FlywayTests;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spring test execution listener to get annotation {@link FlywayTest} up and
 * running
 *
 * <p>
 * If the annotation {@link FlywayTest} used on class level a clean , init ,
 * migrate cycle is done during class load.<br>
 * If the annotation {@link FlywayTest} used on test method level a clean , init
 * , migrate cycle is done before test execution.
 * </p>
 *
 * <p>
 * Important if the annotation {@link FlywayTest} are used the system properties
 * for <code>jdbc.url</code>, <code>jdbc.driver</code>,
 * <code>jdbc.username</code>, and <code>jdbc.password</code> should be set.
 * </p>
 * Also the test application context should contains code like this:
 *
 * <pre>
 * {@code
 * <bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
 *   <property name="location" value="dbc.properties"/>
 *   <property name="ignoreResourceNotFound" value="true"/>
 * </bean>
 *
 * <bean id="flyway" class="org.flywaydb.core.Flyway" depends-on="data.source.id">
 *   <property name="dataSource" ref="data.source.id"/>
 *   <property name="locations" value="oracle"/>
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
 * <li>If you using spring framework version lower than 3.x the annotation
 * {@link FlywayTest} wont work at class level.</li>
 * <li>For spring framework version 2.5.6 use
 * simple_applicationContext_spring256.xml as application context example</li>
 * <li>If you using the annotation {@link FlywayTest} more than one time in test
 * classes than <b>do not</b> use parallel execution in surefire plugin.</bR>
 * With this option you will setup your database in more than one thread
 * parallel!</li>
 * </ul>
 *
 * </p>
 *
 * @author Florian
 * @author Eddú Meléndez
 *
 * @version 2011-12-10
 * @version 1.7
 *
 */
public class FlywayTestExecutionListener
        extends AbstractTestExecutionListener
        implements TestExecutionListener {

    /**
     * Used for logging inside test executions.
     */
    // @@ Construction
    private final Log logger = LogFactory.getLog(getClass());

    /** default order 4000 */
    private int order = 4000;

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
     * @throws Exception
     *             if any error occurred
     */
    public void beforeTestClass(final TestContext testContext) throws Exception {
        // no we check for the DBResetForClass
        final Class<?> testClass = testContext.getTestClass();

        handleFlywayTestAnnotationForClass(testContext, testClass);

        // now detect if current class has a beforeClass or BeforeAllAnnotation
        Class beforeClassClass = getClassOrNullForName( "org.junit.BeforeClass");
        Class beforeAllClass = getClassOrNullForName("org.junit.jupiter.api.BeforeAll");
        Class beforeClassTestNgClass = getClassOrNullForName( "org.testng.annotations.BeforeClass");

        // contains first finding of FlywayTest annotation together with a Before annotation
        handleFlywayTestWithTestAnnotation(testContext, testClass, beforeClassClass, beforeAllClass, beforeClassTestNgClass);
    }

    private void handleFlywayTestAnnotationForClass(TestContext testContext, Class<?> testClass) {
        FlywayTests containerAnnotation = AnnotationUtils.getAnnotation(testClass, FlywayTests.class);
        if (containerAnnotation != null) {
            FlywayTest[] annotations = containerAnnotation.value();
            for (FlywayTest annotation : annotations) {
                dbResetWithAnnotation(testContext, annotation);
            }
        } else {
            FlywayTest annotation = AnnotationUtils.getAnnotation(testClass, FlywayTest.class);
            if (annotation != null) {
                dbResetWithAnnotation(testContext, annotation);
            }
        }
    }

    /**
     * implementation for annotation {@link FlywayTest} for handling with {@link org.testng.annotations.BeforeMethod}  annotation.
     *
     * @param testContext
     *            default test context filled from spring
     *
     * @throws Exception
     *             if any error occurred
     */
    public void beforeTestExecution(final TestContext testContext)
            throws Exception {
        Class testClass = testContext.getTestClass();

        Class beforeMethodTestNgClass = getClassOrNullForName( "org.testng.annotations.BeforeMethod");

        // contains first finding of FlywayTest annotation together with a Before annotation
        handleFlywayTestWithTestAnnotation(testContext, testClass, null, null, beforeMethodTestNgClass);
    }

    /**
     * implementation for annotation {@link FlywayTest} for handling with {@link org.junit.Before} or
     * {@link org.junit.jupiter.api.BeforeEach}  annotation.
     *
     * @param testContext
     *            default test context filled from spring
     *
     * @throws Exception
     *             if any error occurred
     */
    public void prepareTestInstance (final TestContext testContext)
            throws Exception {
        Class testClass = testContext.getTestClass();

        Class beforeMethodClass = getClassOrNullForName( "org.junit.Before");
        Class beforeEachMethodClass = getClassOrNullForName("org.junit.jupiter.api.BeforeEach");

        // contains first finding of FlywayTest annotation together with a Before annotation
        handleFlywayTestWithTestAnnotation(testContext, testClass, beforeMethodClass, beforeEachMethodClass, null);
    }

    /**
     * Search the class hierachie if a {@link FlywayTest} or {@link FlywayTests} annotation is used
     * together with a one of the Test annotations.
     *
     * @param testContext  current test spring context
     * @param testClass  current test class
     * @param junit4TestAnnotationClass  junit4 test annotation class
     * @param junit5TestAnnotationClass junit5 test annotation class
     */
    private void handleFlywayTestWithTestAnnotation(TestContext testContext, Class testClass, Class junit4TestAnnotationClass, Class junit5TestAnnotationClass, Class testNgAnnotationClass) {
        Class currentTestClass = testClass;

        // search the first class with Before and FlywayTest annotation
        while (currentTestClass != Object.class) {
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(currentTestClass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (isMethodAnnotatedWithAtLeastOne(method, junit4TestAnnotationClass, junit5TestAnnotationClass, testNgAnnotationClass)
                        && isMethodAnnotatedWithAtLeastOne(method, FlywayTest.class, FlywayTests.class, null)) {
                    // we have a method here that have both annotations
                    getLogger().debug("Method " + method.getName() + " using flyway annotation.");
                    if (handleFlywayTestAnnotationForMethod(testContext, method)) {
                        // finished handling
                        return;
                    }
                }
            }

            // move to the upper class in the hierarchy in search for more methods
            currentTestClass = currentTestClass.getSuperclass();
        }
    }

    private boolean isMethodAnnotatedWithAtLeastOne(Method method, Class firtsAnnotationToCheck, Class secondAnnotationToCheck,
                                                    Class testNgAnnotationToCheck) {
        return (firtsAnnotationToCheck != null && method.isAnnotationPresent(firtsAnnotationToCheck))
                || (secondAnnotationToCheck != null && method.isAnnotationPresent(secondAnnotationToCheck)
                || (testNgAnnotationToCheck != null && method.isAnnotationPresent(testNgAnnotationToCheck)) );
    }

    private Class getClassOrNullForName(String classname) {
        Class before = null;

        try {
            before = getClass().getClassLoader().loadClass(classname);
        } catch (ClassNotFoundException ignored) {
            getLogger().debug(String.format("No class %s is present.", classname));
        }

        return before;
    }


    /**
     * Called from spring before a test method will be invoked.
     *
     * @param testContext
     *            default test context filled from spring
     *
     * @throws Exception
     *             if any error occurred
     */
    public void beforeTestMethod(final TestContext testContext)
            throws Exception {
        final Method testMethod = testContext.getTestMethod();

        handleFlywayTestAnnotationForMethod(testContext, testMethod);
    }

    /**
     * Test if the annoration {@link FlywayTest} or {@link FlywayTests} is present and innvoke the annotation support.
     *
     * @param testContext current spring context
     * @param method method to test.
     *
     * @return true if a annotation was present.
     */
    private boolean handleFlywayTestAnnotationForMethod(TestContext testContext, Method method) {
        boolean result = false;

        FlywayTests containerAnnotation = AnnotationUtils.getAnnotation(method, FlywayTests.class);
        if (containerAnnotation != null) {
            FlywayTest[] annotations = containerAnnotation.value();
            for (FlywayTest annotation : annotations) {
                dbResetWithAnnotation(testContext, annotation);
                result = true;
            }
        } else {
            FlywayTest annotation = AnnotationUtils.getAnnotation(method, FlywayTest.class);
            if (annotation != null) {
                dbResetWithAnnotation(testContext, annotation);
                result = true;
            }
        }

        return result;
    }

    /**
     * no implementation for annotation {@link FlywayTest} needed.
     *
     * @param testContext
     *            default test context filled from spring
     *
     * @throws Exception
     *             if any error occurred
     */
    public void afterTestMethod(final TestContext testContext) throws Exception {
    }

    /**
     * no implementation for annotation {@link FlywayTest} needed.
     *
     * @param testContext
     *            default test context filled from spring
     *
     * @throws Exception
     *             if any error occurred
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
    private void dbResetWithAnnotation(final TestContext testContext,
                                       final FlywayTest annotation) {
        if (annotation != null) {
            Flyway flyWay = null;

            final ApplicationContext appContext = testContext
                    .getApplicationContext();

            if (appContext != null) {
                flyWay = getBean(appContext, Flyway.class, annotation.flywayName());

                if (flyWay != null) {
                    String executionInfo = "";

                    // we have a fly way configuration no lets try
                    if (logger.isInfoEnabled()) {
                        executionInfo = ExecutionListenerHelper
                                .getExecutionInformation(testContext);
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
                    if (annotation.invokeBaselineDB()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("******** Baseline database  for  '"
                                    + executionInfo + "'.");
                        }
                        flyWay.baseline();
                    }
                    if (annotation.invokeMigrateDB()) {
                        String[] locations = annotation.locationsForMigrate();

                        if ((locations == null || locations.length == 0)) {

                            if (logger.isDebugEnabled()) {
                                logger.debug("******** Default migrate database for  '"
                                        + executionInfo + "'.");
                            }

                            flyWay.migrate();
                        } else {
                            locationsMigrationHandling(annotation, flyWay,
                                    executionInfo);
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
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("dbResetWithAnnotation is called without a flyway test annotation.");
            }
        }
    }

    /**
     * Handling of the change of locations configuration of a flyway.
     *
     * @param annotation
     *            current annotation
     * @param flyWay
     *            bean
     * @param executionInfo
     *            current test context.
     */
    private void locationsMigrationHandling(final FlywayTest annotation,
                                            final Flyway flyWay, final String executionInfo) {
        final String[] locations = annotation.locationsForMigrate();

        // now migration handling for locations support
        String[] oldLocations = flyWay.getLocations();
        boolean override = annotation.overrideLocations();
        try {
            String[] useLocations = null;
            if (override) {
                useLocations = locations;
            } else {
                // Fill the locations
                useLocations = Arrays.copyOf(oldLocations, oldLocations.length
                        + locations.length);

                System.arraycopy(locations, 0, useLocations, oldLocations.length, locations.length);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(String
                        .format("******** Start migration from locations directories '%s'  for  '%s'.",
                                Arrays.asList(useLocations), executionInfo));

            }

            flyWay.setLocations(useLocations);

            flyWay.migrate();
        } finally {
            // reset the flyway bean to original configuration.
            flyWay.setLocations(oldLocations);
        }
    }

    /**
     * Wrapper to get a method
     * <code>ApplicationContext.getBean(Class _class)</code> like in spring 3.0.
     * It will returns always the first instance of the founded class.
     *
     * @param context
     *            from which the bean should be retrieved
     * @param classType
     *            class type that should be retrieved from the configuration
     *            file.
     *
     * @return a object of the type or <code>null</code>
     */
    private Flyway getBean(final ApplicationContext context,
                           final Class<?> classType, String idName) {
        Flyway result = null;

        String[] names = context.getBeanNamesForType(classType);

        if (names != null && names.length > 0) {
            if (idName == null || idName.trim().isEmpty()) {
                // old behaviour
                // we always return the bean with the first name

                result = (Flyway) context.getBean(names[0]);
            } else {
                result = (Flyway) context.getBean(idName);
            }
        }

        return result;
    }

    /**
     * change the default order value;
     * @since 3.2.1.1
     *
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     *
     * @return order default 4500
     * @since 3.2.1.1
     *
     */
    public int getOrder() {
        return order;
    }
}
