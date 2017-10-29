package com.fyp.discussx.model;

/**
 * Created by IMCKY on 26-Oct-17.
 */
import java.io.Serializable;

/**
 * Created by brad on 2017/02/05.
 */

public class Comment implements Serializable {
    private User user;
    private String userName;
    private String commentId;
    private long timeCreated;
    private long numCommentLikes;
    private long numCommentDownvotes;
    private String replyToComment;
    private String comment;
    private Group group;

    public Comment() {
    }


    public Comment(User user, String userName, String commentId, long timeCreated, long numCommentLikes, long numCommentDownvotes, String replyToComment, String comment, Group group) {
        this.user = user;
        this.userName = userName;
        this.commentId = commentId;
        this.timeCreated = timeCreated;
        this.numCommentLikes = numCommentLikes;
        this.numCommentDownvotes = numCommentDownvotes;
        this.replyToComment = replyToComment;
        this.comment = comment;
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getNumCommentLikes() {
        return numCommentLikes;
    }

    public void setNumCommentLikes(long numCommentLikes) {
        this.numCommentLikes = numCommentLikes;
    }

    public long getNumCommentDownvotes() {
        return numCommentDownvotes;
    }

    public void setNumCommentDownvotes(long numCommentDownvotes) {
        this.numCommentDownvotes = numCommentDownvotes;
    }

    public String getReplyToComment() {
        return replyToComment;
    }

    public void setReplyToComment(String replyToComment) {
        this.replyToComment = replyToComment;
    }
}