package com.sathish.thodar.features.support;

import java.lang.String;
import java.lang.Long;
import java.lang.Boolean;

public class SupportModel {
    private Long queryId;
    private Long userId;
    private String issueDescription;
    private String adminReply;
    private Boolean isResolved;

    public SupportModel() {}

    public SupportModel(Long queryId, Long userId, String issueDescription) {
        this.queryId = queryId;
        this.userId = userId;
        this.issueDescription = issueDescription;
        this.isResolved = false; // Always false when newly created
    }

    public Long getQueryId() { return queryId; }
    public void setQueryId(Long queryId) { this.queryId = queryId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public String getAdminReply() { return adminReply; }
    public void setAdminReply(String adminReply) { 
        this.adminReply = adminReply; 
        this.isResolved = true; // Auto-resolve when admin replies
    }

    public Boolean getIsResolved() { return isResolved; }
    public void setIsResolved(Boolean isResolved) { this.isResolved = isResolved; }
}