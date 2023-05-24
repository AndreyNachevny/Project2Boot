package com.springProject.Project2Boot.repositories;


import com.springProject.Project2Boot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface   BooksRepository extends JpaRepository<Book,Integer> {
    List<Book> findByNameStartingWith(String name);
}
