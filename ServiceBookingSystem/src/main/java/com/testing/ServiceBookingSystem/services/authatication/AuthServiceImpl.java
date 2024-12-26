package com.testing.ServiceBookingSystem.services.authatication;

import com.testing.ServiceBookingSystem.dto.SignupRequestDTO;
import com.testing.ServiceBookingSystem.dto.UserDTO;
import com.testing.ServiceBookingSystem.entity.User;
import com.testing.ServiceBookingSystem.enums.UserRole;
import com.testing.ServiceBookingSystem.repository.UserRepository;
import com.testing.ServiceBookingSystem.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    public UserDTO signUpClient(SignupRequestDTO signupRequestDTO){
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setLastname(signupRequestDTO.getLastname());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.CLIENT);

        return userRepository.save(user).getDTO();

    }

    public Boolean presentByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDTO signUpCompany(SignupRequestDTO signupRequestDTO){
        User user = new User();
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.COMPANY);

        return userRepository.save(user).getDTO();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create UserDetails object and return
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                JwtTokenUtil.getAuthorities(user)
        );
    }

}
