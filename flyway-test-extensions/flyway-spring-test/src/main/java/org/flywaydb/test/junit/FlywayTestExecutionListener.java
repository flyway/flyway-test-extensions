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
package org.flywaydb.test.junit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.ExecutionListenerHelper;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.annotation.FlywayTests;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

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
 * @deprecated use {@link org.flywaydb.test.FlywayTestExecutionListener} instead.
 *
 */
public class FlywayTestExecutionListener
        extends org.flywaydb.test.FlywayTestExecutionListener {


    /**
     * Allocates new <code>AbstractDbSpringContextTests</code> instance.
     *
     * @deprecated 
     */
    public FlywayTestExecutionListener() {
    }
}
