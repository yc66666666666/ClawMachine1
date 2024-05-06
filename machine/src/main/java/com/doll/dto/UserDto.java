package com.doll.dto;

import com.doll.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}
