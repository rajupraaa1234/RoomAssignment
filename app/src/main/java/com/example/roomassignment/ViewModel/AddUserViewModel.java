package com.example.roomassignment.ViewModel;

import android.content.Context;

import androidx.room.Room;

import com.example.roomassignment.Model.MyRoom.MyDataBase;
import com.example.roomassignment.Model.MyRoom.UserEntity;
import com.example.roomassignment.Model.Session.Sessionmanager;

import java.util.List;

public class AddUserViewModel {
    MyDataBase myDataBase;
    Sessionmanager sessionmanager;
    String owner_email;
    public AddUserViewModel(Context context){
        myDataBase= Room.databaseBuilder(context,MyDataBase.class,"userdb").allowMainThreadQueries().build();
        sessionmanager=new Sessionmanager(context);
        owner_email=sessionmanager.getEmail();
    }
    public boolean insertdata(UserEntity userEntity){
        int id=userEntity.getId();
        int res=myDataBase.myDataAccsessObject().isDataExist(id,owner_email);
        if(res==0){
            myDataBase.myDataAccsessObject().insert(userEntity);
            return true;
        }else{
            return false;
        }
    }

    public List<UserEntity> getUserData(){
        List<UserEntity> res=myDataBase.myDataAccsessObject().getUser(owner_email);
        return res;
    }

    public boolean deleteUser(UserEntity userEntity){
        int ispresent=myDataBase.myDataAccsessObject().isDataExist(userEntity.getId(),owner_email);
        if(ispresent==0){
            return false;
        }else{
            myDataBase.myDataAccsessObject().deleteUser(userEntity);
            return true;
        }
    }

    public void updateUser(UserEntity userEntity){
        myDataBase.myDataAccsessObject().UpdateUser(userEntity);
    }
}
