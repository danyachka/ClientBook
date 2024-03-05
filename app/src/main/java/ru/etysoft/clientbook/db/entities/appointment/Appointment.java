package ru.etysoft.clientbook.db.entities.appointment;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private long clientId;

    private long startTime;

    private long endTime;

    @ColumnInfo(defaultValue = "ENABLED")
    private NotificationStatus notificationStatus = NotificationStatus.ENABLED;

    @ColumnInfo(defaultValue = "0")
    private int value;

    @ColumnInfo(defaultValue = "")
    private String text;

    public Appointment(@NonNull long id, long clientId, long startTime, long endTime, NotificationStatus notificationStatus) {
        this.id = id;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationStatus = notificationStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
