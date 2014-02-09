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
package com.googlecode.flyway.test.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Support for executing flyway commands during test without any special flyway command in test code.</p>
 *
 * The default behavior is to execute the flyway commands:
 * <ul>
 * <li>{@link com.googlecode.flyway.core.Flyway#clean()}</li>
 * <li>{@link com.googlecode.flyway.core.Flyway#init()}</li>
 * <li>{@link com.googlecode.flyway.core.Flyway#migrate()}</li>
 * </ul>
 *
 * For usage together with JUnit see {@link com.googlecode.flyway.test.junit.FlywayTestExecutionListener}.</p>
 *
 * @author Florian
 * @version  2011-12-10
 * @version 1.7
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FlywayTest {

	/**
	 * invoke flyway command clean before a init/migrate call.</p>
	 *
	 *  Default: true
	 */
	public boolean invokeCleanDB() default true;

	/**
	 * invoke flyway command init before a migrate call</p>
	 *
	 *  Default: true
	 */
	public boolean invokeInitDB() default true;

	/**
	 * invoke flyway command migrate </p>
	 *
	 *  Default: true
	 */
	public boolean invokeMigrateDB() default true;

	/**
	 * Support to change the default setting for the base directory setting. </p>
	 *
	 * For each entry in the list a separate flyway migrate call will be executed.</br>
	 * Afterwards the default base directory will be set.</p>
	 *
	 * Default: empty list - default settings will be used. <p/>
	 *
	 * Attention: This annotation are deprecated and can not used together  with {@link #locationsForMigrate}.
	 *
	 * @deprecated
	 */
	public String[] baseDirsForMigrate() default {};

	/**
	 * Support to add locations to the default location settings. <p/>
	 *
	 * if {link #overrideLocations()} returns true the complete locations will
	 * be changed.<p/>
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
}
