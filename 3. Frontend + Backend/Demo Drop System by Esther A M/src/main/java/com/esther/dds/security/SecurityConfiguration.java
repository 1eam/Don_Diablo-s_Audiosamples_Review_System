package com.esther.dds.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //set all mapplings & their permissions
        http
                //todo, read into this (how to make it shorter so I dont have to declare everything
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to("info")).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                .antMatchers("/actuator/").hasRole("ADMIN")
                .antMatchers("/").hasRole("USER")
                .antMatchers("/login").permitAll()
                .antMatchers("/activate/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/dashboard").hasRole("USER")
                .antMatchers("/dropdemo").hasRole("USER")
                .antMatchers("/demo/{id}").hasRole("USER")
                .antMatchers("/delete").hasRole("USER")
                .antMatchers("/settings").hasRole("USER")


                .antMatchers("/h2-console/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("email")
                .and()
                .logout()
                .and()
                .rememberMe()
               .and()
               .csrf().disable()
               .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}