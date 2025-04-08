package com.example.arrowdb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        return http
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                        .csrfTokenRepository(csrfTokenRepository)
                        .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository)))
                .authorizeHttpRequests(authUsers -> authUsers.requestMatchers("/general/**")
                        .authenticated())
                .formLogin(formUsers -> formUsers
                        .loginPage("/login")
                        .defaultSuccessUrl("/general", true)
                        .permitAll())
                .logout(logout -> logout.logoutRequestMatcher((new AntPathRequestMatcher("/logout")))
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)))
                        .permitAll())
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}