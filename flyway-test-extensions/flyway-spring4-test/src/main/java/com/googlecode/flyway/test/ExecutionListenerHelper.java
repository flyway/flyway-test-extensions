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
package com.googlecode.flyway.test;

import java.lang.reflect.Method;

import org.springframework.test.context.TestContext;

/**
 * Simple helper for the TestExecutionListener
 * 
 * @author Florian
 * @version 2011-12-28
 * @version 1.0
 *
 */
public abstract class ExecutionListenerHelper {
	/**
	 * Helper method to build test execution information with test class and
	 * method
	 * 
	 * @param testContext
	 * 
	 * @return String like &lt;Class Name&gt;[.&lt;Method Name&gt;]
	 */
	public static String getExecutionInformation(TestContext testContext) {
		String result = "";
		Class<?> testClass = testContext.getTestClass();

		result = testClass.getName();

		// now check for method
		Method m = testContext.getTestMethod();
		if (m != null) {
			result = result + "." + m.getName();
		}

		return result;
	}

}
