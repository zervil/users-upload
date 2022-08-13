package com.demo.usersupload;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

  @Query(value = "SELECT * FROM Person WHERE name = ?1", nativeQuery = true)
  public Person findByName(String name);
  
  @Query(value = "SELECT * FROM Person WHERE salary <= ?2 AND salary >= ?1 LIMIT ?3 OFFSET ?4", nativeQuery = true)
  public List<Person> findByParamsLimit(Double min, Double max, Integer limit, Integer offset);

  @Query(value = "SELECT * FROM Person WHERE salary <= ?2 AND salary >= ?1 ORDER BY '?5' ASC LIMIT ?3 OFFSET ?4 ", nativeQuery = true)
  public List<Person> findByParamsLimitSort(Double min, Double max, Integer limit, Integer offset, String sort);

}