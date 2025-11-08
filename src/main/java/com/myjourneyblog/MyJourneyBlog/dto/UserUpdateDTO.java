package com.myjourneyblog.MyJourneyBlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  DTO for user profile update requests
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    
    @Email(message = "Email must be valid")
    @Schema(description = "Email address", example = "john@example.com")
    private  String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password (min 6 characters)", example = "password123")
    private String password;

    @Schema(description = "Full name", example = "John Doe")
    private String fullname;

    @Schema(description = "User bio", example = "Software developer learning Spring Boot")
    private String bio;

    @Schema(description = "User profile image file",  example = "IMG file")
    private String profileImageUrl;
    
}
