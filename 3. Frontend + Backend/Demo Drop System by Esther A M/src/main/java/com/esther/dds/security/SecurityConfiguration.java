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
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //set all mappings & their permissions
        http.
                antMatcher("/user-side/**")
                .authorizeRequests()
                    .requestMatchers(EndpointRequest.to("info")).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                    .antMatchers("/actuator/").hasRole("ADMIN")

                    .antMatchers("/user-side/authorized/**").hasRole("USER")
                    .antMatchers("/user-side/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                .and()
                .formLogin()
                    .loginPage("/user-side/login").permitAll()
                    .loginProcessingUrl("/user-side/login")
                    .usernameParameter("email")
                    .defaultSuccessUrl("/user-side/authorized/dashboard")
                .and()
                    .logout()
                    .logoutUrl("/user-side/logout")
                .and()
                    .rememberMe()// session expires after 2 weeks
                .and()
                    .csrf();

//                .and().csrf().ignoringAntMatchers("/h2-console/**") //don't apply CSRF protection to /h2-console
//                .and().headers().frameOptions().sameOrigin(); //allow use of frame to same origin urls

                /*
                Disable the above two lines of code so the DATABASE will be protected against csrf. current state: disabled
                To make it possible to both run the application and inspect the DB, you should have ignoreAntMatchers nd frameOptions enabled
                In this case: uncomment the last 2 lines

                CSRF should needs to be turned off in production.
                 */

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}