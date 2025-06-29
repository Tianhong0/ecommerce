package com.example.admin.dto;


import com.example.admin.entity.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserInfoDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<String> permissions;


    public static UserInfoDTO fromUser(User user, List<String> permissions) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setStatus(user.getStatus());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        dto.setPermissions(permissions);
        dto.setAvatar(user.getAvatar());

        return dto;
    }
}
