package com.HR.Blog.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.HR.Blog.Entities.User;
import com.HR.Blog.Payloads.ApiResponse;
import com.HR.Blog.Payloads.PostDto;
import com.HR.Blog.Payloads.UserDto;
import com.HR.Blog.Repositories.UserRepo;
import com.HR.Blog.Services.FileService;
import com.HR.Blog.Services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
//@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;
	@Autowired
	private UserRepo userRepo;

	@PostMapping("/add")
	public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
		UserDto createUserDto = this.userService.addUser(userDto);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) {
		UserDto updatedUser = this.userService.updateUser(userDto, uid);
		return ResponseEntity.ok(updatedUser);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
	}

	@GetMapping("/get/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	@PutMapping("/user/imageUpload/{userId}")
	public ResponseEntity<UserDto> uploadUserImage(@RequestParam("image") MultipartFile image,
												   @PathVariable Integer userId) throws IOException {

		UserDto userDto = this.userService.getUserById(userId);

		String fileName = this.fileService.uploadImage(path, image);
		userDto.setImageName(fileName);
		UserDto updateUser = this.userService.updateImageName(userDto, userId);
		return new ResponseEntity<UserDto>(updateUser, HttpStatus.OK);
	}

	@GetMapping(value = "/user/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
	) throws IOException {

		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream())   ;
	}

	@PutMapping("/{followerId}/follow/{userId}")
	public ResponseEntity<User> followUser(@PathVariable int userId, @PathVariable int followerId) {
		User user =this.userService.followUser(followerId, userId);
		return ResponseEntity.ok().body(user);
	}

	@PutMapping("/{followerId}/unfollow/{userId}")
	public ResponseEntity<User> unfollowUser(@PathVariable int userId, @PathVariable int followerId) {
		User user = this.userService.unfollowUser(followerId, userId);
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/{userId}/followers")
	public ResponseEntity<Set<User>> getFollowers(@PathVariable int userId) {
		Set<User> followers = this.userService.getFollowers(userId);
		return ResponseEntity.ok().body(followers);
	}

	@GetMapping("/{userId}/following")
	public ResponseEntity<Set<User>> getFollowing(@PathVariable int userId) {
		Set<User> following = this.userService.getFollowing(userId);
		return ResponseEntity.ok().body(following);
	}

	@GetMapping("/name/{email}")
	public String getNameByEmail(@PathVariable String email) {
		return userRepo.findNameByEmail(email);
	}
}
