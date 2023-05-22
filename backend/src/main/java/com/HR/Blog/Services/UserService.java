package com.HR.Blog.Services;

import java.util.List;
import java.util.Set;

import com.HR.Blog.Entities.PasswordResetToken;
import com.HR.Blog.Entities.User;
import com.HR.Blog.Payloads.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	String registerNewUser(UserDto user);

	UserDto addUser(UserDto user);

	UserDto updateUser(UserDto user, Integer userId);

	UserDto updateImageName(UserDto userDto, Integer userId);

	UserDto getUserById(Integer userId);

	List<UserDto> getAllUsers();

	void deleteUser(Integer userId);

	public User followUser( int followerId, int userId);

	public User unfollowUser( int followerId, int userId);

	public Set<User> getFollowers(int userId);

	public Set<User> getFollowing(int userId);

	public void createPRTForUser(User user, String token);

	public PasswordResetToken getFromPRT(String token);

	public void deletePRTForUser(User user);


}
