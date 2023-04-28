package com.myapp.mybackend;

import com.myapp.mybackend.data.MyUserRepository;
import com.myapp.mybackend.models.MyUser;
import com.myapp.mybackend.models.MyUserAuthorityEnum;
import com.myapp.mybackend.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

// Annotated with @EnableWebSecurity to enable Spring Security’s web security
// support and provide the Spring MVC integration. It also exposes two beans
// to set some specifics for the web security configuration:
// SecurityFilterChain and UserDetailsService

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private MyUserRepository myUserRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // The SecurityFilterChain bean defines which URL paths should be secured
    // and which should not.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        //.anyRequest().authenticated()
                        .anyRequest().hasRole(MyUserAuthorityEnum.ROLE_USER.getRoleName())
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll())
        ;
        return http.build();
    }

    // UserDetailsService is used by DaoAuthenticationProvider for retrieving a username,
    // a password, and other attributes for authenticating with a username and password
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // Create some default users for testing
        //TODO Remove user creation code in production
        MyUser user = MyUser.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("user")
                .password("fake password so we don't check raw secrets into version control")
                .authorities(
                        MyUserAuthorityEnum.ROLE_USER,
                        MyUserAuthorityEnum.ROLE_ADMIN,
                        MyUserAuthorityEnum.CAN_VIEW_COMMENTS)
                .build();
        user.setPassword("{bcrypt}$2a$10$pwTCw6PyH5AodegZY5grDOdFXK7zTLGKbFcw4EnFrYv22pkcvTBhC");
        if (myUserRepository.findByUsername(user.getUsername()) == null) {
            myUserRepository.save(user);
        }

        // Used by JPA (DaoAuthenticationProvider) authentication provider
        MyUserDetailsService userDetailsService = new MyUserDetailsService();
        return userDetailsService;
    }
}
