package com.tuyweb.adso_ml.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuyweb.adso_ml.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
