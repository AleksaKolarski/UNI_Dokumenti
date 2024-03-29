package com.projekat.dokumenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<User> findAll(){
		return userRepository.findAll();
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}
	
	@Override
	public User findById(Integer userId) {
		Optional<User> optional = userRepository.findById(userId);
		if(optional.isPresent() == false)
			return null;
		return optional.get();
	}
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public void remove(User user) {
		userRepository.deleteById(user.getId());
	}
}
