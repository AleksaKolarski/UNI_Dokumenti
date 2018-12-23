package com.projekat.dokumenti.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projekat.dokumenti.DokumentiApplication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Filter koji ce presretati svaki zahtev klijenta ka serveru
//Sem nad putanjama navedenim u WebSecurityConfig.configure(WebSecurity web)
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LogManager.getLogger(DokumentiApplication.class);

    private TokenHelper tokenHelper;

    private UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService) {
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String username;
        String authToken = tokenHelper.getToken(request);

        if (authToken != null) {
            //uzmi username iz tokena
            username = tokenHelper.getUsernameFromToken(authToken);
            if (username != null) {
                // uzmi user-a na osnovu username-a
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("User " + username + " is accessing with token");
                //proveri da li je prosledjeni token validan
                if (tokenHelper.validateToken(authToken, userDetails)) {
                    // kreiraj autentifikaciju
                    TokenBasedAuthentication authentication = new TokenBasedAuthentication(authToken, userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("User " + username + " accessed with valid token");
                }
                else {
                	logger.info("User " + username + " tried to access with invalid token");
                }
            }
            else{
            	logger.info("AuthToken has no username");
            }
        }
        else {
        	logger.info("Request without AuthToken");
        }
        chain.doFilter(request, response);
    }
}