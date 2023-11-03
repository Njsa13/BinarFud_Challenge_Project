package com.binarfud.binarfud_challenge6.config;

import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/login","/swagger-ui", "/api/auth/**", "/swagger-ui.html",
                        "/swagger-ui/**", "/v3/api-docs/**", "/api/product//download-image/{productName}")
                .permitAll()
                .antMatchers("/api/merchant/get-open", "/api/product/get-open")
                .hasAnyAuthority(ERole.ROLE_CUSTOMER.name(), ERole.ROLE_MERCHANT.name(), ERole.ROLE_ADMIN.name())
                .antMatchers("/api/order/**")
                .hasAuthority(ERole.ROLE_CUSTOMER.name())
                .antMatchers("/api/merchant/add", "/api/merchant/update/{merchantName}",
                        "/api/merchant/update-status/{merchantName}", "/api/product/**")
                .hasAuthority(ERole.ROLE_MERCHANT.name())
                .antMatchers("/api/user/update/{username}", "/api/user/delete/{username}", "/api/order/print-invoice/{username}")
                .access("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MERCHANT') and #username == authentication.name")
                .antMatchers("/api/auth/**", "/api/merchant/**", "/api/order/**", "/api/product/**", "/api/user/**")
                .hasAuthority(ERole.ROLE_ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/swagger-ui/index.html")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .httpBasic();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
