package com.voidsow.community.backend.mapper;

import com.voidsow.community.backend.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CustomPostMapper {
    int insert(Post post);

    List<Post> selectByUid(Collection<Integer> followees, Integer offset, Integer limit);

    long countByUid(Collection<Integer> followees);
}
