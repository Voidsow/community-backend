package com.voidsow.community.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@JsonIgnoreProperties(value = {"uid", "type", "status", "score"}, allowSetters = true)
@Document(indexName = "post")
public class Post {
    @Id
    private Integer id;
    @Field(type = FieldType.Integer)
    private Integer uid;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.Integer)
    private Integer type;
    @Field(type = FieldType.Integer)
    private Integer status;
    @Field(type = FieldType.Date)
    private Date gmtCreate;
    @Field(type = FieldType.Date)
    private Date gmtModified;
    @Field(type = FieldType.Integer)
    private Integer commentNum;
    @Field(type = FieldType.Double)
    private Double score;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    public Post(Integer id, Integer uid, String title, Integer type, Integer status, Date gmtCreate, Date gmtModified, Integer commentNum, Double score) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.type = type;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.commentNum = commentNum;
        this.score = score;
    }

    public Post(Integer id, Integer uid, String title, Integer type, Integer status, Date gmtCreate, Date gmtModified, Integer commentNum, Double score, String content) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.type = type;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.commentNum = commentNum;
        this.score = score;
        this.content = content;
    }

    public Post() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}