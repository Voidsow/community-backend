package com.voidsow.community.backend.service;

import com.voidsow.community.backend.entity.Comment;
import com.voidsow.community.backend.entity.CommentExample;
import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.mapper.CommentMapper;
import com.voidsow.community.backend.mapper.CustomCommentMapper;
import com.voidsow.community.backend.mapper.PostMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

import static com.voidsow.community.backend.constant.Constant.POST_LEVEL_ONE;

@Service
public class CommentService {
    CommentMapper commentMapper;
    CustomCommentMapper customCommentMapper;
    PostMapper postMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper, CustomCommentMapper customCommentMapper, PostMapper postMapper) {
        this.commentMapper = commentMapper;
        this.customCommentMapper = customCommentMapper;
        this.postMapper = postMapper;
    }

    public List<Comment> find(int type, int replyTo, int offset, int limit) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(type).andReplyToEqualTo(replyTo);
        return commentMapper.selectByExampleWithBLOBsWithRowbounds(commentExample,
                new RowBounds(offset, limit));
    }

    public long getCount(int type, int replyTo) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(type).andReplyToEqualTo(replyTo);
        return commentMapper.countByExample(commentExample);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int add(Comment comment, int postId) {
        Date curTime = new Date();
        comment.setGmtCreate(curTime);
        comment.setGmtModified(curTime);
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //返回主键
        int id = customCommentMapper.insert(comment);
        Post newValPost = new Post();
        newValPost.setId(postId);
        //修改最后回帖时间
        newValPost.setGmtModified(new Date());
        //若为一级评论则更新Post的commentNum字段
        if (comment.getType() == POST_LEVEL_ONE)
            newValPost.setCommentNum(postMapper.selectByPrimaryKey(
                    comment.getReplyTo()).getCommentNum() + 1);
        postMapper.updateByPrimaryKeySelective(newValPost);
        return id;
    }
}