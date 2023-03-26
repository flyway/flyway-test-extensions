package org.flywaydb.sample.test.spring.boot27;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PersonRepository extends CrudRepository<Person, Long> {

}
