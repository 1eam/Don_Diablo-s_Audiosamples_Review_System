package com.esther.dds.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(1)
public class BoSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private BoUserDetailsServiceImpl boUserDetailsService;

    public BoSecurityConfiguration(BoUserDetailsServiceImpl boUserDetailsService) {
        this.boUserDetailsService = boUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //set all mapplings & their permissions
        //todo, read into this (how to make it shorter so I dont have to declare everything
        http.
                antMatcher("/bo-side/**")
                .authorizeRequests()
                    .antMatchers("/bo-side/authorized/**").hasRole("BO-USER")
                    .antMatchers("/bo-side/**").permitAll()
                .and()
                .formLogin()
                    .loginPage("/bo-side/login").permitAll()
                    .loginProcessingUrl("/bo-side/login")
                    .usernameParameter("email")
                .and()
                    .logout()
                    .logoutUrl("/bo-side/logout")
                .and()
                    .rememberMe();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(boUserDetailsService);
    }
}