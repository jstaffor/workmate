package com.workmate.server.basicauth.config;

import com.workmate.server.service.MyAppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class BasicAuthConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAppUserDetailsService myAppUserDetailsService;

    @Autowired
    private AppAuthenticationEntryPoint appAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
          .inMemoryAuthentication()
          .withUser("killesk")
          .password(passwordEncoder().encode("m123"))
          .roles("USER");
//        auth.inMemoryAuthentication()
//                .withUser("killesk").password("m123").roles("USER", "USER");
    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//          .authorizeRequests()
//          .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//          .antMatchers("/login").permitAll()
//          .anyRequest()
//          .authenticated()
//          .and()
//          .httpBasic();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.csrf().disable()
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/user/**").permitAll()
//                .antMatchers("/index.html", "/", "/login").permitAll()
////                .antMatchers("/user").hasAnyRole()
//                .anyRequest().authenticated()
//        .and().anonymous().disable()
//        ;

//        http
//                .httpBasic()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/index.html", "/", "/home", "/login").permitAll()
//                .anyRequest().authenticated();

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/jobs/**").authenticated()
                .antMatchers("/interviews/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/login").permitAll()
////                .antMatchers("/user/**").permitAll()
//                .antMatchers("/user/**").hasAnyRole("ADMIN","USER")
//                .and().httpBasic().realmName("MY APP REALM")
//                .authenticationEntryPoint(appAuthenticationEntryPoint);
    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        auth.userDetailsService(myAppUserDetailsService).passwordEncoder(passwordEncoder);
//    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
