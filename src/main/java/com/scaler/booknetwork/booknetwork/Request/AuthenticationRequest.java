package com.scaler.booknetwork.booknetwork.Request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "Email is mandatory")
    //@NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not formatted")
    private String email;
    @NotEmpty(message = "password is mandatory")
    //@NotBlank(message = "password is mandatory")
    @Size(min=8 , message = "password shoul be 8 charcter")
    private String password;
}
