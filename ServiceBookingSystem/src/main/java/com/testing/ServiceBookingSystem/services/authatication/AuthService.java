package com.testing.ServiceBookingSystem.services.authatication;

import com.testing.ServiceBookingSystem.dto.SignupRequestDTO;
import com.testing.ServiceBookingSystem.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    UserDTO signUpClient(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

    UserDTO signUpCompany(SignupRequestDTO signupRequestDTO);

    UserDetails loadUserByUsername(String email);
}
