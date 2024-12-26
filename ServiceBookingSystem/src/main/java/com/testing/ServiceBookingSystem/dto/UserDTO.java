package com.testing.ServiceBookingSystem.dto;

import com.testing.ServiceBookingSystem.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastname;

    private String phone;

    private UserRole role;

}
