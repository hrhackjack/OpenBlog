package com.HR.Blog.Repositories;


import com.HR.Blog.Entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo  extends JpaRepository<Comments, Integer> {

}
