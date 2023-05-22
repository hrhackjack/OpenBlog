package com.HR.Blog.Repositories;

import com.HR.Blog.Entities.PasswordResetToken;
import com.HR.Blog.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PRTRepo extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);

}
