package com.voidsow.community.backend.dto;

import com.voidsow.community.backend.entity.Chat;
import com.voidsow.community.backend.entity.User;
import lombok.Data;

@Data
public class Conversation {
    Chat last;
    User talkTo;
    int count;
    int unread;
}
