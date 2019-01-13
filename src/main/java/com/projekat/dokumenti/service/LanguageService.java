package com.projekat.dokumenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekat.dokumenti.entity.Language;
import com.projekat.dokumenti.repository.LanguageRepository;

@Service
public class LanguageService implements LanguageServiceInterface {

	@Autowired
	private LanguageRepository languageRepository;
	
	@Override
	public List<Language> findAll() {
		return languageRepository.findAll();
	}

	@Override
	public Language findByName(String name) {
		return languageRepository.findByName(name);
	}

	@Override
	public Language findById(Integer id) {
		Optional<Language> optional = languageRepository.findById(id);
		if(optional.isPresent() == false)
			return null;
		return optional.get();
	}

	@Override
	public Language save(Language language) {
		return languageRepository.save(language);
	}

	@Override
	public void remove(Language language) {
		languageRepository.deleteById(language.getId());
	}

}
