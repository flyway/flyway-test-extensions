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
package org.flywaydb.test.junit5.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation FlywayTestExtension is a shortcut for JUnit5 usage.
 *
 * The annotation only work together with <a href="https://spring.io/"}>Spring Framework</a> and need a
 * implementation of {@link SpringExtension}.
 *
 * It support all features that are described with {@link org.flywaydb.test.annotation.FlywayTest} and
 * {@link org.flywaydb.test.annotation.FlywayTests}.
 *
 * Usage:
 * <pre>
 * {@code
 * @FlywayTestExtension.class
 * class TestClass {
 *
 * }
 * }
 * </pre>
 * and will replace
 * <pre>
 * {@code
 * @ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
 * @ExtendWith(org.flywaydb.test.junit5.FlywayTestExtension.class)
 * class TestClass {
 *
 * }
 * }
 * </pre>
 *
 *
 * @author florian
 * @since 5.2.2
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(SpringExtension.class)
@ExtendWith(org.flywaydb.test.junit5.FlywayTestExtension.class)
public @interface FlywayTestExtension {
}
