package com.projekat.dokumenti.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projekat.dokumenti.DokumentiApplication;
import com.projekat.dokumenti.entity.User;
import com.projekat.dokumenti.security.CustomUserDetailsService;
import com.projekat.dokumenti.security.TokenHelper;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping( value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE )
public class AuthenticationController {
	
	protected final Logger logger = LogManager.getLogger(DokumentiApplication.class);

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        // Izvrsavanje security dela
    	Authentication authentication;
    	try {
    		authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.username,
                            authenticationRequest.password
                    )
            );
    		logger.info("User " + authenticationRequest.username + " successfully logged in");
    	}catch (AuthenticationException e) {
    		logger.info("Someone tried to log in with wrong credentials");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong username/password.");
		}
        
        // Ubaci username + password u kontext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token
        User user = (User)authentication.getPrincipal();
        String jws = tokenHelper.generateToken( user.getUsername());
        
        // Vrati token kao odgovor na uspesno autentifikaciju
        return ResponseEntity.ok(jws);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
        userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
        Map<String, String> result = new HashMap<>();
        result.put( "result", "success" );
        return ResponseEntity.accepted().body(result);
    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }
    static class JwtAuthenticationRequest {
    	public String username;
        public String password;
    }
}