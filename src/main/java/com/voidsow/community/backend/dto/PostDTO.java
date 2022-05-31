package com.voidsow.community.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PostDTO {
    int id;
    String title;
    String content;
    Date gmtCreate;
    Date gmtModified;
    int commentNum;
    boolean like;
    long likeNum;
}