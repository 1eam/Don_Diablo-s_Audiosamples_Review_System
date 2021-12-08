package com.esther.dds.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(3)
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private AdminDetailsServiceImpl adminDetailsService;
    public AdminSecurityConfiguration(AdminDetailsServiceImpl adminDetailsService) {
        this.adminDetailsService = adminDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //set all mappings & their permissions
        http.
                antMatcher("/admin-side/**")
                .authorizeRequests()
                    .antMatchers("/admin-side/authorized/**").hasRole("ADMIN")
                    .antMatchers("/admin-side/**").permitAll()
                .and()
                .formLogin()
                    .loginPage("/admin-side/login").permitAll()
                    .loginProcessingUrl("/admin-side/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/admin-side/authorized/dashboard")
                .and()
                    .logout()
                    .logoutUrl("/admin-side/authorized/logout")
                .and()
                    .rememberMe();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsService);
    }
}