package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.CustomerType;
import com.ecommerce.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private Role role;
    private CustomerType customerType;
}
