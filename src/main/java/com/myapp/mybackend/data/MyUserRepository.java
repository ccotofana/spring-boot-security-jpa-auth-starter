package com.myapp.mybackend.data;

import com.myapp.mybackend.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
    MyUser findByUsername(String username);
}
