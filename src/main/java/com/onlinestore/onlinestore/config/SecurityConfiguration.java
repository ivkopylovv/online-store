package com.onlinestore.onlinestore.config;

import com.onlinestore.onlinestore.filter.AuthenticationFilter;
import com.onlinestore.onlinestore.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable().
                sessionManagement().sessionCreationPolicy(STATELESS).
                and().
                authorizeRequests().antMatchers(
                        "/api/login/**",
                        "/api/token/refresh/**",
                        "/api/registration/**",
                        "/api/logout/**").permitAll().
                and().
                authorizeRequests().antMatchers(
                        "api/products/add-product/**",
                        "api/products/update-product/**",
                        "api/products/delete-product/**").hasAnyAuthority("ROLE_ADMIN").
                and().
                authorizeRequests().antMatchers(
                        "api/products/count-page/**",
                        "api/favourites/**",
                        "api/cart/**").hasAnyAuthority( "ROLE_USER").
                and().
                authorizeRequests().antMatchers(
                        "api/products/get-product/**",
                        "api/products/get-page/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN").
                and().
                authorizeRequests().anyRequest().authenticated().
                and().
                addFilter(authenticationFilter).
                addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
