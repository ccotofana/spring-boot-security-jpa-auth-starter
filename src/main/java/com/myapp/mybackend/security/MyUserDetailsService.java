package com.myapp.mybackend.security;

import com.myapp.mybackend.data.MyUserRepository;
import com.myapp.mybackend.models.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom UserDetailsService implementation that uses JPA-managed ManaUser records
 * for authentication.
 *
 * UserDetailsService is used by DaoAuthenticationProvider for retrieving a username,
 * a password, and other attributes for authenticating with a username and password.
 *
 * Adding "@Service" to this class results in the DaoAuthenticationProvider not being
 * used during login form submission because MyUserDetailsService is also declared
 * as a bean in the SecurityConfig class.
 */
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private MyUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = myUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetails(user);
    }
}
