flyway-test-extensions
======================

![flyway-test-extension logo](https://github.com/flyway/flyway-test-extensions/blob/master/image/logo-flyway-test-extensions.png) 


Test extensions for the Flyway project
--------------------------------------

For Flyway's features, see the [Flyway Db Org Page](http://flywaydb.org/) 

Version 4.2.0.2 Released 
------------------------

<b>2017-12-02</b> flyway-test-extensions version <b>4.2.0.2</b> released.

Version number 4.2.x are used to show the dependency to Flyway version 4.2.x.

See also [Release Notes](https://github.com/flyway/flyway-test-extensions/wiki/Release-Notes) 

Central maven repository under http://search.maven.org/#search|ga|1|flyway-test-extensions contains all project jars.

Project
-------
This extension gives the ability to reset and/or fill the database with defined content.
With this precondition, each test provides a reproducible database start point. 

* Annotation `FlywayTest` for database unit testing. Use [Flyway](https://github.com/flyway/) feature.
  * <b>clean</b> - execution of flyway task clean
  * <b>init</b> - execution of flyway task init
  * <b>migrate</b> - execution of flyway task migrate
* `FlywayTest` annotation can be used at
  * each test class (once per test case)
  * each test method  
  * together with JUnit Before or BeforeEach annotation
* Annotation `FlywayTests` if more than one database must be controlled during a test. Annotation can be used at 
  * each test class (once per test case)
  * each test method  
  * together with JUnit `Before` or `BeforeEach` annotation
* Sample projects on how to use the annotations inside a unit testing environment
  * Spring 5.x sample (see [complete sample for usage together with Spring 5](https://github.com/flyway/flyway-test-extensions/tree/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-sample-spring5) )
  * Spring 4.x sample (see [UsageFlywaySpringTest4](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) )
  * Spring 2.5.6 (see [UsageFlywaySpringTest256](http://code.google.com/p/flyway-test-extensions/wiki/UsageFlywaySpringTest256) )
  * SpringBoot test example (see [FlywayTestApplicationTest](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/spring-boot-sample-flyway/src/test/java/org/flywaydb/sample/test/spring/boot/flywaytest/FlywayTestApplicationTest.java) )
  * JUnit5 test example (see [Junit5SpringTest](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-junit5/src/test/java/org/flywaydb/sample/test/junit5/Junit5SpringTest.java) )
* Additional project supports a DBUnit annotation use together with FlywayTest [DBUnitSupport](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-dbunit-test/src/main/java/org/flywaydb/test/dbunit/DBUnitSupport.java). A usage example you will find at [UsageFlywayDBUnitTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-dbunit-test).

How to use it
-------------
The flyway test extensions are available at [Maven Central](http://repo1.maven.org/maven2/org/flywaydb/flyway-test-extensions).

For a detail usage description see the [UsageFlywaySpringTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) usage page. Attention: this version has a dependency on Spring 4. If Spring 3 support is needed, use flyway-spring3-test instead.

* Add dependency to flyway-spring-test to your Maven pom file

```xml
    <dependency>
       <groupId>org.flywaydb.flyway-test-extensions</groupId>
       <artifactId>flyway-spring-test</artifactId>
       <version>4.2.0.2</version>
       <scope>test</scope>
    </dependency>
```

* Extend your test class with the Spring test runner annotation (Junit 4).

```java
    @RunWith(SpringRunner.class)
    @ContextConfiguration(locations = {"/context/simple_applicationContext.xml" })
    @TestExecutionListeners({DependencyInjectionTestExecutionListener.class, 
                             FlywayTestExecutionListener.class })
```
    
* Add the @[FlywayTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-of-Annotation-FlywayTest) annotation on each class or method were you need a clean database. You can also use the anntotation on class basis and every test method in the class where a clean database is also needed.

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

* Add the @FlywayTests with @FlywayTest annotation on each class or method were you need a clean multible database setup. 

```java
    // usage as once per class
    @FlywayTests(value = {
	@FlywayTest(flywayName = "flyway1"),   // Flyway configuration for database 1
	@FlywayTest(flywayName = "flyway2")    // Flyway configuration for database 2
    })
    public class Spring4JUnitTest 

    // another TestClass
    
    public class Spring4JUnitTest {
    
    // usage as per test method
    @Test
    @FlywayTests(value = {
	@FlywayTest(flywayName = "flyway3"), // Flyway configuration for database 3
	@FlywayTest(flywayName = "flyway4")  // Flyway configuration for database 4
    })
    public void testMethod() { 
```

* Junit 5 support is only available together with Spring5 and need a different Spring setup.<br/> 
A step by step setup can be found [here](https://github.com/flyway/flyway-test-extensions/wiki/How-to-use-Flyway-Test-with-Junit5-and-Springframework-5).

```java
@ExtendWith({SpringExtension.class})
@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		FlywayTestExecutionListener.class })
@FlywayTest         // as class annotation
public class Junit5SpringTest ...

    @BeforeEach
    @FlywayTest(locationsForMigrate = {"loadmsqlbefore"})  // together with BeforeEach
    public void before() {
    ...

    @Test
    @FlywayTest       // as method annotation
    public void testMethodLoad() {

```

Project depend on
-----------------
* [Flyway](https://github.com/flyway/) (4.2.0)
* [Spring Framework](http://www.springsource.org/) test, context, jdbc (5.0.2, 4.3.13, 3.2 or 2.5.6)

Notes
-----
* The project depends on flyway version 4.2.0
* The project will be supported until the extension will be integrated into the flyway project.
* The project depends on Spring version 5.x (see flyway-spring5-test)
* The project depends on Spring version 4.x (see flyway-spring4-test and flyway-dbunit-spring4-test)
* The project depends on Spring version 3.2 (see flyways-swpring3-test and flyway-dbunit-spring3-test)
  * All features works with Spring version 3.x
  * It works also with Spring version 2.5.6, but you can not use all features. A example project show how to use it with Spring 2.5.6
* At the moment the code is tested with database H2 and Oracle.<br>Only the DBunit part contains database specific code. 
