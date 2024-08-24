package ru.etysoft.clientbook.db.entities.appointment;

import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id = 0;

    private long clientId;

    private long startTime;

    private long endTime;

    @ColumnInfo(defaultValue = "ENABLED")
    private NotificationStatus notificationStatus = NotificationStatus.ENABLED;

    @ColumnInfo(defaultValue = "0")
    private int value;

    @ColumnInfo(defaultValue = "")
    private String text;

    public Appointment(@NonNull long id, String text, int value, long clientId, long startTime, long endTime, NotificationStatus notificationStatus) {
        this.id = id;
        this.text = text;
        this.value = value;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationStatus = notificationStatus;
    }

    @Ignore
    public Appointment(String text, int value, long clientId, long startTime, long endTime, NotificationStatus notificationStatus) {
        this.text = text;
        this.value = value;
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

    public boolean isSameDay(Appointment appointment) {
        // TODO: fill methods
        return true;
    }

    public String getDateText() {
        return "Сегодня";
    }

    public String getTimeText() {
        return "14:00";
    }

    @NonNull
    @Override
    public  String toString() {
        return new StringBuilder("Appointment(")
                .append("id: ").append(id)
                .append(", text: ").append(text)
                .append(", value: ").append(value)
                .append(", clientId: ").append(clientId)
                .append(", startTime: ").append(startTime)
                .append(", endTime: ").append(endTime).toString();
    }
}
