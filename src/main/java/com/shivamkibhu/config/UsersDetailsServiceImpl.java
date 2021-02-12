package com.shivamkibhu.config;

import com.shivamkibhu.dao.UserRepo;
import com.shivamkibhu.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsersDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByEmail();
      User user = userRepo.getUserByUserName(username);

        if(user == null){
            throw new UsernameNotFoundException("Could not find any user!!!");
        }

        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        return customUserDetail;
    }
}
