package com.HR.Blog.Services.impl;
import com.HR.Blog.Entities.Comments;
import com.HR.Blog.Entities.Post;
import com.HR.Blog.Exceptions.ResourceNotFoundException;
import com.HR.Blog.Payloads.CommentDto;
import com.HR.Blog.Repositories.CommentRepo;
import com.HR.Blog.Repositories.PostRepo;
import com.HR.Blog.Services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id ", postId));

		Comments comment = (Comments) this.modelMapper.map(commentDto, Comments.class);

		comment.setPost(post);

		Comments savedComment = this.commentRepo.save(comment);

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {

		Comments com = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
		this.commentRepo.delete(com);
	}

}
