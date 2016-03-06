flyway-test-extensions
======================

![flyway-test-extension logo](https://github.com/flyway/flyway-test-extensions/blob/master/image/logo-flyway-test-extensions.png) 


Test extensions for the Flyway project
--------------------------------------

For feature of Flyway see [Flyway Db Org Page](http://flywaydb.org/) 

Version 4.0 Released 
------------------------

<b>2016-03-06</b> flyway-test-extensions version <b>4.0</b> released.

Version number 4.0.x are used to show the dependency to Flyway version 4.0.

See also [Release Notes](https://github.com/flyway/flyway-test-extensions/wiki/Release-Notes) 

Central maven repository under http://search.maven.org/#search|ga|1|flyway-test-extensions contains all projects jar.

Project
-------
This extension give the possibility to reset and/or fill the database with defined content.<br>With this precondition each test had reproducible database start point. 

* Annotation support FlywayTest for database unit testing. Use [Flyway](https://github.com/flyway/)  feature.
  * <b>clean</b> - execution of flyway task clean
  * <b>init</b> - execution of flyway task init
  * <b>migrate</b> - execution of flyway task migrate
* Annotation can be used at
  * each test class (once per test case)
  * each test method  
* Samples projects to use annotation inside a unit testing environment
  * Spring 4.x sample (see [UsageFlywaySpringTest4](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) )
  * Spring 2.5.6 (see [UsageFlywaySpringTest256](http://code.google.com/p/flyway-test-extensions/wiki/UsageFlywaySpringTest256) )
* Additional project supports a DBUnit annotation use together with FlywayTest [DBUnitSupport](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-dbunit-test/src/main/java/org/flywaydb/test/dbunit/DBUnitSupport.java). A usage example you will find at [UsageFlywayDBUnitTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-dbunit-test).

How to use it
-------------
The flyway test extension are available at [Maven Central](http://repo1.maven.org/maven2/org/flywaydb/flyway-test-extensions).

For a detail usage description see the [UsageFlywaySpringTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) usage page. Attention: this version has a dependency to spring 4. If spring 3 support is needed use flyway-spring3-test instead.

* add dependency to flyway-spring-test to your Mavan pom file

```xml
    <dependency>
       <groupId>org.flywaydb.flyway-test-extensions</groupId>
       <artifactId>flyway-spring-test</artifactId>
       <version>4.0</version>
       <scope>test</scope>
    </dependency>
```

* Extend your test class with the Spring test runner annotation.

```java
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(locations = {"/context/simple_applicationContext.xml" })
    @TestExecutionListeners({DependencyInjectionTestExecutionListener.class, 
                             FlywayTestExecutionListener.class })
```
    
* add the @[FlywayTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-of-Annotation-FlywayTest) annotation on each class or method were you need a clean database. You can also use the anntotation on class basis and every test method in the class where a clean database is also needed.

```java
    // usage as once per class
    @FlywayTest
    public class Spring4JUnitTest 

    // another TestClass
    
    public class Spring4JUnitTest {
    
    // usage as per test method
    @Test
    @FlywayTest
    public void testMethod() { 
```


Project depend on
-----------------
* [Flyway](https://github.com/flyway/) (4.0)
* [Spring Framework](http://www.springsource.org/) test, context, jdbc (4.2, 3.2 or 2.5.6)

Notes
-----
* The project depends on flyway version 4.0
* The project will be supported until the extension will be integrated into the flyway project.
* The project depends on Spring version 4.x (see flyway-spring4-test and flyway-dbunit-spring4-test)
* The project depends on Spring version 3.2 (see flyways-swpring3-test and flyway-dbunit-spring3-test)
  * All features works with Spring version 3.x
  * It works also with Spring version 2.5.6, but you can not use all features. A example project show how to use it with Spring 2.5.6
* At the moment the code is tested with database H2 and Oracle.<br>Only the DBunit part contains database specific code. 
