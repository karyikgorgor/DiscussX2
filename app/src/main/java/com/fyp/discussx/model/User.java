package com.fyp.discussx.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brad on 2017/02/05.
 */

public class User implements Serializable {
    private String user;
    private String email;
    private String photoUrl;
    private String uid;
    private Group group;
    private String gender;
    private String dob;

    public User() {
    }

    public User(String user, String email, String photoUrl, String uid, Group group, String gender, String dob) {
        this.user = user;
        this.email = email;
        this.photoUrl = photoUrl;
        this.uid = uid;
        this.group = group;
        this.gender = gender;
        this.dob = dob;
    }

    public User(String gender, String dob) {
        this.gender = gender;
        this.dob = dob;
    }

    public User (String x) {
        this.gender = x;
        this.dob = x;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}