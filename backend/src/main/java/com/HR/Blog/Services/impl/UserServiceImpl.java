package com.HR.Blog.Services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.HR.Blog.Configuration.AppConstants;
import com.HR.Blog.Entities.PasswordResetToken;
import com.HR.Blog.Entities.Role;
import com.HR.Blog.Entities.User;
import com.HR.Blog.Exceptions.ResourceNotFoundException;
import com.HR.Blog.Payloads.UserDto;
import com.HR.Blog.Repositories.PRTRepo;
import com.HR.Blog.Repositories.RoleRepo;
import com.HR.Blog.Repositories.UserRepo;

import com.HR.Blog.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService  {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PRTRepo prtRepo;

	@Override
	public UserDto addUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		user.setImageName("profile.png");
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		user.setImageName(user.getImageName());
		user.setAbout(userDto.getAbout());

		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updatedUser);
		return userDto1;
	}

	@Override
	public UserDto updateImageName(UserDto userDto, Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		user.setImageName(userDto.getImageName());

		User updatedUser = this.userRepo.save(user);
		UserDto updatedUserDto = this.userToDto(updatedUser);
		return updatedUserDto;
	}

	@Override
	public UserDto getUserById(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		this.userRepo.delete(user);

	}

	public User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	public UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public String registerNewUser(UserDto userDto) {

		User user = this.modelMapper.map(userDto, User.class);
		user.setImageName("profile.png");
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		this.modelMapper.map(newUser, UserDto.class);
		return "User Registered Successfully !! ";
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userRepo.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFoundException("User ", " Email : " + username, 0));
	}



	@Override
	public User followUser(int followerId, int userId ) {
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		User follower = this.userRepo.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		System.out.println(user); System.out.println(follower);
		user.getFollowers().add(follower);
		this.userRepo.save(user);
		return user;
	}


	@Override
	public User unfollowUser( int followerId, int userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		User follower = userRepo.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		user.getFollowers().remove(follower);
		userRepo.save(user);
		return user;
	}


	@Override
	public Set<User> getFollowers(int userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		return user.getFollowers();
	}


	@Override
	public Set<User> getFollowing(int userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","ID",userId));
		return user.getFollowing();
	}

	@Override
	public void createPRTForUser(User user, String token) {
		PasswordResetToken prtoken = new  PasswordResetToken();
		prtoken.setToken(token);
		prtoken.setUser(user);
		prtoken.setExpiryDate(LocalDateTime.now().plusHours(24));
		prtRepo.save(prtoken);
	}

	@Override
	public PasswordResetToken getFromPRT(String token) {
		System.out.println(prtRepo.findByToken(token));
		return prtRepo.findByToken(token);
	}

	@Override
	public void deletePRTForUser(User user) {
		PasswordResetToken prt = prtRepo.findByUser(user);
		if (prt != null) {
			prtRepo.delete(prt);
		}
	}
}
