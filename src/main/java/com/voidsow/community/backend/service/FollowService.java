package com.voidsow.community.backend.service;

import com.voidsow.community.backend.entity.User;
import com.voidsow.community.backend.mapper.UserMapper;
import com.voidsow.community.backend.utils.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.voidsow.community.backend.utils.RedisKey.FOLLOWEE;
import static com.voidsow.community.backend.utils.RedisKey.FOLLOWER;

@Service
public class FollowService {
    UserMapper userMapper;
    RedisTemplate<String, Integer> redisTemplate;


    @Autowired
    public FollowService(UserMapper userMapper, RedisTemplate<String, Integer> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    //将关注状态取反，并返回取反后的状态
    public boolean followOrNot(int userId, int followeeId) {
        String followeeKey = RedisKey.getKey(userId, FOLLOWEE);
        String followerKey = RedisKey.getKey(followeeId, FOLLOWER);
        boolean isFollowed = isFollower(followeeId, userId);
        //实际上不存在竞争关系，Redis指令是原子性的
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Date now = new Date();
                if (isFollowed) {
                    operations.opsForZSet().remove(followeeKey, followeeId);
                    operations.opsForZSet().remove(followerKey, userId);
                } else {
                    operations.opsForZSet().add(followeeKey, followeeId, now.getTime());
                    operations.opsForZSet().add(followerKey, userId, now.getTime());
                }
                return redisTemplate.exec();
            }
        });
        return !isFollowed;
    }

    public boolean isFollower(int followee, int follower) {
        return redisTemplate.opsForZSet().score(RedisKey.getKey(followee, FOLLOWER), follower) != null;
    }

    public Long countFollower(int uid) {
        return redisTemplate.opsForZSet().zCard(RedisKey.getKey(uid, FOLLOWER));
    }

    public Long countFollowee(int uid) {
        return redisTemplate.opsForZSet().zCard(RedisKey.getKey(uid, FOLLOWEE));
    }

    private List<User> findFollows(int uid, String type, int offset, int limit) {
        Set<Integer> followers = redisTemplate.opsForZSet().reverseRange(RedisKey.getKey(uid, type), offset, offset + limit - 1);
        if (followers == null)
            return null;
        List<User> users = new ArrayList<>();
        followers.forEach(follower -> users.add(userMapper.selectByPrimaryKey(follower)));
        return users;
    }

    public List<User> findFollowers(int uid, int offset, int limit) {
        return findFollows(uid, FOLLOWER, offset, limit);
    }

    public List<User> findFollowees(int uid, int offset, int limit) {
        return findFollows(uid, FOLLOWEE, offset, limit);
    }
}