package com.example.roomassignment.Model.CallBack;

import com.example.roomassignment.Model.MyRoom.UserEntity;

public interface UpdateUserClickListner {
    void onEdit(UserEntity userEntity,int position);
    void onDelete(UserEntity userEntity,int position);
    void onToggleClick(UserEntity userEntity,int position,Boolean change);
}
