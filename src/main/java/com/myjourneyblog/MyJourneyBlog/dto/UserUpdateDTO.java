package com.myjourneyblog.MyJourneyBlog.dto;

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
    private  String email;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private String fullname;

    private String bio;

    private String profileImageUrl;
    
}
