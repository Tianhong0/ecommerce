package com.example.admin.dto;

import com.example.admin.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends User {
    private List<Long> roleIds;
} 