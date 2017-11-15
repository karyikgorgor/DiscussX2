package com.fyp.discussx.model;

/**
 * Created by IMCKY on 16-Nov-17.
 */

public class CommentReport {
    private String reportId;
    private String groupId;
    private String commenterId;
    private long timeReported;
    private String reportedUser;
    private String reason;
    private String reportedComment;
    private String reporter;

    public CommentReport() {
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }


    public long getTimeReported() {
        return timeReported;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setTimeReported(long timeReported) {
        this.timeReported = timeReported;
    }

    public String getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(String reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(String reportedComment) {
        this.reportedComment = reportedComment;
    }
}
