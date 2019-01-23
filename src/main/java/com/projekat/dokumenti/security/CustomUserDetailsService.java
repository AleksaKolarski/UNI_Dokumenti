package com.projekat.dokumenti.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	protected final Logger logger = LogManager.getLogger(DokumentiApplication.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	logger.info("Could not find user with username '" + username + "'");
        	return null;
        } else {
            return user;
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();
        if (authenticationManager != null) {
        	logger.info("Re-authenticating user '"+ username + "' for password change request.");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
        	logger.info("No authentication manager set. can't change Password!");
            return false;
        }
        logger.info("Changing password for user '"+ username + "'");
        User user = (User) loadUserByUsername(username);
        if(user == null) {
        	return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user = userRepository.save(user);
        if(user == null) {
        	logger.info("Could not change password!");
        	return false;
        }
        return true;
    }
}