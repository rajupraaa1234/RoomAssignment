package com.example.roomassignment.View.signUp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.MyRoom.MyDataBase;
import com.example.roomassignment.Model.MyRoom.RegisterUser;
import com.example.roomassignment.Model.ProjectConstant.appConstant;
import com.example.roomassignment.R;
import com.example.roomassignment.View.Utilities.LoginValidation;
import com.example.roomassignment.View.login.UserLogin;
import com.example.roomassignment.ViewModel.AddNewOwner;
import com.example.roomassignment.databinding.ActivityUserSignupBinding;
import com.google.android.material.snackbar.Snackbar;

public class UserSignup extends AppCompatActivity implements View.OnClickListener {

    ActivityUserSignupBinding binding;
    private LoginValidation loginValidation;
    private static final int PICK_IMAGE = 100;
    private String imageuri;
    private String imgeuri="";
    private AddNewOwner addNewOwner;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            EnableButton();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_signup);
        init();
    }

    private void init() {
            binding.firstname.addTextChangedListener(textWatcher);
            binding.secondname.addTextChangedListener(textWatcher);
            binding.email.addTextChangedListener(textWatcher);
            binding.etPhoneNum.addTextChangedListener(textWatcher);
            binding.password.addTextChangedListener(textWatcher);
            binding.cpassword.addTextChangedListener(textWatcher);
            binding.reg.setOnClickListener(this);
            binding.tvSignIn.setOnClickListener(this);
            binding.clickcamera1.setOnClickListener(this);
            binding.closelogo.setOnClickListener(this);
            loginValidation=new LoginValidation(this);
            addNewOwner=new AddNewOwner(this);
            EnableButton();
            SocialMediaDataInitilize();
    }


    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.closelogo:{
                 Intent intent=new Intent(UserSignup.this, UserLogin.class);
                 startActivity(intent);
                 finish();
                 break;
             }
             case R.id.tvSignIn:
                 Intent intent=new Intent(UserSignup.this, UserLogin.class);
                 startActivity(intent);
                 finish();
                 break;

             case R.id.clickcamera1:
                 setUserImage();
                 break;

             case R.id.reg:
                 CheckValidation();
                 break;
         }
    }


    public void CheckValidation(){
        if(checkFirstName() && checkSecondName() && checkEmail() &&  checkPhoneNumber() && checkPassword() && PasswordMatch()){
            HitSinghup();
        }
    }

    public void EnableButton(){
        if(UtilValid()){
            binding.reg.setBackground(getResources().getDrawable(R.drawable.enablebtn));
            binding.reg.setEnabled(true);
        }else{
            binding.reg.setBackground(getResources().getDrawable(R.drawable.disable_btn));
            binding.reg.setEnabled(false);
        }
    }

    public boolean UtilValid() {
        if (binding.firstname.getText().toString().isEmpty() || binding.secondname.getText().toString().isEmpty()
                || binding.email.getText().toString().isEmpty() || binding.etPhoneNum.getText().toString().isEmpty()
                || binding.password.getText().toString().isEmpty() || binding.cpassword.getText().toString().isEmpty())
            return false;
        return true;
    }

    private boolean checkFirstName(){
        String fname=binding.firstname.getText().toString().trim();
        if(loginValidation.checkfirstname(fname)){
            Snackbar.make(binding.getRoot(),getString(R.string.First_name_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkSecondName(){
        String sname=binding.secondname.getText().toString().trim();
        if(loginValidation.checksecondname(sname)){
            Snackbar.make(binding.getRoot(),getString(R.string.second_name_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkEmail(){
        String email=binding.email.getText().toString().trim();
        if(!loginValidation.isvalidEmail(email)){
            Snackbar.make(binding.getRoot(),getString(R.string.enter_your_email), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPhoneNumber(){
        String mob=binding.etPhoneNum.getText().toString().trim();
        if(!loginValidation.isValidPhoneNumber(mob)){
            Snackbar.make(binding.getRoot(),getString(R.string.valid_phone_number), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword(){
        String pass=binding.password.getText().toString().trim();
        if(!loginValidation.isPasswordCorrect(pass)){
            Snackbar.make(binding.getRoot(),getString(R.string.pass_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean PasswordMatch(){
        String pass=binding.password.getText().toString().trim();
        String cpass=binding.cpassword.getText().toString().trim();
        if(!pass.equals(cpass)){
            Snackbar.make(binding.getRoot(),getString(R.string.pass_not_match), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void setUserImage(){
        Intent gallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            imageuri=data.getData().toString();
            Glide.with(this).load(data.getData()).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
        }
    }

    private void HitSinghup(){
        String firstname = binding.firstname.getText().toString().trim();
        String secondname = binding.secondname.getText().toString().trim();
        String eml = binding.email.getText().toString().trim();
        String phn = binding.etPhoneNum.getText().toString().trim();
        String pass = binding.password.getText().toString().trim();
        String img = imageuri;
        RegisterUser newUser = new RegisterUser();
        newUser.setFisrt_name(firstname);
        newUser.setImage(img);
        newUser.setSecond_name(secondname);
        newUser.setUseremail(eml);
        newUser.setPassword(pass);
        newUser.setUser_mobile(phn);
        boolean res = addNewOwner.inserOwnertdata(newUser);
        if (res){
            String temp = getString(R.string.Registration_Successfully);
            Snackbar.make(binding.getRoot(), temp, Snackbar.LENGTH_LONG).show();
            Intent intent=new Intent(UserSignup.this,UserLogin.class);
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }else{
            String temp = getString(R.string.This_Owner_has_Already_registered);
            Snackbar.make(binding.getRoot(), temp, Snackbar.LENGTH_LONG).show();
        }
    }


    public void SocialMediaDataInitilize(){
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
              SetData(intent);
        }
    }



    public void SetData(Intent intent){
         Bundle bundle=intent.getExtras();
         String fullname=bundle.getString(appConstant.firstname,"");
         String email=bundle.getString(appConstant.email,"");
         String first_name=bundle.getString(appConstant.givenname,"");
         String image=bundle.getString(appConstant.image,"");
         String type=bundle.getString(appConstant.type,"");
         binding.firstname.setText(fullname);
         binding.secondname.setText(first_name);
         binding.email.setText(email);
         if(type.equals("google") && !image.isEmpty()) {
             Uri uri = Uri.parse(image);
             Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
         }else if(type.equals("facebook")){
             Glide.with(this).load(image).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
         }
    }



    private void OpenAlertForLogout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you Sure want to close signUp")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(UserSignup.this,UserLogin.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onBackPressed() {
        OpenAlertForLogout();
        return;
    }
}