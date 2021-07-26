package com.example.roomassignment.Model.MyRoom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.xml.namespace.QName;
@Entity(tableName = "Register_users")
public class RegisterUser {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "email")
    private String Useremail;

    @ColumnInfo(name = "User_password")
    private String password;

    @ColumnInfo(name = "User_image")
    private String image;

    @ColumnInfo(name ="User_mobile")
    private String user_mobile;

    @ColumnInfo(name = "FirstName")
    private String fisrt_name;

    @ColumnInfo(name = "SecondName")
    private String second_name;

    public void setUseremail(@NonNull String useremail) {
        Useremail = useremail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public void setFisrt_name(String fisrt_name) {
        this.fisrt_name = fisrt_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    @NonNull
    public String getUseremail() {
        return Useremail;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public String getFisrt_name() {
        return fisrt_name;
    }

    public String getSecond_name() {
        return second_name;
    }
}
