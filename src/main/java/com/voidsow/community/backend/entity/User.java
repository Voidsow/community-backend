package com.voidsow.community.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties({"password", "salt", "type", "status", "email", "activationCode"})
public class User {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.username
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String username;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.salt
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String salt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.type
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.email
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String email;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.status
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.activation_code
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String activationCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.header_url
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private String headerUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_create
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_modified
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    private Date gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public User(Integer id, String username, String password, String salt, Integer type, String email, Integer status, String activationCode, String headerUrl, Date gmtCreate, Date gmtModified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.type = type;
        this.email = email;
        this.status = status;
        this.activationCode = activationCode;
        this.headerUrl = headerUrl;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public User() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.username
     *
     * @return the value of user.username
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.username
     *
     * @param username the value for user.username
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.salt
     *
     * @return the value of user.salt
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getSalt() {
        return salt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.salt
     *
     * @param salt the value for user.salt
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.type
     *
     * @return the value of user.type
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.type
     *
     * @param type the value for user.type
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.email
     *
     * @return the value of user.email
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.email
     *
     * @param email the value for user.email
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.status
     *
     * @return the value of user.status
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.status
     *
     * @param status the value for user.status
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.activation_code
     *
     * @return the value of user.activation_code
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getActivationCode() {
        return activationCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.activation_code
     *
     * @param activationCode the value for user.activation_code
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode == null ? null : activationCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.header_url
     *
     * @return the value of user.header_url
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public String getHeaderUrl() {
        return headerUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.header_url
     *
     * @param headerUrl the value for user.header_url
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl == null ? null : headerUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_create
     *
     * @return the value of user.gmt_create
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_create
     *
     * @param gmtCreate the value for user.gmt_create
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_modified
     *
     * @return the value of user.gmt_modified
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_modified
     *
     * @param gmtModified the value for user.gmt_modified
     * @mbg.generated Tue Apr 26 20:33:20 CST 2022
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}