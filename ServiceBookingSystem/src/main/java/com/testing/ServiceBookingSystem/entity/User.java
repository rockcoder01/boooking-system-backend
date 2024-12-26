package com.testing.ServiceBookingSystem.entity;

import com.testing.ServiceBookingSystem.dto.UserDTO;
import com.testing.ServiceBookingSystem.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String lastname;

    private String phone;

    private UserRole role;


    public UserDTO getDTO() {
        UserDTO userDto = new UserDTO();

        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setRole(role);
        return userDto;
    }

}
