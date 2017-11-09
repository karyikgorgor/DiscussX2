package com.fyp.discussx.model;

/**
 * Created by IMCKY on 29-Oct-17.
 */

public class Group {

    private User user;
    private Post post;
    private String groupId;
    private String groupName;
    private long numMembers;
    private String membersId;
    private String creatorId;
    private String creatorName;
    private long timeCreated;


    public Group() {

    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group(User user, Post post, String groupId, String groupName, long numMembers, String membersId, String creatorId, String creatorName, long timeCreated) {
        this.user = user;
        this.post = post;
        this.groupId = groupId;
        this.groupName = groupName;
        this.numMembers = numMembers;
        this.membersId = membersId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.timeCreated = timeCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getGroupId() {
        return groupId;
    }



    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(long numMembers) {
        this.numMembers = numMembers;
    }


    public String getMembersId() {
        return membersId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }


    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

}
