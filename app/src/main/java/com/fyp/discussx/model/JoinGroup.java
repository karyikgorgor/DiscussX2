package com.fyp.discussx.model;

/**
 * Created by IMCKY on 30-Oct-17.
 */

public class JoinGroup {
    private String membersId;
    private Long timeJoined;
    private String email;
    private String userName;
    private String groupName;

    public JoinGroup() {

    }

    public void setMembersId(String memberId) {
        this.membersId = memberId;
    }

    public JoinGroup(String membersId, Long timeJoined, String email, String userName, String groupName) {
        this.membersId = membersId;
        this.timeJoined = timeJoined;
        this.email = email;
        this.userName = userName;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMembersId() {
        return membersId;
    }


    public Long getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(Long timeJoined) {
        this.timeJoined = timeJoined;
    }
}
