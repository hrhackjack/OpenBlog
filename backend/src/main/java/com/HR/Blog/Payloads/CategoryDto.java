package com.HR.Blog.Payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	@NotBlank
	@Size(min = 4, max = 17, message = "Category Title must contains a min of 5 and a max of 17 characters!")
	private String categoryTitle;

	@NotBlank
	@Size(min = 10, message = "Category Description must contains at least 10  characters!")
	private String categoryDescription;

}
