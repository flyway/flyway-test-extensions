/**
 * Copyright (C) 2011-2018 the original author or authors.
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
package org.flywaydb.test.junit5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flywaydb.test.FlywayTestExecutionListener;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

/**
 * FlywayTest extension for JUnit5.
 */
public class FlywayTestExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback,
        BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        logger.debug("afterAll " + extensionContext);

        getTestExecutionListener(extensionContext).afterTestClass(getTestContextManager(extensionContext).getTestContext());
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        logger.debug("afterEach " + extensionContext);
        getTestExecutionListener(extensionContext).afterTestMethod(getTestContextManager(extensionContext).getTestContext());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        logger.debug("afterTestExecution " + extensionContext);
        getTestExecutionListener(extensionContext).afterTestExecution(getTestContextManager(extensionContext).getTestContext());
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        logger.debug("beforeAll " + extensionContext);
        getTestExecutionListener(extensionContext).beforeTestClass(getTestContextManager(extensionContext).getTestContext());
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        logger.debug("beforeEach " + extensionContext);
        getTestExecutionListener(extensionContext).beforeTestMethod(getTestContextManager(extensionContext).getTestContext());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        logger.debug("beforeTestExecution " + extensionContext);
        getTestExecutionListener(extensionContext).beforeTestExecution(getTestContextManager(extensionContext).getTestContext());
    }

    private FlywayTestExecutionListener getTestExecutionListener(ExtensionContext extensionContext) {

        FlywayTestExecutionListener flywayTestExecutionListener = new FlywayTestExecutionListener();

        return flywayTestExecutionListener;
    }

    // #tag::springCopy[]
    /**
     * {@link ExtensionContext.Namespace} in which {@code TestContextManagers} are stored,
     * keyed by test class.
     */
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpringExtension.class);

    /**
     * Get the {@link TestContextManager} associated with the supplied {@code ExtensionContext}.
     *
     * @return the {@code TestContextManager} (never {@code null})
     */
    private static TestContextManager getTestContextManager(ExtensionContext context) {
        Assert.notNull(context, "ExtensionContext must not be null");
        Class<?> testClass = context.getRequiredTestClass();
        ExtensionContext.Store store = getStore(context);
        return store.getOrComputeIfAbsent(testClass, TestContextManager::new, TestContextManager.class);
    }

    private static ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE);
    }
    // #end::springCopy[]

}
