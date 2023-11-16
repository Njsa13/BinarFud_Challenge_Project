package com.binarfud.binarfud_challenge7.config;

import com.binarfud.binarfud_challenge7.enums.ERole;
import com.binarfud.binarfud_challenge7.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/login","/swagger-ui", "/api/auth/**", "/swagger-ui.html",
                        "/swagger-ui/**", "/v3/api-docs/**", "/api/product/download-image/{productName}").permitAll()
                .antMatchers("/api/order/**").hasAnyAuthority(ERole.ROLE_CUSTOMER.name(), ERole.ROLE_ADMIN.name())
                .antMatchers("/api/merchant/get-open", "/api/product/get-open").hasAnyAuthority(ERole.ROLE_CUSTOMER.name(),
                        ERole.ROLE_MERCHANT.name(), ERole.ROLE_ADMIN.name())
                .antMatchers("/api/merchant/add", "/api/merchant/update/{merchantName}",
                        "/api/merchant/update-status/{merchantName}", "/api/product/add", "/api/product/get-all",
                        "/api/product/update/{productName}", "/api/product/delete").hasAnyAuthority(ERole.ROLE_MERCHANT.name(), ERole.ROLE_ADMIN.name())
                .antMatchers("/api/user/update", "/api/user/delete",
                        "/api/order/print-invoice").hasAnyRole(ERole.ROLE_CUSTOMER.name(), ERole.ROLE_MERCHANT.name(), ERole.ROLE_ADMIN.name())
                .antMatchers("/api/merchant/get-all", "/api/merchant/delete/{merchantName}",
                        "/api/user/get").hasAuthority(ERole.ROLE_ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(cookieBasedAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public CookieBasedAuthFilter cookieBasedAuthFilter() {
        return new CookieBasedAuthFilter();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
