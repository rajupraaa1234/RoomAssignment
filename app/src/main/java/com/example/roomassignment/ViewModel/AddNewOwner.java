package com.example.roomassignment.ViewModel;

import android.content.Context;

import androidx.room.Room;

import com.example.roomassignment.Model.MyRoom.MyDataBase;
import com.example.roomassignment.Model.MyRoom.RegisterUser;
import com.example.roomassignment.Model.MyRoom.UserEntity;

public class AddNewOwner {
    MyDataBase myDataBase;
    public AddNewOwner(Context context){
        myDataBase= Room.databaseBuilder(context,MyDataBase.class,"userdb").allowMainThreadQueries().build();
    }

    public boolean inserOwnertdata(RegisterUser registerUser){
        String email=registerUser.getUseremail();
        int res=myDataBase.myDataAccsessObject().isOwnerExist(email);
        if(res==0){
            myDataBase.myDataAccsessObject().insertRegister(registerUser);
            return true;
        }else{
            return false;
        }
    }

    public boolean UserExist(String email){
        int res=myDataBase.myDataAccsessObject().isOwnerExist(email);
        if(res==0){
            return false;
        }else{
            return true;
        }
    }

    public boolean PassWordMathc(String eml,String pass){
        int res=myDataBase.myDataAccsessObject().isExistUser(eml,pass);
        if(res==0) return false;
        return true;
    }

    public RegisterUser getOwnerUserDetail(String email){
        return myDataBase.myDataAccsessObject().getOwnerUser(email);
    }

    public void UpdateOwnerdetails(RegisterUser user){
        myDataBase.myDataAccsessObject().UpdateOwnerUser(user);
    }
}
