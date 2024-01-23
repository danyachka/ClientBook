package ru.etysoft.clientbook.db.entities.appointment;

public class Appointment {

    private String id;

    private String clientId;

    private long startTime;

    private long endTime;

    private NotificationStatus notificationStatus = NotificationStatus.ENABLED;

    public Appointment(String id, String clientId, long startTime, long endTime, NotificationStatus notificationStatus) {
        this.id = id;
        this.clientId = clientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notificationStatus = notificationStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
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

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
