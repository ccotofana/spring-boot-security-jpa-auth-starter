package com.myapp.mybackend.security;

import com.myapp.mybackend.models.MyUser;
import com.myapp.mybackend.models.MyUserAuthorityEnum;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class MyUserDetails implements UserDetails, CredentialsContainer {
    private MyUser myUser;

    public MyUserDetails(MyUser myUser) {
        this.myUser = myUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (MyUserAuthorityEnum authEnum : this.myUser.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authEnum.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return myUser.getPassword();
    }

    @Override
    public String getUsername() {
        return myUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !myUser.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !myUser.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !myUser.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return myUser.isEnabled();
    }

    @Override
    public void eraseCredentials() {
        this.myUser.setPassword(null);
    }


}
