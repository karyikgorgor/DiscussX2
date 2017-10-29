package com.fyp.discussx.model;

/**
 * Created by IMCKY on 30-Oct-17.
 */

public class JoinGroup {
    private String membersId;
    private Long timeJoined;

    public JoinGroup() {
    }

    public JoinGroup(String membersId, Long timeJoined) {
        this.membersId = membersId;
        this.timeJoined = timeJoined;
    }

    public String getMembersId() {
        return membersId;
    }

    public void setMembersId(String memberId) {
        this.membersId = memberId;
    }

    public Long getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(Long timeJoined) {
        this.timeJoined = timeJoined;
    }
}
