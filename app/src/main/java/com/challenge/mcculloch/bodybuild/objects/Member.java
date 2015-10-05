package com.challenge.mcculloch.bodybuild.objects;

import java.util.Date;

public class Member {
    private String realName;
    private String userName;
    private String city;
    private String state;
    private String country;
    private String profilePicUrl;
    private int bodyfat;
    private int userId;
    private int height;
    private int weight;
    private String birthday;
    private Date createdAt;
    private Date updatedAt;
    private int id;

    public Member() {
    }

    public Member(String realName, String userName, String city, String state, String country, String profilePicUrl, int bodyfat, int userId, int height, int weight, String birthday, Date createdAt, Date updatedAt, int id) {
        this.realName = realName;
        this.userName = userName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.profilePicUrl = profilePicUrl;
        this.bodyfat = bodyfat;
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public int getBodyfat() {
        return bodyfat;
    }

    public void setBodyfat(int bodyfat) {
        this.bodyfat = bodyfat;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
