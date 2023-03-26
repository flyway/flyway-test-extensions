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
package org.flywaydb.sample.test.spring.boot2.flywaytest;

import org.flywaydb.sample.test.spring.boot2.SampleFlywayApplication;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleFlywayApplication.class)
@Rollback
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@FlywayTest
/**
 * Show a spring-boot2 test configuration with {@link FlywayTest} annotation.
 *
 */
public class FlywayTestApplicationTest {

    @Autowired
    private JdbcTemplate template;

    @FlywayTest(invokeCleanDB = true)
    @Test
    public void singleLocation() throws Exception {
        assertThat(template.queryForObject(
                "SELECT COUNT(*) from PERSON", Integer.class),
                is(3));
    }

    @FlywayTest(locationsForMigrate = {"/db/migration", "/db/migration2"})
    @Test
    public void twoLocations() throws Exception {
        assertThat(template.queryForObject(
                "SELECT COUNT(*) from PERSON", Integer.class),
                is(5));
    }

}
