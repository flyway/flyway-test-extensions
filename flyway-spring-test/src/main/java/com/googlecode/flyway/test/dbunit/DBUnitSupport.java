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
package com.googlecode.flyway.test.dbunit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Add additional DBUnit support for loading data during test.</p>
 *  
 * 
 * @author florian
 * 
 * @date 2011-12-10
 * @version 1.0
 *
 */
 @Retention(RetentionPolicy.RUNTIME)
public @interface DBUnitSupport {

	 /** were to store the database after test run */
     public String saveFileAfterRun() default "";

     /** which tables should be exported ,
      * we use a pattern of TABLE_NAME , SELECT Querry ,
      * if select query is empty a select * from table will be done*/
     public String[] saveTableAfterRun() default {};

     /** the list of files that should be loaded for a test run.
      * This files where searched in the classpath.
      * It will use a pattern like 'DBUnit Command', 'xmlfile' */
     public String[] loadFilesForRun() default {};

}
