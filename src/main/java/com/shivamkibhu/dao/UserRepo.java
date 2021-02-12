package com.shivamkibhu.dao;

import com.shivamkibhu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Integer> {
//    public User findByEmail();

    @Query("select u from User u where u.email =:email")
    public User getUserByUserName(@Param("email") String email);
}
