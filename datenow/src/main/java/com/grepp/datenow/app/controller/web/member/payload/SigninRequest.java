package com.grepp.datenow.app.controller.web.member.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String userId;
    
    @NotBlank
    @Size(min = 8, max = 10)
    private String password;


}
