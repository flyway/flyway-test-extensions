package org.flywaydb.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Eddú Meléndez
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlywayTests {

	FlywayTest[] value();

}
