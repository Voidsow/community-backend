package com.voidsow.community.backend.dto;

import lombok.Data;

@Data
public class CommentPOST {
    int pid;
    String content;
    Integer replyTo;
    Integer atCommentId;
    Integer replyToUid;
}
