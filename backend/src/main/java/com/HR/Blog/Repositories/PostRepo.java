package com.HR.Blog.Repositories;


import java.util.List;

import com.HR.Blog.Entities.Category;
import com.HR.Blog.Entities.Post;
import com.HR.Blog.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	@Query("select p from Post p where p.title like :key OR p.content like :key")
	List<Post> searchByTitle(@Param("key") String title);
}
