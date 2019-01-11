package com.projekat.dokumenti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekat.dokumenti.entity.EBook;

@Repository
public interface EBookRepository extends JpaRepository<EBook, Integer> {
	EBook findByTitle(String title);
	
	List<EBook> findByUser_Id(Integer userId);
}
