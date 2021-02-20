package com.shivamkibhu.dao;

import com.shivamkibhu.entities.Contact;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ContactRepo extends JpaRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.id =:id")
    public Page<Contact> findContactsByUser(@Param("id") int id, Pageable pageable);

}
