package com.example.roomassignment.Model.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.roomassignment.Model.ProjectConstant.appConstant;

public class Sessionmanager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String session;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Sessionmanager(Context context){
        sharedPreferences = context.getSharedPreferences(appConstant.app_key, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(Boolean login){
        editor.putBoolean(appConstant.key_login, login);
        editor.commit();
    }

    public boolean getLogin(){
        return sharedPreferences.getBoolean(appConstant.key_login, false);
    }

    public void setEmail(String email){
        editor.putString(appConstant.key_email, email);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(appConstant.key_email, null);
    }

    public String getSessionName(){
        return sharedPreferences.getString(appConstant.key_session_name, null);
    }

    public void setSessionName(String sessionName){
        editor.putString(appConstant.key_session_name, sessionName);
        editor.commit();
    }
}
