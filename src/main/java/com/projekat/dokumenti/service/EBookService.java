package com.projekat.dokumenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekat.dokumenti.entity.EBook;
import com.projekat.dokumenti.repository.EBookRepository;

@Service
public class EBookService implements EBookServiceInterface {
	
	@Autowired
	private EBookRepository ebookRepository;

	@Override
	public List<EBook> findAll() {
		return ebookRepository.findAll();
	}

	@Override
	public EBook findByTitle(String title) {
		return ebookRepository.findByTitle(title);
	}

	@Override
	public EBook findById(Integer id) {
		Optional<EBook> optional = ebookRepository.findById(id);
		if(optional.isPresent() == false) {
			return null;
		}
		return optional.get();
	}

	@Override
	public EBook findByFilename(String filename) {
		return null;
	}

	@Override
	public List<EBook> findByUserId(Integer userId) {
		return ebookRepository.findByUser_Id(userId);
	}

	@Override
	public EBook save(EBook ebook) {
		return ebookRepository.save(ebook);
	}

	@Override
	public void remove(EBook ebook) {
		ebookRepository.deleteById(ebook.getId());
	}
}
