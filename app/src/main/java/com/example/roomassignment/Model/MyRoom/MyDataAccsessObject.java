package com.example.roomassignment.Model.MyRoom;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDataAccsessObject{

    @Insert
    public void insert(UserEntity userEntity);

    @Query("select * from users WHERE Owner_email = :owner_email AND id= :mid")
    UserEntity getUser(String owner_email,int mid);


    @Query("SELECT * FROM users WHERE id = :id AND Owner_email = :owner_email")
    int isDataExist(int id,String owner_email);


    @Query("select * from users WHERE Owner_email = :owner_email")
    List<UserEntity> getUser(String owner_email);

    @Delete
    public void deleteUser(UserEntity userEntity);

    @Update
    public void UpdateUser(UserEntity user);

    @Query("UPDATE users SET atime = :t WHERE Owner_email =:mail")
    public void UpdateOwnerUser(Boolean t,String mail);


//***************************New Owner Table*************************************
    @Query("SELECT * FROM Register_users WHERE email = :eml")
    int isOwnerExist(String eml);

    @Insert
    public void insertRegister(RegisterUser registerUser);

    @Query("SELECT * FROM Register_users WHERE email = :eml")
    public RegisterUser getOwnerUser(String eml);


    @Query("SELECT * FROM Register_users WHERE email = :eml AND User_password = :pas")
    int isExistUser(String eml,String pas);

    @Query("UPDATE Register_users SET User_password = :pass,User_password = :pass,User_image = :image,User_mobile = :mob,FirstName = :fname,SecondName =:sname  WHERE email =:mail")
    public void UpdateOwnerUser(String mail,String fname,String sname,String image,String mob,String pass);

 //***************************** Alarm Time Table **********************************

    @Insert
    void insertAll(AlarmTimeEntity entity);


    @Query("SELECT * FROM AlarmTable WHERE RemenderId = :id")
    public AlarmTimeEntity getAlarmItem(int id);


    @Delete
    public void deleteAlarmTime(AlarmTimeEntity userEntity);
    
}
