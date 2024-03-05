package ru.etysoft.clientbook.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Client {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    private String name;

    @ColumnInfo(defaultValue = "")
    private String phoneNumber;

    @ColumnInfo(defaultValue = "")
    private String picturePath;

    public Client(@NonNull long id, String name, String phoneNumber, String picturePath) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.picturePath = picturePath;
    }

    @Ignore
    public Client(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
