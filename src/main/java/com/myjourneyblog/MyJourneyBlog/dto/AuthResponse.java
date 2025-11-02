package com.myjourneyblog.MyJourneyBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;      // JWT token
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private String message;
}
