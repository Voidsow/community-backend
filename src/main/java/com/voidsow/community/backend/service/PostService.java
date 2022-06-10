package com.voidsow.community.backend.service;

import com.voidsow.community.backend.dto.PostDTO;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.mapper.CustomPostMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voidsow.community.backend.constant.Constant.LIKE_POST;

@Service
public class PostService {
    PostMapper postMapper;
    CustomPostMapper customPostMapper;
    UserMapper userMapper;
    LikeService likeService;

    @Autowired
    public PostService(PostMapper postMapper, CustomPostMapper customPostMapper, UserMapper userMapper, LikeService likeService) {
        this.postMapper = postMapper;
        this.customPostMapper = customPostMapper;
        this.userMapper = userMapper;
        this.likeService = likeService;
    }

    public Map<String, Object> getPosts(Integer uid, int pageNo, int pageSize) {
        PostExample postExample = new PostExample();
        if (uid != null)
            postExample.createCriteria().andUidEqualTo(uid);
        Map<String, Object> map = new HashMap<>();
        List<PostDTO> postDTOS = new ArrayList<>();
        List<Post> posts = postMapper.selectByExampleWithBLOBsWithRowbounds(
                postExample, new RowBounds((pageNo - 1) * pageSize, pageSize));
        ArrayList<Object> users = new ArrayList<>();
        posts.forEach(x -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(x, postDTO);
            users.add(userMapper.selectByPrimaryKey(x.getUid()));
            postDTO.setLikeNum((likeService.likeNum(LIKE_POST, x.getId())));
            postDTO.setLike(uid != null && likeService.like(LIKE_POST, postDTO.getId(), uid));
            postDTOS.add(postDTO);
        });
        map.put("posts", postDTOS);
        map.put("users", users);
        map.put("lastPage", Math.ceil(1.0 * postMapper.countByExample(postExample) / pageSize));
        return map;
    }

    public long getCount(Integer uid) {
        PostExample postExample = new PostExample();
        if (uid != null)
            postExample.createCriteria().andUidEqualTo(uid);
        return postMapper.countByExample(postExample);
    }

    public Post get(Integer id) {
        return postMapper.selectByPrimaryKey(id);
    }

    public void add(Post post) {
        customPostMapper.insert(post);
    }
}