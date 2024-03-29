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
package org.flywaydb.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper factory for Flyway creation and support for setting the flyway.properties and configure
 * flyway with {@link Flyway#configure(java.util.Properties)}.
 *
 * @author Florian
 *
 * @version 2013-04-01
 * @version 2.1
 */
public class FlywayHelperFactory {

    /**
     * Used for logging inside test executions.
     */
    // @@ Construction
    private final Log logger = LogFactory.getLog(getClass());

    private Flyway flyway;

    private Properties flywayProperties;

    private ClassicConfiguration flywayConfiguration;

    public FlywayHelperFactory() {
        logger.info("Create flyway helper factory.");
    }

    /**
     * Create a new flyway instance and call {@link Flyway#configure(java.util.Properties)}  with
     * content of file <i>flyway.properties</i>.
     *
     * @return the flyway instance
     */
    public synchronized Flyway createFlyway() {

        if (flyway == null) {
            logger.info("Create a new flyway instance.");

            // now use the flyway properties
            Properties configuredProperties = getFlywayProperties();

            if (configuredProperties == null) {
                // try to search flyway.properties in classpath
                configuredProperties = new Properties();
                setFlywayProperties(configuredProperties);

                ClassPathResource classPathResource = new ClassPathResource("flyway.properties");

                InputStream inputStream = null;

                try {
                    inputStream = classPathResource.getInputStream();
                    configuredProperties.load(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Can not load flyway.properties.", e);
                }

                setFlywayProperties(configuredProperties);
                logger.info(String.format("Load flyway.properties with %d entries.", configuredProperties.size()));
            } else {
                logger.info(String.format("Used preconfigured flyway.properties with %d entries.", configuredProperties.size()));
            }

            if ( flywayConfiguration == null) {
                ClassicConfiguration classicConfiguration = new ClassicConfiguration();

                classicConfiguration.configure(getFlywayProperties());
                setFlywayConfiguration(classicConfiguration);

            } else {
                ClassicConfiguration classicConfiguration = getFlywayConfiguration();

                classicConfiguration.configure(getFlywayProperties());
                setFlywayConfiguration(classicConfiguration);
            }

            Flyway toReturn = Flyway.configure()
              .configuration(getFlywayConfiguration())
                    .load();

            setFlyway(toReturn);
        }

        return flyway;
    }

    public Properties getFlywayProperties() {
        return flywayProperties;
    }

    public void setFlywayProperties(Properties flywayProperties) {
        this.flywayProperties = flywayProperties;
    }

    @SuppressWarnings("unused")
    public Flyway getFlyway() {
        return flyway;
    }

    private void setFlyway(Flyway flyway) {
        this.flyway = flyway;
    }

    public ClassicConfiguration getFlywayConfiguration() {
        return flywayConfiguration;
    }

    public void setFlywayConfiguration(ClassicConfiguration flywayConfiguration) {
        this.flywayConfiguration = flywayConfiguration;
    }
}