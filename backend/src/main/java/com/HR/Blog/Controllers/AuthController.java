package com.HR.Blog.Controllers;

import com.HR.Blog.Entities.User;
import com.HR.Blog.Exceptions.ApiException;
import com.HR.Blog.Payloads.JwtAuthRequest;
import com.HR.Blog.Payloads.JwtAuthResponse;
import com.HR.Blog.Payloads.UserDto;
import com.HR.Blog.Repositories.UserRepo;
import com.HR.Blog.Security.JwtTokenHelper;
import com.HR.Blog.Services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
//@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/auth/")
public class AuthController {

	private final JwtTokenHelper jwtTokenHelper;
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private final ModelMapper mapper;

	@Autowired
	public AuthController(JwtTokenHelper jwtTokenHelper, UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
						  UserService userService, ModelMapper mapper) {
		this.jwtTokenHelper = jwtTokenHelper;
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.mapper = mapper;
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
		try {
			String registeredUser = this.userService.registerNewUser(userDto);
			return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
		}catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered, try logging in !! ");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
		this.authenticate(request.getUsername(), request.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		String token = this.jwtTokenHelper.generateToken(userDetails);

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		response.setUser(this.mapper.map((User) userDetails, UserDto.class));
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);

		try {

			this.authenticationManager.authenticate(authenticationToken);

		} catch (BadCredentialsException e) {
			System.out.println("Invalid Details !!");
			throw new ApiException("Invalid username or password !!");
		}
	}

	@GetMapping("/current-user")
	public ResponseEntity<UserDto> getUser(Principal principal) {
		User user = this.userRepo.findByEmail(principal.getName()).get();
		return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
	}
}
