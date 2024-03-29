package com.projekat.dokumenti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.projekat.dokumenti.security.CustomUserDetailsService;
import com.projekat.dokumenti.security.RestAuthenticationEntryPoint;
import com.projekat.dokumenti.security.TokenAuthenticationFilter;
import com.projekat.dokumenti.security.TokenHelper;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( jwtUserDetailsService )
            .passwordEncoder( passwordEncoder() );
    }

    @Autowired
    TokenHelper tokenHelper;
    
    //Definisemo prava pristupa odredjenim URL-ovima
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        		//komunikacija izmedju klijenta i servera je stateless
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ).and()
                //za neautorizovane zahteve posalji 401 gresku
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .authorizeRequests()
                	//svim korisnicima dopusti da pristupe putanjama /auth/login
                	.antMatchers("/auth/login").permitAll()
                	.antMatchers("/category/all").permitAll()
                	.antMatchers("/ebook/all").permitAll()
                	.antMatchers("/language/all").permitAll()
                	.antMatchers("/search/search").permitAll()
                	.antMatchers("/user/currentUser").permitAll()
                	.antMatchers("/user/register").permitAll()
                	.antMatchers("/file/download/*").permitAll()
                	.antMatchers("/search/**").permitAll()
                	//svaki zahtev mora biti autorizovan
                	.anyRequest().authenticated().and()
                //presretni svaki zahtev filterom
                .addFilterBefore(new TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService), BasicAuthenticationFilter.class);
        http.csrf().disable();
    }

    //Generalna bezbednost aplikacije
    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/*.html",
                "/favicon.ico",
                "/img/**",
                "/lib/**",
                "/js/**",
                "/css/**"
            );
    }
}