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
package com.googlecode.flyway.test.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Support for executing flyway commands during test without any special flyway command in test code.</p>
 * 
 * The default behavior is to execute the flyway commands:
 * <li>{@link com.googlecode.flyway.core.Flyway#clean()}</li>
 * <li>{@link com.googlecode.flyway.core.Flyway#init()}</li>
 * <li>{@link com.googlecode.flyway.core.Flyway#migrate()}</li>
 * 
 * For usage together with JUnit see {@link com.googlecode.flyway.test.junit.FlywayTestExecutionListener}
 * 
 * @author florian
 * @date  2011-12-10
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FlywayTest {

	/**
	 * invoke flyway command clean before a init/migrate call.</p>
	 *  Default: true
	 */
	public boolean invokeCleanDB() default true;

	/** 
	 * invoke flyway command init before a migrate call</p>
	 *  Default: true
	 */
	public boolean invokeInitDB() default true;

	/** 
	 * invoke flyway command migrate </p>
	 *  Default: true
	 */
	public boolean invokeMigrateDB() default true;

	/**
	 * Support to change the default setting for the base directory setting. </p>
	 * for each entry in the list a separate flayway migrate call will be executed.</p>
	 *  Default: empty list
	 */
	public String[] baseDirsForMigrate() default {};

}
