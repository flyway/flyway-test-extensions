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
package org.flywaydb.test.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Support for executing flyway commands during test without any special flyway command in test code.</p>
 *
 * The default behavior is to execute the flyway commands:
 * <ul>
 * <li>{@link org.flywaydb.core.Flyway#clean()}</li>
 * <li>{@link org.flywaydb.core.Flyway#baseline()}</li>
 * <li>{@link org.flywaydb.core.Flyway#migrate()}</li>
 * </ul>
 *
 * For usage together with JUnit see {@link org.flywaydb.test.junit.FlywayTestExecutionListener}.</p>
 *
 * @author Florian
 * @version 2011-12-10
 * @version 1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FlywayTests.class)
public @interface FlywayTest {

    /**
     * invoke flyway command clean before a init/migrate call.</p>
     * If set to true it will invoke {@link org.flywaydb.core.Flyway#clean()}.
     *
     *  Default: true
     */
    public boolean invokeCleanDB() default true;

    /**
     * invoke flyway command baseline before a migrate call</p>
     *
     * Baseline will create the schema_version table with a initialization entry
     * depending on {@link org.flywaydb.core.Flyway} configuration property
     * {@link org.flywaydb.core.Flyway#baselineVersion}.
     * It will invoke {@link org.flywaydb.core.Flyway#baseline()}.
     *
     *  Default: false
     */
    public boolean invokeBaselineDB() default false;

    /**
     * invoke flyway command migrate </p>
     * If set to true it will invoke {@link org.flywaydb.core.Flyway#migrate()}.
     *
     *  Default: true
     */
    public boolean invokeMigrateDB() default true;

    /**
     * Support to add locations to the default location settings. <p/>
     *
     * if {@link #overrideLocations()} returns true the complete locations will
     * be changed.
     *
     * Default: empty list  <p/>
     */
    public String[] locationsForMigrate() default {};

    /**
     * With this attribute the handling of the locationsForMigrate can be changed. <p/>
     *
     * Default: false
     */
    public boolean overrideLocations() default false;

    /**
     * Reference the flyway bean name.<p/>
     *
     * If no name are specified the first Flyway instance {@link org.flywaydb.core.Flyway} of the
     * application context are used.<p/>
     *
     * If the name of the {@link org.flywaydb.core.Flyway} instance are not part of the application
     * context, the test will fail with {@link org.springframework.beans.factory.NoSuchBeanDefinitionException}.<p/>
     *
     * Default: empty string<p/>
     */
    public String flywayName() default "";
}
