package com.example.roomassignment.Model.MyRoom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "AlarmTable")
public class AlarmTimeEntity {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "RemenderId")
    String RemenderId;

    @ColumnInfo(name = "eventdate")
    String eventdate;
    @ColumnInfo(name = "eventtime")
    String eventtime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemenderId() {
        return RemenderId;
    }

    public void setRemenderId(String remenderId) {
        RemenderId = remenderId;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }
}
