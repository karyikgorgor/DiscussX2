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
    private String postTitle;
    private String postDesc;
    private String postImageUrl;
    private String postId;
    private long numLikes;
    private long numDownvotes;
    private long numComments;
    private long replyToComments;
    private long timeCreated;
    private Group group;

    public Post() {
    }


    public Post(User user, String postTitle, String postDesc, String postImageUrl, String postId, long numLikes, long numDownvotes, long numComments, long replyToComments, long timeCreated, Group group) {
        this.user = user;
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.numLikes = numLikes;
        this.numDownvotes = numDownvotes;
        this.numComments = numComments;
        this.replyToComments = replyToComments;
        this.timeCreated = timeCreated;
        this.group = group;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public long getReplyToComments() {
        return replyToComments;
    }

    public void setReplyToComments(long replyToComments) {
        this.replyToComments = replyToComments;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
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
