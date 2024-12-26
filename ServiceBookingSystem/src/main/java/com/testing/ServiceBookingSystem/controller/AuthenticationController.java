package com.testing.ServiceBookingSystem.controller;

import com.testing.ServiceBookingSystem.dto.SignupRequestDTO;
import com.testing.ServiceBookingSystem.dto.UserDTO;
import com.testing.ServiceBookingSystem.services.authatication.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

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

        UserDTO createUser = authService.signUpCompany  (signupRequestDTO);
        return new ResponseEntity<>(createUser, HttpStatus.OK);
    }

}
