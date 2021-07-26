package com.example.roomassignment.View.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Pattern;

public class LoginValidation {
    Context context;
    public LoginValidation(Context con){
        this.context=con;
    }
    public boolean checkfirstname(String firstname){
        if(firstname.length()<3) return true;
        return false;
    }

    public static boolean checkPersonname(String name){
        if(!name.matches("^[A-Za-z]+$")){
            return true;
        }
        return false;
    }

    public boolean checksecondname(String secondname){
        if(secondname.length()<3) return true;
        return false;
    }

    public static boolean isvalidEmail(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public boolean isPasswordCorrect(String password){
        if(password.length()<6) return false;
        return true;
    }
    public boolean issamewithpassword(String pass,String cpass){
        return pass.equals(cpass);
    }

    public boolean isValidPhoneNumber(String number) {
        if(number.length()==10 && android.util.Patterns.PHONE.matcher(number).matches()) return true;
        return false;
    }



}
