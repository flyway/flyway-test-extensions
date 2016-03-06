/**
 * Copyright (C) 2011-2016 the original author or authors.
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
 * Spring extension and implementation of annotation {@link org.flywaydb.test.annotation.FlywayTest}.</p>
 * 
 * Implementation will be found inside {@link org.flywaydb.test.junit.FlywayTestExecutionListener}.
 *
 * A factory helper for {@link org.flywaydb.core.Flyway} provide {@link FlywayHelperFactory}. This are needed
 * to configure Flyway to use the flyway.properties file correct.
 */
package org.flywaydb.test.junit;