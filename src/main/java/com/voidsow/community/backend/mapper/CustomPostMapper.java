package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomPostMapper {
    int insert(Post post);
}
