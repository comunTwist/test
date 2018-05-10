package com.gmail.agentup.repository;

import com.gmail.agentup.model.UserOwner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserOwner, Long> {
    @Query("SELECT u FROM UserOwner u WHERE u.login = :login")
    UserOwner findByLogin(@Param("login") String login);

    @Query("SELECT u FROM UserOwner u")
    List<UserOwner> findAll();

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserOwner u WHERE u.login = :login")
    boolean existsByLogin(@Param("login") String login);
}
