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
@Order(2)
public class BoSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private BoUserDetailsServiceImpl boUserDetailsService;
    public BoSecurityConfiguration(BoUserDetailsServiceImpl boUserDetailsService) {
        this.boUserDetailsService = boUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //set all mappings & their permissions
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
                    .defaultSuccessUrl("/bo-side/authorized/dashboard")
                .and()
                    .logout()
                    .logoutUrl("/bo-side/authorized/logout")
                .and()
                    .rememberMe();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(boUserDetailsService);
    }
}