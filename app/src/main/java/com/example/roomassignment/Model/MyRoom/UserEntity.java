package com.example.roomassignment.Model.MyRoom;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    private String name;

    @ColumnInfo(name="userdesc")
    private String desc;

    @ColumnInfo(name = "userimage")
    private String user_image;

    @ColumnInfo(name="Owner_email")
    private String owner_email;

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUser_image() {
        return user_image;
    }
}
