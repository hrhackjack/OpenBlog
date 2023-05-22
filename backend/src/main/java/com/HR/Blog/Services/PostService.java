package com.HR.Blog.Services;

import java.util.List;

import com.HR.Blog.Entities.Post;
import com.HR.Blog.Payloads.PostDto;
import com.HR.Blog.Payloads.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);

	PostDto editPost(PostDto postDto, Integer postId);

	public void likePost(Integer postId);

	public void notLikePost(Integer postId);

	public void dislikePost(Integer postId);

	public void notDislikePost(Integer postId);

	void deletePost(Integer postId);

	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	List<Post> getAll();

	PostDto getPostById(Integer postId);

	List<PostDto> getPostsByCategory(Integer categoryId);

	List<PostDto> getPostsByUser(Integer userId);

	List<PostDto> searchPosts(String keyword);
}
