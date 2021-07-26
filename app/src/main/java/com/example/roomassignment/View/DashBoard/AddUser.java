package com.example.roomassignment.View.DashBoard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.ProjectConstant.appConstant;
import com.example.roomassignment.R;
import com.example.roomassignment.View.Utilities.LoginValidation;
import com.example.roomassignment.databinding.ActivityAddUserBinding;
import com.google.android.material.snackbar.Snackbar;


public class AddUser extends AppCompatActivity implements View.OnClickListener{
    private static final int PICK_IMAGE = 100;
    ActivityAddUserBinding binding;
    private String imageuri="";
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
        binding= DataBindingUtil.setContentView(AddUser.this,R.layout.activity_add_user);
        initilization();
        EnableButton();
    }

    private void initilization() {
         binding.userdesc.addTextChangedListener(textWatcher);
         binding.username.addTextChangedListener(textWatcher);

         binding.userid.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                  String temp=firstNameValidation(binding.username.getText().toString().trim());
                  if(!temp.equals("OK")){
                      Snackbar.make(binding.getRoot(), temp, Snackbar.LENGTH_LONG).show();
                  }
             }
         });
         binding.addbtn.setOnClickListener(this);
         binding.cancelbtn.setOnClickListener(this);
         binding.cancelbtn.setBackground(getResources().getDrawable(R.drawable.enablebtn));
         binding.cancelbtn.setEnabled(true);
         binding.clickcamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                setUserImage();
                return false;
            }
         });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.addbtn:
                AddUserMethod();
                break;
            case R.id.cancelbtn:
                Cancel();
                break;
        }
    }

    private void Cancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void AddUserMethod(){
        String name=binding.username.getText().toString().trim();
        String id=binding.userid.getText().toString().trim();
        String desc=binding.userdesc.getText().toString().trim();

        // return UserEntryData
        Intent returnIntent = new Intent();
        returnIntent.putExtra(appConstant.user_name, name);
        returnIntent.putExtra(appConstant.user_id, id);
        returnIntent.putExtra(appConstant.user_desc, desc);
        returnIntent.putExtra(appConstant.image,imageuri);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void EnableButton(){
        String res=UtilValid();
        if(res.equals(appConstant.OK) && firstNameValidation(binding.username.getText().toString().trim()).equals("OK")){
            binding.addbtn.setBackground(getResources().getDrawable(R.drawable.enablebtn));
            binding.addbtn.setEnabled(true);
        }else{
            binding.addbtn.setBackground(getResources().getDrawable(R.drawable.disable_btn));
            binding.addbtn.setEnabled(false);
        }
    }
    public void setUserImage(){
        Intent gallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    public String UtilValid(){
        String result=appConstant.OK;
        if(binding.username.getText().toString().isEmpty()){
            return getString(R.string.enterUserName);
        }else if(binding.userid.getText().toString().isEmpty()){
            return getString(R.string.enterUserid);
        }else if(binding.userdesc.getText().toString().isEmpty()){
            return getString(R.string.enterUserDesc);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
             imageuri=data.getData().toString();
             Glide.with(AddUser.this).load(data.getData()).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
        }
    }

    public String firstNameValidation(String name){
        String res="OK";
        if(name.isEmpty()){
            res=getString(R.string.enter_UserName);
        }else if(name.length()<3){
            res=getString(R.string.UserName_greater);
        }else if(LoginValidation.checkPersonname(name)){
            res=getString(R.string.Not_UserName_Valid);
        }
        return res;
    }
}