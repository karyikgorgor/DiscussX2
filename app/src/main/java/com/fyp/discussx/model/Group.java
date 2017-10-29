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
    private String moderator;
    private String creator;
    private long numPosts;
    private long timeCreated;


    public Group() {

    }


    public Group(User user, Post post, String groupId, String groupName, long numMembers, String membersId, String moderator, String creator, long numPosts, long timeCreated) {
        this.user = user;
        this.post = post;
        this.groupId = groupId;
        this.groupName = groupName;
        this.numMembers = numMembers;
        this.membersId = membersId;
        this.moderator = moderator;
        this.creator = creator;
        this.numPosts = numPosts;
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

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public long getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(long numPosts) {
        this.numPosts = numPosts;
    }

    public String getMembersId() {
        return membersId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }



    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

}
