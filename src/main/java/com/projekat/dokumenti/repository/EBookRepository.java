package com.projekat.dokumenti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projekat.dokumenti.entity.EBook;

@Repository
public interface EBookRepository extends JpaRepository<EBook, Integer> {
	
	// Replace sorting methods with ones that take Pageable
	
	EBook findByTitle(String title);
	
	EBook findByFilename(String filename);
	
	EBook findByDocumentName(String documentName);
	
	List<EBook> findByUser_Id(Integer userId);
	
	List<EBook> findByCategory_Name(String categoryName);
	
	List<EBook> findAllByOrderByTitleDesc();
	
	List<EBook> findAllByOrderByTitleAsc();
	
	List<EBook> findByCategory_NameOrderByTitleDesc(String categoryName);
	
	List<EBook> findByCategory_NameOrderByTitleAsc(String categoryName);
}
