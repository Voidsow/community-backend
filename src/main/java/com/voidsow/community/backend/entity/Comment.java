package com.voidsow.community.backend.entity;

import java.util.Date;

public class Comment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.id
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer uid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.type
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.reply_to
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer replyTo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.reply_to_uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer replyToUid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.status
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Integer status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_create
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Date gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.gmt_modified
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private Date gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comment.content
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    private String content;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table comment
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Comment(Integer id, Integer uid, Integer type, Integer replyTo, Integer replyToUid, Integer status, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.uid = uid;
        this.type = type;
        this.replyTo = replyTo;
        this.replyToUid = replyToUid;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table comment
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Comment(Integer id, Integer uid, Integer type, Integer replyTo, Integer replyToUid, Integer status, Date gmtCreate, Date gmtModified, String content) {
        this.id = id;
        this.uid = uid;
        this.type = type;
        this.replyTo = replyTo;
        this.replyToUid = replyToUid;
        this.status = status;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.content = content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table comment
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Comment() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.id
     *
     * @return the value of comment.id
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.id
     *
     * @param id the value for comment.id
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.uid
     *
     * @return the value of comment.uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.uid
     *
     * @param uid the value for comment.uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.type
     *
     * @return the value of comment.type
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.type
     *
     * @param type the value for comment.type
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.reply_to
     *
     * @return the value of comment.reply_to
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getReplyTo() {
        return replyTo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.reply_to
     *
     * @param replyTo the value for comment.reply_to
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.reply_to_uid
     *
     * @return the value of comment.reply_to_uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getReplyToUid() {
        return replyToUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.reply_to_uid
     *
     * @param replyToUid the value for comment.reply_to_uid
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setReplyToUid(Integer replyToUid) {
        this.replyToUid = replyToUid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.status
     *
     * @return the value of comment.status
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.status
     *
     * @param status the value for comment.status
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.gmt_create
     *
     * @return the value of comment.gmt_create
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.gmt_create
     *
     * @param gmtCreate the value for comment.gmt_create
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.gmt_modified
     *
     * @return the value of comment.gmt_modified
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.gmt_modified
     *
     * @param gmtModified the value for comment.gmt_modified
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comment.content
     *
     * @return the value of comment.content
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comment.content
     *
     * @param content the value for comment.content
     *
     * @mbg.generated Tue Apr 26 22:18:37 CST 2022
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}