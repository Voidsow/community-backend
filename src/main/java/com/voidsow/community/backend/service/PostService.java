package com.voidsow.community.backend.service;

import com.voidsow.community.backend.dto.PostDTO;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.mapper.CustomPostMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.mapper.UserMapper;
import com.voidsow.community.backend.utils.HostHolder;
import com.voidsow.community.backend.utils.RedisKey;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.voidsow.community.backend.constant.Constant.*;

@Service
public class PostService {
    PostMapper postMapper;
    CustomPostMapper customPostMapper;
    UserMapper userMapper;
    LikeService likeService;
    HostHolder hostHolder;
    RedisTemplate<String, Integer> intRedisTemplate;

    @Autowired
    public PostService(PostMapper postMapper, CustomPostMapper customPostMapper, UserMapper userMapper,
                       LikeService likeService, HostHolder hostHolder, RedisTemplate<String, Integer> intRedisTemplate) {
        this.postMapper = postMapper;
        this.customPostMapper = customPostMapper;
        this.userMapper = userMapper;
        this.likeService = likeService;
        this.hostHolder = hostHolder;
        this.intRedisTemplate = intRedisTemplate;
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

    public Map<String, Object> getPosts(Integer uid, int pageNo, int pageSize, int order) {
        PostExample postExample = new PostExample();
        List<Post> posts;
        long count;
        if (order == ORDER_BY_FOLLOW) {
            Set<Integer> range = intRedisTemplate.opsForZSet().range(RedisKey.getKey(uid, RedisKey.FOLLOWEE), 0, -1);
            if (range.isEmpty()) {
                posts = new ArrayList<>();
                count = 0;
            } else {
                posts = customPostMapper.selectByUid(range, (pageNo - 1) * pageSize, pageSize);
                count = customPostMapper.countByUid(range);
            }
        } else {
            postExample.setOrderByClause(order == ORDER_BY_NEWEST ? "gmt_create desc" : "score desc");
            posts = postMapper.selectByExampleWithBLOBsWithRowbounds(postExample, new RowBounds((pageNo - 1) * pageSize, pageSize));
            count = postMapper.countByExample(postExample);
        }
        return encapsulatePosts(posts, count, pageSize);
    }

    public List<Post> getPostsByUid(int uid) {
        PostExample postExample = new PostExample();
        postExample.createCriteria().andUidEqualTo(uid);
        postExample.setOrderByClause("gmt_create desc");
        return postMapper.selectByExampleWithBLOBs(postExample);
    }

    public Post get(Integer id) {
        return postMapper.selectByPrimaryKey(id);
    }

    public void add(Post post) {
        customPostMapper.insert(post);
    }
}