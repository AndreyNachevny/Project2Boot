package com.springProject.Project2Boot.repositories;

import com.springProject.Project2Boot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer>{

    List<Person> findByFio(String fio);
}
