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


    @Query("SELECT * FROM users WHERE id = :id AND Owner_email = :owner_email")
    int isDataExist(int id,String owner_email);

    @Query("select * from users WHERE Owner_email = :owner_email")
    List<UserEntity> getUser(String owner_email);

    @Delete
    public void deleteUser(UserEntity userEntity);

    @Update
    public void UpdateUser(UserEntity user);


//***************************New Owner Table*************************************
    @Query("SELECT * FROM Register_users WHERE email = :eml")
    int isOwnerExist(String eml);

    @Insert
    public void insertRegister(RegisterUser registerUser);

    @Query("SELECT * FROM Register_users WHERE email = :eml")
    public RegisterUser getOwnerUser(String eml);


    @Query("SELECT * FROM Register_users WHERE email = :eml AND User_password = :pas")
    int isExistUser(String eml,String pas);

    @Update
    public void UpdateOwnerUser(RegisterUser user);

}
