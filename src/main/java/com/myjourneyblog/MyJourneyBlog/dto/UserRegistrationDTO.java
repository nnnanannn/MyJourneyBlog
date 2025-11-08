package com.myjourneyblog.MyJourneyBlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 *  DTO for user registration requests
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User registration request")
public class UserRegistrationDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique username", example = "johndoe", required = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address", example = "john@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password (min 6 characters)", example = "password123", required = true)
    private String password;

    @Schema(description = "Full name", example = "John Doe")
    private String fullname;

    @Schema(description = "User bio", example = "Software developer learning Spring Boot")
    private String bio;
}
