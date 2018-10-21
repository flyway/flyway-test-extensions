/**
 * Copyright (C) 2011-2018 the original author or authors.
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

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;

import org.flywaydb.test.ExecutionListenerHelper;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * A {@link TestExecutionListeners} to get the annotation {@link DBUnitSupport}
 * up and running.</p>
 *
 * <b>Attention: at the moment this implementation is only tested with H2 and
 * Oracle!</B></p>
 *
 * The annotation {@link DBUnitSupport}
 * <ul>
 * <li>{@link DBUnitSupport#loadFilesForRun()} use this with the DBUinit
 * database operation and the load file (classpath ressource)</li>
 * <li> {@link DBUnitSupport#saveFileAfterRun()} to store the result after run.
 * if {@link DBUnitSupport#saveTableAfterRun()} is not omitted a whole database
 * table export will be done.</br> Otherwise only the given tables will be
 * exported.</li>
 * </ul>
 * Important if the annotation
 * {@link org.flywaydb.test.annotation.FlywayTest} are used the system
 * properties for <code>jdbc.url</code>, <code>jdbc.driver</code>,
 * <code>jdbc.username</code>, and <code>jdbc.password</code> should be set.</p>
 * Also the test application context should contains code like this:
 *
 * <pre>
 * {@code
 * 	<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
 * 		<property name="location"><value>jdbc.properties</value></property>
 * 		<property name="ignoreResourceNotFound" value="true"/>
 * 	</bean>
 *
 *     <!-- H2 Setup
 *     -Djdbc.driver=org.h2.Driver
 * 	-Djdbc.url=jdbc:h2:./db/testCaseDb
 * 	-Djdbc.username=OC_MORE_TEST
 * 	-Djdbc.password=OC_MORE_TEST
 *
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
 * </br> If this setup is used exist the possibility to run test again different
 * database such as H2 or Oracle.
 * <p/>
 * Usage inside the TestClass
 *
 * <pre>
 * &#064;RunWith(SpringJUnit4ClassRunner.class)
 * &#064;ContextConfiguration(locations = { &quot;/context/simple_applicationContext.xml&quot; })
 * &#064;TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
 * 		DBUnitTestExecutionListener.class })
 * public class SimpleLoadTest {
 *
 * 	&#064;Test
 * 	&#064;DBUnitSupport(saveFileAfterRun = &quot;./dbunit/result/oneMandatorCreate.xml&quot;, saveTableAfterRun = {
 * 		&quot;MANDATOR&quot;, &quot;&quot; })
 * 	public void oneMandatorCreate() throws SQLException
 * 	{
 * 		...
 * 	}
 *
 * 	&#064;Test
 * 	&#064;FlywayTest
 * 	&#064;DBUnitSupport(loadFilesForRun={&quot;INSERT&quot;,&quot;dbunit/insert/oneMandatorInsert.xml&quot;})
 * 	public void oneMandatorInsert() throws SQLException
 * 	{
 * 		....
 * 	}
 * }
 * </pre>
 *
 * If the {@link DefaultDatabaseConnectionFactory} do not fit for usage with DBunit it can be customized.<br/>
 * Define a bean in the application context that implement the interface {@link DatabaseConnectionFactory}.
 * Afterwards the own implementation of the database connection factory will be used for generating database connection.
 * <p/>
 *
 * @author Florian Eska
 *
 * @date 2011-12-10
 * @version 1.7
 *
 */
public class DBUnitTestExecutionListener
	extends AbstractTestExecutionListener
		implements TestExecutionListener {
	// @@ Construction
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired(required = false)
	protected DatabaseConnectionFactory dbConnectionFactory = new DefaultDatabaseConnectionFactory();

	/** default order 4500 */
	private int order = 4500;

	/**
	 * Allocates new <code>AbstractDbSpringContextTests</code> instance.
	 */
	public DBUnitTestExecutionListener() {
	}

	/**
	 * Only annotation with loadFiles will be executed
	 */
	public void beforeTestClass(final TestContext testContext) throws Exception {
		// no we check for the DBResetForClass
		final Class<?> testClass = testContext.getTestClass();
		// For convinience one may annotate tests superclass.
		final Annotation annotation = AnnotationUtils.findAnnotation(testClass,DBUnitSupport.class);
		if (annotation != null) {
			final DBUnitSupport dbUnitAnnotaton = (DBUnitSupport) annotation;

			loadFiles(testContext, dbUnitAnnotaton);
		}
	}

	public void prepareTestInstance(final TestContext testContext)
			throws Exception {
	}

	/**
	 * Only annotation with loadFiles will be executed
	 */
	public void beforeTestMethod(final TestContext testContext)
			throws Exception {
		// no we check for the DBResetForClass
		final Method testMethod = testContext.getTestMethod();

		final Annotation annotation = testMethod
				.getAnnotation(DBUnitSupport.class);
		if (annotation != null) {
			final DBUnitSupport dbUnitAnnotaton = (DBUnitSupport) annotation;

			loadFiles(testContext, dbUnitAnnotaton);
		}
	}

	/**
	 * Only annotation with saveIt will be executed
	 */
	public void afterTestMethod(final TestContext testContext) throws Exception {
		// no we check for the DBResetForClass
		final Method testMethod = testContext.getTestMethod();

		final Annotation annotation = testMethod
				.getAnnotation(DBUnitSupport.class);

		if (annotation != null) {
			final DBUnitSupport dbUnitAnnotaton = (DBUnitSupport) annotation;
			final String saveIt = dbUnitAnnotaton.saveFileAfterRun();
			final String[] tables = dbUnitAnnotaton.saveTableAfterRun();

			if (saveIt != null && saveIt.trim().length() > 0) {
				String executionInfo = "";
				if (logger.isDebugEnabled()) {
					executionInfo = ExecutionListenerHelper
							.getExecutionInformation(testContext);
					logger.debug("******** Start save information '"
							+ executionInfo + "' info file '" + saveIt + "'.");
				}
				final DataSource ds = getSaveDataSource(testContext);

				final IDatabaseConnection con = getConnection(ds, testContext);
                // Issue 16 fix leaking database connection - will look better with Java 7 Closable
                try {
                    final IDataSet dataSet = getDataSetToExport(tables, con);
                    final File fileToExport = getFileToExport(saveIt);

                    final FileWriter fileWriter = new FileWriter(fileToExport);
                    FlatXmlDataSet.write(dataSet, fileWriter);
                } finally {
                    if (logger.isDebugEnabled()) {
                        logger.debug("******** Close database connection " + con);
                    }
                    con.close();
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("******** Finished save information '"
                            + executionInfo + "' info file '" + saveIt + "'.");
                }
            }
		}
	}

	public void afterTestClass(final TestContext testContext) throws Exception {
	}

	/**
	 * Implementation of loadFiles
	 *
	 * @param testContext
	 * @param dbUnitAnnotation
	 * @throws Exception
	 */
	private void loadFiles(final TestContext testContext,
			final DBUnitSupport dbUnitAnnotation) throws Exception {
		final String[] loadFiles = dbUnitAnnotation.loadFilesForRun();

		if (loadFiles != null && loadFiles.length > 0) {
			// we have some files to load
			String executionInfo = "";

			if (logger.isDebugEnabled()) {
				executionInfo = ExecutionListenerHelper
						.getExecutionInformation(testContext);
				logger.debug("******** Load files  '" + executionInfo + "'.");
			}

			for (int i = 0; i < loadFiles.length; i += 2) {
				final String operationName = loadFiles[i];
				final String fileResource = loadFiles[i + 1];

				if (logger.isDebugEnabled()) {
					logger.debug("******** load file '" + executionInfo
							+ "' op='" + operationName + "' - '" + fileResource
							+ "'.");
				}

				final DatabaseOperation operation = getOperation(operationName);

				final ClassPathResource resource = new ClassPathResource(
						fileResource);

                final InputStream is = resource.getInputStream();

                try {

                    // now we try to load the data into database
                    final DataSource ds = getSaveDataSource(testContext);

                    final IDatabaseConnection con = getConnection(ds, testContext);
                    // Issue 16 fix leaking database connection - will look better with Java 7 Closable
                    try {
                        final FlatXmlDataSet dataSet = getFileDataSet(is);
                        operation.execute(con, dataSet);
                    } finally {
                        if (logger.isDebugEnabled()) {
                            logger.debug("******** Close database connection " + con);
                        }
                        con.close();
                    }
                } finally {
                    if ( is != null ) {
                        // avoid memory leak in streams
                        is.close();
                    }
                }
            }
			if (logger.isDebugEnabled()) {
				logger.debug("******** Finished load files '" + executionInfo
						+ "'.");

			}
		}
	}

	private DatabaseOperation getOperation(final String operation)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		final String upOper = operation.toUpperCase();
		final Field field = DatabaseOperation.class.getField(upOper);
		if (field == null || !field.getType().equals(DatabaseOperation.class)) {
			throw new IllegalArgumentException("Operation " + operation
					+ " is unknown");
		}

		final DatabaseOperation result = (DatabaseOperation) field
				.get(DatabaseOperation.class);

		return result;
	}

	/**
	 * Get the a file to export. If the directory does not exist-> create it.
	 *
	 * @param fileName
	 *            for the export file
	 * @return a file
	 */
	private File getFileToExport(final String fileName) {
		String fileToExportName = fileName;
		if (fileName.startsWith(".")) {
			final File curDir = new File(".");

			fileToExportName = curDir.getAbsolutePath() + File.separator
					+ fileName;
		}
		final File fileToExport = new File(fileToExportName);
		fileToExport.getParentFile().mkdirs();
		return fileToExport;
	}

	/**
	 * Return a data set for export. A sort filter is used for the foreign key.
	 *
	 * @param tables
	 *            which table should be exported.
	 * @param con
	 *            is used for the export
	 *
	 * @return the export data set
	 *
	 * @throws DataSetException
	 * @throws SQLException
	 * @throws AmbiguousTableNameException
	 */
	private IDataSet getDataSetToExport(final String[] tables,
			final IDatabaseConnection con) throws DataSetException,
			SQLException, AmbiguousTableNameException {
		final ITableFilter filter = new DatabaseSequenceFilter(con);
		IDataSet dataSetToExport = null;
		if (tables == null || tables.length == 0) {
			// want a complete database export
			dataSetToExport = con.createDataSet();
			dataSetToExport = new FilteredDataSet(filter, dataSetToExport);
		} else {
			final QueryDataSet qDataSet = new QueryDataSet(con);
			// generate the data set
			if (tables.length % 2 != 0) {
				throw new IllegalArgumentException(
						"Contract {<Table Name>,<SELECT_QUERY>} is brocken.");
			}
			for (int i = 0; i < tables.length; i += 2) {
				final String table = tables[i].toUpperCase();
				String query = tables[i + 1];
				if (query == null || query.trim().length() == 0) {
					query = "SELECT * FROM " + table;
				}

				qDataSet.addTable(table, query);
			}

			dataSetToExport = qDataSet;
		}
		return dataSetToExport;
	}

	/**
	 *
	 * @param testContext
	 * @return the data source to use
	 */
	private DataSource getSaveDataSource(final TestContext testContext) {
		final ApplicationContext appContext = testContext
				.getApplicationContext();

		if (appContext != null) {
			final DataSource dataSource = getBean(appContext, DataSource.class);
			if (dataSource != null) {
				return dataSource;
			}
			throw new IllegalArgumentException(
					"The test application context has no configured data source!");
		}

		throw new IllegalArgumentException(
				"The test configuration contains no application context.");
	}

	/**
	 * Get the dbunit specific database connection
	 *
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	protected IDatabaseConnection getConnection(final DataSource dataSource,final TestContext context)
			throws Exception {
		// get connection
		final Connection con = dataSource.getConnection();
		final DatabaseMetaData databaseMetaData = con.getMetaData();
		IDatabaseConnection connection = null;

		try {

			DatabaseConnectionFactory factory = context.getApplicationContext().getBean(DatabaseConnectionFactory.class);

			if ( factory != null )
			{
				dbConnectionFactory = factory;
			}
		}
		catch ( Exception e)
		{
			logger.debug(String.format("We ignore if we could not find a instance of '%s'",DatabaseConnectionFactory.class.getName()));
		}

		if ( dbConnectionFactory != null ) {
			connection = dbConnectionFactory.createConnection(con, databaseMetaData);
			return connection;
		}
//		else {
//			//nnneee
//			DatabaseConnectionFactory factory = context.getApplicationContext().getBean(DatabaseConnectionFactory.class);
//			dbConnectionFactory = factory;
//			connection = dbConnectionFactory.createConnection(con, databaseMetaData);
//			return connection;
//		}
		return null;
	}

	private FlatXmlDataSet getFileDataSet(final InputStream is)
			throws Exception {

		final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		return builder.build(is);
	}

	/**
	 * Wrapper to get a methode
	 * <code>ApplicationContext.getBean(Class _class)</code> like in spring 3.0.
	 *
	 * @param context
	 *            from which the bean should be retrieved
	 * @param classType
	 *            class type
	 *
	 * @return a object of the type or <code>null</code>
	 */
	private DataSource getBean(final ApplicationContext context,
			final Class<?> classType) {
		DataSource result = null;

		String[] names = context.getBeanNamesForType(classType);

		if (names != null && names.length > 0) {
			// we always return the bean with the first name

			result = (DataSource) context.getBean(names[0]);
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
