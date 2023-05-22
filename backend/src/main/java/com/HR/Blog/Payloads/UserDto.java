package com.HR.Blog.Payloads;

import java.util.HashSet;
import java.util.Set;


import com.HR.Blog.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {
	private Integer id;
	@NotEmpty(message = "Name can't be empty !!")
	@Size(min = 3, message = "Name must contain at least 3 characters !!")
	private String name;

	private String imageName;

	@Email(message = "Invalid Email Format !!")
	@NotEmpty(message = "Email is required !!")
	         //(message = "Email  already registered, try logging in !! ")
	private String email;

	@NotEmpty(message = "Password is required")
	@Size(min = 8, max = 35, message = "Password must be min of 8 chars and max of 35 characters !!")
	private String password;

	@NotEmpty(message = "This field is required !!")
	private String about;

	@JsonIgnore
	private Set<User> followers = new HashSet<>();

	@JsonIgnore
	private Set<User> following = new HashSet<>();
	
	private Set<RoleDto> roles = new HashSet<>();
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password=password;
	}

}
