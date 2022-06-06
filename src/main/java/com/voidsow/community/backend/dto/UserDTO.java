package com.voidsow.community.backend.dto;

import com.voidsow.community.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    User user;
    long followeeNum;
    long followerNum;
    boolean followed;
    long likeNum;
}
