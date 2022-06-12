package com.voidsow.community.backend.service;

import com.voidsow.community.backend.dto.PostDTO;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.mapper.CustomPostMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.mapper.UserMapper;
import com.voidsow.community.backend.utils.HostHolder;
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
    HostHolder hostHolder;

    @Autowired
    public PostService(PostMapper postMapper, CustomPostMapper customPostMapper,
                       UserMapper userMapper, LikeService likeService, HostHolder hostHolder) {
        this.postMapper = postMapper;
        this.customPostMapper = customPostMapper;
        this.userMapper = userMapper;
        this.likeService = likeService;
        this.hostHolder = hostHolder;
    }

    public Map<String, Object> encapsulatePosts(List<Post> posts, long total, int pageSize) {
        User user = hostHolder.user.get();
        List<PostDTO> postDTOS = new ArrayList<>();
        ArrayList<Object> users = new ArrayList<>();
        posts.forEach(x -> {
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(x, postDTO);
            users.add(userMapper.selectByPrimaryKey(x.getUid()));
            postDTO.setLikeNum((likeService.likeNum(LIKE_POST, x.getId())));
            postDTO.setLike(user != null && likeService.like(LIKE_POST, postDTO.getId(), user.getId()));
            postDTOS.add(postDTO);
        });
        Map<String, Object> map = new HashMap<>();
        map.put("posts", postDTOS);
        map.put("users", users);
        map.put("lastPage", Math.ceil(1.0 * total / pageSize));
        return map;
    }

    public Map<String, Object> getPosts(Integer uid, int pageNo, int pageSize) {
        PostExample postExample = new PostExample();
        if (uid != null)
            postExample.createCriteria().andUidEqualTo(uid);
        List<Post> posts = postMapper.selectByExampleWithBLOBsWithRowbounds(
                postExample, new RowBounds((pageNo - 1) * pageSize, pageSize));
        return encapsulatePosts(posts, postMapper.countByExample(postExample), pageSize);
    }

    public Post get(Integer id) {
        return postMapper.selectByPrimaryKey(id);
    }

    public void add(Post post) {
        customPostMapper.insert(post);
    }
}