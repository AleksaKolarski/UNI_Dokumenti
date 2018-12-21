package com.projekat.dokumenti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        		.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
        	.and()
        		.authorizeRequests()
                	.antMatchers(HttpMethod.POST, "/user/register").permitAll()
                	.antMatchers(HttpMethod.GET, "/user/currentUser").permitAll()
                	.anyRequest().authenticated()
            .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()));
        http.csrf().disable();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception{
    	web.ignoring().antMatchers(HttpMethod.POST, "/user/login");
    	web.ignoring().antMatchers(
    								HttpMethod.GET, 
    								"/",
    								"/webjars/**",
    								"/*.html",
    								"/favicon.ico",
    								"/**/*.html",
    								"/**/*.css",
    								"/**/*.js"
    								);
    }

    /*
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    */
}
