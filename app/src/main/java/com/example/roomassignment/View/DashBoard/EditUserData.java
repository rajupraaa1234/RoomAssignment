package com.example.roomassignment.View.DashBoard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.ProjectConstant.appConstant;
import com.example.roomassignment.R;
import com.example.roomassignment.databinding.ActivityEditUserDataBinding;

import java.util.Calendar;

public class EditUserData extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE = 100;
    ActivityEditUserDataBinding binding;
    private String imageuri="";
    private String time ="";
    private String date = "";
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
        binding= DataBindingUtil.setContentView(EditUserData.this,R.layout.activity_edit_user_data);
        initilization();
        EnableButton();
        initilaizeData();
    }
    public void EnableButton(){
        String res=UtilValid();
        if(res.equals(appConstant.OK)){
            binding.editbtn.setBackground(getResources().getDrawable(R.drawable.enablebtn));
            binding.editbtn.setEnabled(true);
        }else{
            binding.editbtn.setBackground(getResources().getDrawable(R.drawable.disable_btn));
            binding.editbtn.setEnabled(false);
        }
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
    public void setUserImage(){
        Intent gallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==PICK_IMAGE){
            imageuri=data.getData().toString();
            Glide.with(EditUserData.this).load(data.getData()).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
        }
    }

    private void initilization() {
        binding.userdesc.addTextChangedListener(textWatcher);
        binding.username.addTextChangedListener(textWatcher);
        binding.userid.addTextChangedListener(textWatcher);
        binding.editbtn.setOnClickListener(this);
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
            case R.id.editbtn:
                EditUserDataMethod();
                break;
            case R.id.cancelbtn:
                Cancel();
                break;
        }
    }
    private void EditUserDataMethod(){

        String name=binding.username.getText().toString().trim();
        String id=binding.userid.getText().toString().trim();
        String desc=binding.userdesc.getText().toString().trim();

        // return UserEntryData
        Intent returnIntent = new Intent();
        returnIntent.putExtra(appConstant.user_name, name);
        returnIntent.putExtra(appConstant.user_id, id);
        returnIntent.putExtra(appConstant.user_desc, desc);
        returnIntent.putExtra(appConstant.image,imageuri);
        returnIntent.putExtra(appConstant.time,time);
        returnIntent.putExtra(appConstant.date,date);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void initilaizeData(){
        Intent intent=getIntent();
        String name=intent.getStringExtra(appConstant.user_name);
        String id=intent.getStringExtra(appConstant.user_id);
        String desc=intent.getStringExtra(appConstant.user_desc);
        String imageStringUri=intent.getStringExtra(appConstant.User_image);
        String mtime = intent.getStringExtra(appConstant.time);
        String mdate = intent.getStringExtra(appConstant.date);
        imageuri=imageStringUri;
        binding.userdesc.setText(desc);
        binding.userid.setText(id);
        binding.username.setText(name);
        Log.i("mMyDate"," "+time + " " + date );
        if((mtime!=null && mdate!=null) && (!mtime.isEmpty() && !mdate.isEmpty())){
            binding.time.setChecked(true);
            binding.date.setChecked(true);
            time = mtime;
            date = mdate;
            binding.time.setText(mtime);
            binding.date.setText(mdate);
        }
        binding.time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    selectTime();
                }else{
                    time = "";
                }
            }
          }
        );

        binding.date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    selectDate();
                }else{
                    date = "";
                }
            }
          }
        );
        if(imageStringUri!=null && !imageStringUri.isEmpty()){
            Uri uri=Uri.parse(imageStringUri);
            Glide.with(EditUserData.this).load(uri).apply(RequestOptions.circleCropTransform()).into(binding.userimage);
        }
    }

    private void Cancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time = i + ":" + i1;
                binding.time.setText(FormatTime(i, i1));
            }

        }, hour, minute, false);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                binding.time.setChecked(false);
            }
        });
        timePickerDialog.show();

    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = day + "-" + (month + 1) + "-" + year;
                binding.date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                binding.date.setChecked(false);
            }
        });
        datePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }
}