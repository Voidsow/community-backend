package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomCommentMapper {
    //返回主键
    int insert(Comment comment);
}
