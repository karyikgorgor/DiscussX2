package com.fyp.discussx.model;

/**
 * Created by IMCKY on 26-Oct-17.
 */

import java.io.Serializable;

/**
 * Created by brad on 2017/02/05.
 */

public class Post implements Serializable {
    private User user;
    private String postText;
    private String postImageUrl;
    private String postId;
    private long numLikes;
    private long numDownvotes;
    private long numComments;
    private long timeCreated;

    public Post() {
    }

    public Post(User user, String postText, String postImageUrl, String postId, long numLikes, long numDownvotes, long numComments, long timeCreated) {

        this.user = user;
        this.postText = postText;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.numLikes = numLikes;
        this.numDownvotes = numDownvotes;
        this.numComments = numComments;
        this.timeCreated = timeCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getNumDownvotes() {
        return numDownvotes;
    }

    public void setNumDownvotes(long numDownvotes) {
        this.numDownvotes = numDownvotes;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public long getNumComments() {
        return numComments;
    }

    public void setNumComments(long numComments) {
        this.numComments = numComments;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
