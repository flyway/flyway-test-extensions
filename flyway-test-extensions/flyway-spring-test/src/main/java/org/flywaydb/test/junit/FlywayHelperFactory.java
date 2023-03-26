/**
 * Copyright (C) 2011-2023 the original author or authors.
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
package org.flywaydb.test.junit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper factory for Flyway creation and support for setting the flyway.properties and configure
 * flyway with {@link Flyway#configure(Properties)}.
 *
 * @author Florian
 *
 * @version 2013-04-01
 * @version 2.1
 *
 * @deprecated use {@link org.flywaydb.test.FlywayHelperFactory} instead
 */
public class FlywayHelperFactory extends org.flywaydb.test.FlywayHelperFactory {

    /**
     * Used for logging inside test executions.
     */
    // @@ Construction
    private final Log logger = LogFactory.getLog(getClass());


    public FlywayHelperFactory() {
        logger.info("Create flyway helper factory.");
    }
}