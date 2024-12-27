package com.testing.ServiceBookingSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.testing.ServiceBookingSystem.config.JWTConfig;
import com.testing.ServiceBookingSystem.dto.AuthResponse;
import com.testing.ServiceBookingSystem.dto.AuthenticationRequest;
import com.testing.ServiceBookingSystem.dto.SignupRequestDTO;
import com.testing.ServiceBookingSystem.dto.UserDTO;
import com.testing.ServiceBookingSystem.entity.User;
import com.testing.ServiceBookingSystem.repository.UserRepository;
import com.testing.ServiceBookingSystem.services.authatication.AuthService;
import com.testing.ServiceBookingSystem.utility.JwtTokenUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signUpClient(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity("client already Exist with this email", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createUser = authService.signUpClient(signupRequestDTO);
        return new ResponseEntity<>(createUser, HttpStatus.OK);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signUpCompany(@RequestBody SignupRequestDTO signupRequestDTO) {
        if (authService.presentByEmail(signupRequestDTO.getEmail())) {
            return new ResponseEntity("company already Exist with this email", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO createUser = authService.signUpCompany(signupRequestDTO);
        return new ResponseEntity<>(createUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadRequestException, IOException, BadRequestException {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsename(), authenticationRequest.getPassword()
//            ));
//        } catch (BadCredentialsException badRequestException) {
//            throw new BadRequestException("incorrect username or password", badRequestException);
//        }
//
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsename());
//        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());
//        Optional<User> user = userRepository.findByEmail(authenticationRequest.getUsename());
//
//        Map<String, Object> jsonResponse = new HashMap<>();
//        jsonResponse.put("userId", user.get().getId());
//        jsonResponse.put("role", user.get().getRole());
//        jsonResponse.put("token", jwt);
//
//        // Convert the Map to JSON and write it to the response
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
//        response.addHeader(HEADER_STRING, TOKEN);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());
            // If authentication is successful, you can return a token or user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Extract user roles and convert to List
            List<String> roles = new ArrayList<>(userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet()));

             AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setRole(roles.get(0));

            authResponse.setId(userDetails.getUsername());
            // Generate and return JWT token or other response here
            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password" + e.getMessage());
        }
    }

}
