package com.example.roomassignment.View.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.MyRoom.MyDataBase;
import com.example.roomassignment.Model.MyRoom.RegisterUser;
import com.example.roomassignment.Model.Session.Sessionmanager;
import com.example.roomassignment.R;
import com.example.roomassignment.View.HomeActivity;
import com.example.roomassignment.View.Utilities.LoginValidation;
import com.example.roomassignment.View.login.UserLogin;
import com.example.roomassignment.View.signUp.UserSignup;
import com.example.roomassignment.ViewModel.AddNewOwner;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    private CircularImageView circularImageView;
    private ImageView ClickImg;
    private EditText fname;
    private EditText sname;
    private EditText email;
    private EditText pass;
    private EditText phone;
    private Sessionmanager sessionmanager;
    private TextView updatebtn;
    private AddNewOwner addNewOwner;
    private static final int PICK_IMAGE = 100;
    private ImageView closelogo;
    private LoginValidation loginValidation;
    private String imageuri;
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
        setContentView(R.layout.activity_profile);
        init();
        setUIDataFromDb();
    }

    private void setUIDataFromDb() {
        String OwnerEmail = sessionmanager.getEmail();
        RegisterUser user = addNewOwner.getOwnerUserDetail(OwnerEmail);
        imageuri = user.getImage();
        fname.setText(user.getFisrt_name());
        sname.setText(user.getSecond_name());
        email.setText(user.getUseremail());
        phone.setText(user.getUser_mobile());
        pass.setText(user.getPassword());
        if(user.getImage()!=null)
            Glide.with(this).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(circularImageView);
    }

    private void init() {
        circularImageView = findViewById(R.id.puserimage);
        ClickImg = findViewById(R.id.pclickcamera1);
        fname = findViewById(R.id.pfirstname);
        sname = findViewById(R.id.psecondname);
        email = findViewById(R.id.pemail);
        phone = findViewById(R.id.petPhoneNum);
        pass = findViewById(R.id.pcpassword);

        fname.addTextChangedListener(textWatcher);
        sname.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);
        pass.addTextChangedListener(textWatcher);

        updatebtn = findViewById(R.id.updatebtn);
        sessionmanager=new Sessionmanager(this);
        addNewOwner = new AddNewOwner(this);
        closelogo = findViewById(R.id.closelogo);

        closelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loginValidation = new LoginValidation(this);

        ClickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserImage();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });
    }

    public void EnableButton(){
        if(UtilValid()){
            updatebtn.setBackground(getResources().getDrawable(R.drawable.enablebtn));
            updatebtn.setEnabled(true);
        }else{
            updatebtn.setBackground(getResources().getDrawable(R.drawable.disable_btn));
            updatebtn.setEnabled(false);
        }
    }

    public boolean UtilValid() {
        if (fname.getText().toString().isEmpty() || sname.getText().toString().isEmpty()
                || email.getText().toString().isEmpty() || phone.getText().toString().isEmpty()
                || pass.getText().toString().isEmpty())
            return false;
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
            Glide.with(this).load(data.getData()).apply(RequestOptions.circleCropTransform()).into(circularImageView);
        }
    }

    public void CheckValidation(){
        if(checkFirstName() && checkSecondName() && checkEmail() &&  checkPhoneNumber() && checkPassword()){
             UpdateUserDetail();
        }
    }

    private void UpdateUserDetail() {
        RegisterUser obj = new RegisterUser();
        obj.setFisrt_name(fname.getText().toString());
        obj.setSecond_name(sname.getText().toString());
        obj.setImage(imageuri);
        obj.setUser_mobile(phone.getText().toString());
        obj.setPassword(pass.getText().toString());
        obj.setUseremail(email.getText().toString());
        addNewOwner.UpdateOwnerdetails(obj);
        Snackbar.make(getWindow().getDecorView().getRootView(),"Owner User Details Updated", Snackbar.LENGTH_LONG).show();
        Intent intent=new Intent(ProfileActivity.this, HomeActivity.class);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private boolean checkFirstName(){
        String fname1 = fname.getText().toString().trim();
        if(loginValidation.checkfirstname(fname1)){
            Snackbar.make(getWindow().getDecorView().getRootView(),getString(R.string.First_name_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkSecondName(){
        String sname1=sname.getText().toString().trim();
        if(loginValidation.checksecondname(sname1)){
            Snackbar.make(getWindow().getDecorView().getRootView(),getString(R.string.second_name_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkEmail(){
        String email1=email.getText().toString().trim();
        if(!loginValidation.isvalidEmail(email1)){
            Snackbar.make(getWindow().getDecorView().getRootView(),getString(R.string.enter_your_email), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPhoneNumber(){
        String mob=phone.getText().toString().trim();
        if(!loginValidation.isValidPhoneNumber(mob)){
            Snackbar.make(getWindow().getDecorView().getRootView(),getString(R.string.valid_phone_number), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword(){
        String pass1=pass.getText().toString().trim();
        if(!loginValidation.isPasswordCorrect(pass1)){
            Snackbar.make(getWindow().getDecorView().getRootView(),getString(R.string.pass_greater_than_3), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}