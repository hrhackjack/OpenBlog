package com.HR.Blog.Repositories;


import java.util.Optional;

import com.HR.Blog.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	@Query("SELECT u.name FROM User u WHERE u.email = :email")
	String findNameByEmail(@Param("email") String email);
}
