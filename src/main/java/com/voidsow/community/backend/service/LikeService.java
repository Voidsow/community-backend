package com.voidsow.community.backend.service;

import com.voidsow.community.backend.mapper.CommentMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.voidsow.community.backend.constant.Constant.LIKE_POST;

@Service
public class LikeService {
    private PostMapper postMapper;
    private CommentMapper commentMapper;
    private RedisTemplate<String, Object> redisTemplate;
    private final String LIKE = "like";
    private final String USER = "user";

    private String getKey(int type, int id) {
        StringBuilder builder = new StringBuilder();
        builder.append(LIKE);
        builder.append(':');
        builder.append(type);
        builder.append(':');
        builder.append(id);
        return builder.toString();
    }

    private String getLikedKey(int uid) {
        StringBuilder builder = new StringBuilder();
        builder.append(USER);
        builder.append(':');
        builder.append(uid);
        builder.append(':');
        builder.append(LIKE);
        return builder.toString();
    }

    @Autowired
    public LikeService(PostMapper postMapper, CommentMapper commentMapper, RedisTemplate<String, Object> redisTemplate) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.redisTemplate = redisTemplate;
    }

    public boolean likeOrNot(int type, int id, int userId) {
        String key = getKey(type, id);
        int likedUid;
        if (type == LIKE_POST)
            likedUid = postMapper.selectByPrimaryKey(id).getUid();
        else
            likedUid = commentMapper.selectByPrimaryKey(id).getUid();
        String likedKey = getLikedKey(likedUid);
        Boolean isMember = redisTemplate.opsForSet().isMember(key, userId);
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                redisTemplate.multi();
                if (isMember) {
                    redisTemplate.opsForSet().remove(key, userId);
                    redisTemplate.opsForValue().decrement(likedKey);
                } else {
                    redisTemplate.opsForSet().add(getKey(type, id), userId);
                    redisTemplate.opsForValue().increment(likedKey);
                }
                return redisTemplate.exec();
            }
        });
        return !isMember;
    }

    public long likeNum(int type, int id) {
        return redisTemplate.opsForSet().size(getKey(type, id));
    }

    public boolean like(int type, int id, int uid) {
        return redisTemplate.opsForSet().isMember(getKey(type, id), uid);
    }

    public long getLike(int uid) {
        Integer likeNum = (Integer) redisTemplate.opsForValue().get(getLikedKey(uid));
        return likeNum == null ? 0 : likeNum;
    }
}
