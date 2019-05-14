/**
 * Copyright (C) 2011-2019 the original author or authors.
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
package org.flywaydb.test.sample.spring5;

import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Simple Test to show how the annotation can be used inside test execution. This test use application
 * context flywayContainerContext.xml to show the usage of the container context.
 * of the simple__applicationContext </p>
 *
 *
 * @author florian
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"/context/flywayContainerContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@FlywayTest
public class SpringDifferentContextJUnitTest extends Spring5JUnitTest {
}
