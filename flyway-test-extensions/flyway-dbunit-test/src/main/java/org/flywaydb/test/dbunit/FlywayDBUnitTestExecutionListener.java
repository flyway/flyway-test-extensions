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
package org.flywaydb.test.dbunit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AbstractTestExecutionListener;


/**
 * A {@link TestExecutionListeners} to get both annotation {@link FlywayTest} and {@link DBUnitSupport} work in the correct order.</p>
 *
 * It make the delegation in the natural order to the both database {@link TestExecutionListener}
 * for db reset {@link FlywayTestExecutionListener} and DBunit
 * {@link DBUnitTestExecutionListener}.
 *
 * If only on of them is needed it can be addressed directly.
 *
 * <pre>
 * {@code
 * 	<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
 * 		<property name="location"><value>jdbc.properties</value></property>
 * 		<property name="ignoreResourceNotFound" value="true"/>
 * 	</bean>
 *
 * 	<!-- flyway part -->
 * 	<bean id="flyway" class="org.flywaydb.core.Flyway" depends-on="dataSourceId">
 * 	    <property name="dataSource" ref="dataSourceId"/>
 * 	    <property name="locations" value="oracle"/>
 * 	</bean>
 *
 *     <!-- H2 Setup
 *     -Djdbc.driver=org.h2.Driver
 * 	-Djdbc.url=jdbc:h2:./db/testCaseDb
 * 	-Djdbc.username=OC_MORE_TEST
 * 	-Djdbc.password=OC_MORE_TEST
 *
 * 	Oracle Setup
 * 	-Djdbc.driver=oracle.jdbc.driver.OracleDriver
 * 	-Djdbc.url=jdbc:oracle:thin:<oracle.host>:<oracle.port>:<oracle.sid>
 * 	-Djdbc.username=OC_MORE_TEST
 * 	-Djdbc.password=OC_MORE_TEST
 *      -->
 * 	<bean id="dataSourceId" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
 * 		<property name="driverClassName"><value>$jdbc.driver</value></property>
 * 		<property name="url"><value>$jdbc.url</value></property>
 * 		<property name="username"><value>$jdbc.username</value></property>
 * 		<property name="password"><value>$jdbc.password</value></property>
 * 	</bean>
 * }
 * </PRE>
 *
 * </br> If this setup is used exist the possibility to run test again different database such as
 * H2 or Oracle.
 * <p/>
 * Usage inside the Testclass
 *
 * <pre>
 *   @RunWith(SpringJUnit4ClassRunner.class)
 *   @ContextConfiguration(locations={"/context/simple_applicationContext.xml"})
 *   @TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
 *   	FlywayDBUnitTestExecutionListener.class})
 *   @DBCleanReset
 * public class SimpleLoadTest
 *
 *    @Autowired
 *    DataSource dataSourceId;
 *    ....
 *    @DBUnitSupport
 *    @Test
 *    public void testMethod() ....
 * </pre>
 *
 * If the {@link DefaultDatabaseConnectionFactory} do not fit for usage with DBunit it can be customized.<br/>
 * Define a bean in the application context that implement the interface {@link DatabaseConnectionFactory}.
 * Afterwards the own implementation of the database connection factory will be used for generating database connection.
 * <p/>
 *
 * @author florian
 *
 * @date 2011-12-10
 * @version 1.0
 *
 */
public class FlywayDBUnitTestExecutionListener
        extends AbstractTestExecutionListener
        implements TestExecutionListener {
    // @@ Construction
    protected final Log logger = LogFactory.getLog(getClass());

    protected final DBUnitTestExecutionListener dbUnit = new DBUnitTestExecutionListener();

    protected final FlywayTestExecutionListener dbReset = new FlywayTestExecutionListener();

    /** default order 4000 */
    private int order = 4000;

    /**
     * Allocates new <code>AbstractDbSpringContextTests</code> instance.
     */
    public FlywayDBUnitTestExecutionListener() {
    }

    public void beforeTestClass(final TestContext testContext) throws Exception {
        dbReset.beforeTestClass(testContext);
        dbUnit.beforeTestClass(testContext);
    }

    public void prepareTestInstance(final TestContext testContext) throws Exception {
        dbReset.prepareTestInstance(testContext);
        dbUnit.prepareTestInstance(testContext);
    }

    public void beforeTestMethod(final TestContext testContext) throws Exception {
        dbReset.beforeTestMethod(testContext);
        dbUnit.beforeTestMethod(testContext);
    }

    public void afterTestMethod(final TestContext testContext) throws Exception {
        dbUnit.afterTestMethod(testContext);
        dbReset.afterTestMethod(testContext);
    }

    public void afterTestClass(final TestContext testContext) throws Exception {
        dbUnit.afterTestClass(testContext);
        dbReset.afterTestClass(testContext);
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
     * @return order default 4000
     * @since 3.2.1.1
     *
     */
    public int getOrder() {
        return order;
    }
}
