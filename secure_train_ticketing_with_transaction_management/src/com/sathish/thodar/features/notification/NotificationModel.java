package com.sathish.thodar.features.notification;

import java.lang.String;
import java.lang.Long;

public class NotificationModel {
    private Long userId;
    private String message;
    private Long timestampEpoch;

    public NotificationModel() {}

    public NotificationModel(Long userId, String message, Long timestampEpoch) {
        this.userId = userId;
        this.message = message;
        this.timestampEpoch = timestampEpoch;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getTimestampEpoch() { return timestampEpoch; }
    public void setTimestampEpoch(Long timestampEpoch) { this.timestampEpoch = timestampEpoch; }
}