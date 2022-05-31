package com.voidsow.community.backend.dto;

import com.voidsow.community.backend.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentDTO {
    int id;
    String content;
    Date gmtCreate;
    long likeNum;
    boolean like;
    int commentNum;
    User user;
    List<CommentDTO> subComments;
    User target;
}
