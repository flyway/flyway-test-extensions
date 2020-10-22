flyway-test-extensions
======================

![flyway-test-extension logo](https://github.com/flyway/flyway-test-extensions/blob/master/image/logo-flyway-test-extensions.png) 


Test extensions for the Flyway project
--------------------------------------

For Flyway's features, see the [Flyway Db Org Page](http://flywaydb.org/) 

Version 7.0.0 Released 
----------------------

<b>2020-10-22</b> flyway-test-extensions version <b>7.0.0</b> released.

Version number 7.0.x are used to show the dependency to Flyway version 7.0.x.

See also [Release Notes](https://github.com/flyway/flyway-test-extensions/wiki/Release-Notes) 

Central maven repository under http://search.maven.org/#search|ga|1|flyway-test-extensions contains all project jars.

Project
-------
This extension gives the ability to reset and/or fill the database with defined content.<br>
With this precondition, each test provides a reproducible database start point. 

* Annotation `FlywayTest` for database unit testing. Use [Flyway](https://github.com/flyway/) feature.
  * <b>clean</b> - execution of flyway task clean
  * <b>init</b> - execution of flyway task init
  * <b>migrate</b> - execution of flyway task migrate
* Annotation `FlywayTest` for a single database control.<br>
  Annotation `FlywayTests` if more than one database must be controlled during a test. <br>
  Annotations can be used
  
  | Test Setup | Junit 4  | Junit 5 | TestNG |
  | --- | --- | --- | --- |
  | Once per Class | :white_check_mark: | :white_check_mark: | :white_check_mark: |
  | Once per Class with test annotation | `@BeforeClass` | `@BeforeAll` | `@BeforeClass` |
  | Once per Method | `@Before` | `@BeforeEach` | `@BeforeMethod` |
  | per Method| `@Test` | `@Test` | `@Test` |
  
* Sample projects on how to use the annotations inside a unit testing environment
  * Spring 5.x sample (see [complete sample for usage together with Spring 5](https://github.com/flyway/flyway-test-extensions/tree/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-sample-spring5) )
  * Spring 4.x sample (see [UsageFlywaySpringTest4](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) )
  * Spring 3.x sample 
  * SpringBoot 2 test example (see [FlywayTestApplicationTest](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-sample-spring-boot/sample-spring-boot-2/src/test/java/org/flywaydb/sample/test/spring/boot2/flywaytest/FlywayTestApplicationTest.java) )
  * JUnit5 test example (see [Junit5SpringTest](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-junit5/src/test/java/org/flywaydb/sample/test/junit5/Junit5SpringTest.java) )
  * TestNG test example (see [BeforeMethodTest](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-test-samples/flyway-test-spring-samples/flyway-test-testng/src/test/java/org/flywaydb/sample/test/testng/BeforeMethodTest.java) )
* Additional project supports a DBUnit annotation use together with FlywayTest [DBUnitSupport](https://github.com/flyway/flyway-test-extensions/blob/master/flyway-test-extensions/flyway-dbunit-test/src/main/java/org/flywaydb/test/dbunit/DBUnitSupport.java). A usage example you will find at [UsageFlywayDBUnitTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-dbunit-test).

How to use it
-------------
The flyway test extensions are available at [Maven Central](http://repo1.maven.org/maven2/org/flywaydb/flyway-test-extensions).

For a detail usage description see the [UsageFlywaySpringTest](https://github.com/flyway/flyway-test-extensions/wiki/Usage-flyway-spring-test) usage page. <br>
*Attention:* 
* this version has a dependency on Spring 5. 
* The project is build with Java 8 compiler settings simular to Flyway project.<br>
  If other compiler classes are needed use flyway-test-spring4 or flyway-test-spring3.

*Integration*
* Add dependency to flyway-spring-test to your Maven pom file

```xml
    <dependency>
       <groupId>org.flywaydb.flyway-test-extensions</groupId>
       <artifactId>flyway-spring-test</artifactId>
       <version>7.0.0</version>
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

* Add the @FlywayTests with @FlywayTest annotation on each class or method were you need a clean multiple database setup. 

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
@ExtendWith({FlywayTestExtension.class})
@ContextConfiguration(locations = { "/context/simple_applicationContext.xml" })
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

* TestNG support is only available with Spring5 and need a different Test setup.

```java
@ContextConfiguration(locations = {"/context/simple_applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        FlywayTestExecutionListener.class})
@Test
@FlywayTest(locationsForMigrate = {"loadmsql"}) // execution once per class
public class MethodTest extends AbstractTestNGSpringContextTests {
    
    @BeforeClass
    @FlywayTest(locationsForMigrate = {"loadmsql"}) // execution once per class
    public static void beforeClass(
    
    @BeforeMethod
    @FlywayTest(locationsForMigrate = {"loadmsql"}) // execution before each test method
    public void beforeMethod(


    @Test
    @FlywayTest(locationsForMigrate = {"loadmsql"}) // as method annotation
    public void simpleCountWithoutAny(

```

Project depend on
-----------------
* [Flyway](https://github.com/flyway/) (7.0.0)
* [Spring Framework](http://www.springsource.org/) test, context, jdbc (5.2.6, 4.3.25, 3.2)

Notes
-----
* The project depends on flyway version 7.0.4
* The project will be supported until the extension will be integrated into the flyway project.
* The project depends on Spring version 5.x (see flyway-spring5-test)
* The project depends on Spring version 4.x (see flyway-spring4-test and flyway-dbunit-spring4-test)
* The project depends on Spring version 3.2 (see flyways-swpring3-test and flyway-dbunit-spring3-test)
* At the moment the code is tested with database H2 and Oracle.<br>Only the DBunit part contains database specific code. 
