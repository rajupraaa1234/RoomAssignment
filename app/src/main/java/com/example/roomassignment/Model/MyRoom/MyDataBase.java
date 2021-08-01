package com.example.roomassignment.Model.MyRoom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class, RegisterUser.class,AlarmTimeEntity.class},version = 4,exportSchema = true)
public abstract class MyDataBase extends RoomDatabase {
    public abstract MyDataAccsessObject myDataAccsessObject();
}
