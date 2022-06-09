package com.onlinestore.onlinestore.config;

import com.onlinestore.onlinestore.filter.AuthenticationFilter;
import com.onlinestore.onlinestore.filter.AuthorizationFilter;
import com.onlinestore.onlinestore.service.TokenService;
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

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), tokenService);
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/login/**",
                        "/api/token/refresh/**",
                        "/api/registration/**",
                        "/api/logout/**",
                        "/api/products/get-product/**",
                        "/api/products/get-page/**",
                        "/api/products/count-page/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/products/add-product/**",
                        "/api/products/update-product/**",
                        "/api/products/delete-product/**").hasAnyAuthority("ROLE_ADMIN").
                and()
                .authorizeRequests()
                .antMatchers(
                        "/api/favourites/**",
                        "/api/cart/**").hasAnyAuthority("ROLE_USER")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
