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
/**
 * Flyway test extension for unit testing.</p>
 *
 * A example project to show the integration of flyway-spring-test extension
 * in a test with spring version 2.5.6.
 *
 * <ul>
 * <li>Maven extension see the pom.xml file</li>
 * <li>Usage inside a test see {@link com.googlecode.flyway.test.sample.spring256.Spring256JUnitTest}
 * <li>Test resources with SQL statements see the directories
 * <b>loadmsql</b> and <b>sampletest256</b></li>
 * </ul>
 *
 * Per default the project will execute all test again H2 database. </br>
 * If you want to execute a test again a oracle database use Profile oracle-test:
 * <code>
 *   mvn clean install -P oracle-test
 * </code>
 *
 *
 * @version 1.0
 * @version 2012-01-04
 *
 */
package com.googlecode.flyway.test.sample.spring256;

