package com.shivamkibhu.dao;

import com.shivamkibhu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
