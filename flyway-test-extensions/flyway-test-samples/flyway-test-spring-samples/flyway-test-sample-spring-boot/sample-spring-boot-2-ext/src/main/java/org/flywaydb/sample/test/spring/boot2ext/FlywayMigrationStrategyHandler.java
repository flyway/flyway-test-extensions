package org.flywaydb.sample.test.spring.boot2ext;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

public class FlywayMigrationStrategyHandler implements FlywayMigrationStrategy {
    @Override
    public void migrate(Flyway flyway) {
        flyway.migrate();
    }
}
