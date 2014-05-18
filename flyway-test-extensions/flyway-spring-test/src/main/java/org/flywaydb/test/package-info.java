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
/**
 * Flyway test extension for unit testing.</p>
 *
 * Description how to use Flyway anotation see javadoc
 * of {@link org.flywaydb.test.annotation.FlywayTest}</p>
 *
 * <b>Notes:</b>
 * <ul><li>I suggest to use spring framework version &gt;= 3.x,
 * for better test support and usage of context access. </BR>
 * If you must use spring 2.5.6 see the following hints.</li>
 * <li>If you using spring framework version lower than 3.x the
 * annotation {@link FlywayTest} wont work at class level.</li>
 * <li>For spring framework version 2.5.6 use simple_applicationContext_spring256.xml
 * as application context example</li>
 * <li>If you using the annotation
 * {@link FlywayTest} more than one time in test classes than <b>do not</b> use
 * parallel execution in surefire plugin.</BR>
 * With this option you will setup
 * your database in more than one thread parallel!</li>
 * </ul>
 *
 */
package org.flywaydb.test;

