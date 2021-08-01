package com.example.roomassignment.View.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.MyRoom.UserEntity;
import com.example.roomassignment.R;
import com.example.roomassignment.ViewModel.AddUserViewModel;
import com.github.siyamed.shapeimageview.CircularImageView;

public class NotificationMessage extends AppCompatActivity {

    private TextView title;
    private CircularImageView img;
    private TextView desc;
    private AddUserViewModel addUserViewModel;
    private TextView Ncontinuebtn;
    private RelativeLayout imagRel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        init();
        fetchDataFromIntent();
    }

    private void init() {
        title = findViewById(R.id.Ntitle);
        desc = findViewById(R.id.Ndesc);
        img = findViewById(R.id.Notificationimg);
        Ncontinuebtn = findViewById(R.id.Ncontinuebtn);
        imagRel = findViewById(R.id.imgRel);
        addUserViewModel = new AddUserViewModel(this);

        Ncontinuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("mail");
        String id = bundle.getString("id");
        UserEntity userEntity = addUserViewModel.getUserData(email,Integer.parseInt(id));
        title.setText(userEntity.getName());
        desc.setText(userEntity.getDesc());
        String imageStringUri=userEntity.getUser_image();
        if(userEntity.getUser_image()!=null && !userEntity.getUser_image().isEmpty()){
            imagRel.setVisibility(View.VISIBLE);
            Log.i("MyImage","Available");
            Uri uri=Uri.parse(imageStringUri);
            Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(img);
        }
    }
}