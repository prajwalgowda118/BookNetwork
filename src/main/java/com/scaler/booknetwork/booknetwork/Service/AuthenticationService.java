package com.scaler.booknetwork.booknetwork.Service;
import com.scaler.booknetwork.booknetwork.Models.EmailTemplateName;
import com.scaler.booknetwork.booknetwork.Models.Token;
import com.scaler.booknetwork.booknetwork.Models.User;
import com.scaler.booknetwork.booknetwork.Repository.RoleRepository;
import com.scaler.booknetwork.booknetwork.Repository.TokenRepository;
import com.scaler.booknetwork.booknetwork.Repository.UserRepository;
import com.scaler.booknetwork.booknetwork.Request.AuthenticationRequest;
import com.scaler.booknetwork.booknetwork.Request.AuthenticationResponse;
import com.scaler.booknetwork.booknetwork.Request.RegistrationRequest;
import com.scaler.booknetwork.booknetwork.SecurityConfig.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;


    private final String activationUrl="http://localhost:8080/activate-account";


    public void CreateUser(RegistrationRequest request) throws MessagingException {

        var userRole=roleRepository.findByName("USER")
                    .orElseThrow(()-> new IllegalStateException("Role not initialized"));

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .accountNonExpired(false)
                .accountNonLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) throws MessagingException {

        var newToken=generateAndSaveActivationToken(user);
        //Send Email
        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"

        );

    }

    private String generateAndSaveActivationToken(User user) {

        String generatedToken=generateActivationCode(6);

        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiryAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int i) {
        String Characters="0123456789";
        StringBuilder sb=new StringBuilder();
        SecureRandom random=new SecureRandom();
        for(int j=0;j<i;j++){
           sb.append(Characters.charAt(random.nextInt(Characters.length())));
        }
        return sb.toString();
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest authenticationRequest) throws MessagingException {

       var authentication=authenticationManager
                            .authenticate(
                                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                            authenticationRequest.getPassword())
                            );

        var claims=new HashMap<String, Object>();
        var user = (User) authentication.getPrincipal();
        claims.put("email", user.getEmail());
        claims.put("username", user.fullName());
        var jwtToken=jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {

        Token savedToken= (Token) tokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("token is invalid"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiryAt()))
        {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation account has been sent");

        }
        var user=userRepository.findByEmail(savedToken.getUser().getEmail())
                .orElseThrow(()->new RuntimeException("user not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }
}
