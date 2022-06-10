package com.voidsow.community.backend.dto.notification;

import com.voidsow.community.backend.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class CommentNotifyDTO {
    int type;
    User user;
    Date time;
    int postId;
    String reply;
    String replied;
}
