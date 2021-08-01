package com.example.roomassignment.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roomassignment.Model.AlarmManager.AlarmBrodcast;
import com.example.roomassignment.Model.MyRoom.RegisterUser;
import com.example.roomassignment.Model.ProjectConstant.appConstant;
import com.example.roomassignment.Model.Session.Sessionmanager;
import com.example.roomassignment.View.DashBoard.AddUser;
import com.example.roomassignment.View.DashBoard.EditUserData;
import com.example.roomassignment.Model.CallBack.UpdateUserClickListner;
import com.example.roomassignment.Model.MyRoom.UserEntity;
import com.example.roomassignment.Model.RecyclerViewAdapter;
import com.example.roomassignment.R;
import com.example.roomassignment.View.Profile.ProfileActivity;
import com.example.roomassignment.View.login.UserLogin;
import com.example.roomassignment.ViewModel.AddNewOwner;
import com.example.roomassignment.ViewModel.AddUserViewModel;
import com.example.roomassignment.databinding.ActivityHomeBinding;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, UpdateUserClickListner {

    private int LAUNCH_SECOND_ACTIVITY = 1;
    private int LAUNCH_EDIT_ACTIVITY=2;
    private Sessionmanager sessionmanager;
    ActivityHomeBinding binding;
    AddUserViewModel addUserViewModel;
    int pos=0;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private AddNewOwner addNewOwner;

    private DrawerLayout drawer_layout;
    private NavigationView navigationView;
    private ImageView toggle;
    private Toolbar toolbar;
    private TextView TbText;
    private RelativeLayout cardv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_home);
        initlization();
        intiActionBar();
        CollectData();
        swipeDelte();
        setUserImage();
    }

    private void setUserImage() {
        View hView =  navigationView.getHeaderView(0);
        CircularImageView image = (CircularImageView)hView.findViewById(R.id.himage);
        TextView uname = hView.findViewById(R.id.usernaqme);
        String OwnerEmail = sessionmanager.getEmail();
        RegisterUser user = addNewOwner.getOwnerUserDetail(OwnerEmail);
        uname.setText(user.getFisrt_name() + " " + user.getSecond_name());
        if(user.getImage()!=null && !user.getImage().isEmpty()) {
            Glide.with(this).load(user.getImage()).apply(RequestOptions.circleCropTransform()).into(image);
        }
    }


    private void initlization() {
        binding.addcart.setOnClickListener(this);
        binding.logout.setOnClickListener(this);
        addUserViewModel=new AddUserViewModel(HomeActivity.this);
        swipeRefreshLayout=binding.myswaper;
        recyclerView=findViewById(R.id.myrecycle);
        recyclerView.setHasFixedSize(true);
        sessionmanager=new Sessionmanager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addNewOwner = new AddNewOwner(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_700,R.color.teal_200,R.color.teal_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){
                CollectData();
            }
        });

        drawer_layout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        toggle = findViewById(R.id.toggle);
        toolbar = findViewById(R.id.toolbar);
        TbText = findViewById(R.id.tabTitle);
        cardv = findViewById(R.id.cardv);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationClickHandler();
            }
        });

        setNavigationClickListner();

    }

    private void setNavigationClickListner() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.mynav_home:
                        drawer_layout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profile:
                        drawer_layout.closeDrawer(GravityCompat.START);
                        openProfileActivity();
                        break;
                    case R.id.logout:
                        drawer_layout.closeDrawer(GravityCompat.START);
                        OpenAlertForLogout();
                        break;
                    case R.id.newReminder:
                        drawer_layout.closeDrawer(GravityCompat.START);
                        addUser();
                        break;
                }
                return false;
            }
        });
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void intiActionBar() {
        setSupportActionBar(toolbar);
    }

    private void NavigationClickHandler(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            drawer_layout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
          switch (v.getId()){
              case R.id.addcart:
                  addUser();
                  break;
              case R.id.logout:
                  //OpenAlertForLogout();
                  break;
          }
    }

    private void OpenAlertForLogout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        String current_user=sessionmanager.getEmail();
        builder.setMessage(current_user + " do you want to logout")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
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

    private void logout() {
            if(sessionmanager.getLogin()){
                sessionmanager.setLogin(false);
                sessionmanager.setSessionName(null);
                sessionmanager.setEmail(null);
                sessionmanager.setSession(null);
                Intent intent = new Intent(HomeActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
    }

    private void addUser() {
        Intent i = new Intent(this, AddUser.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
    }


    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LAUNCH_SECOND_ACTIVITY){
            if(resultCode== Activity.RESULT_OK){
                String user_name=data.getStringExtra(appConstant.user_name);
                String user_id=data.getStringExtra(appConstant.user_id);
                String user_desc=data.getStringExtra(appConstant.user_desc);
                String user_image=data.getStringExtra(appConstant.image);
                String time=data.getStringExtra(appConstant.time);
                String date=data.getStringExtra(appConstant.date);

                String owner_email=sessionmanager.getEmail();
                UserEntity newuser=new UserEntity();
                newuser.setName(user_name);
                newuser.setId(Integer.parseInt(user_id));
                newuser.setDesc(user_desc);
                newuser.setUser_image(user_image);
                newuser.setOwner_email(owner_email);
                newuser.setAtime(time);
                newuser.setDate(date);
                if(!time.isEmpty() && !date.isEmpty()){
                    newuser.setTime(true);
                    setAlarm(sessionmanager.getEmail(),user_id,date,time);
                }else{
                    newuser.setTime(false);
                }
                boolean res=addUserViewModel.insertdata(newuser);
                if(res){
                    Snackbar.make(binding.getRoot(), R.string.user_added_success, Snackbar.LENGTH_LONG).show();
                    recyclerViewAdapter.insertSingleItem(newuser);
                }
                else{
                    Snackbar.make(binding.getRoot(), R.string.enter_diff_id, Snackbar.LENGTH_LONG).show();
                }
            }
            if(resultCode==Activity.RESULT_CANCELED){
                String temp="Cancel";
                Snackbar.make(binding.getRoot(),temp, Snackbar.LENGTH_LONG).show();
            }
        }else if(requestCode==LAUNCH_EDIT_ACTIVITY){
            if(resultCode== Activity.RESULT_OK){
                String user_name=data.getStringExtra(appConstant.user_name);
                String user_id=data.getStringExtra(appConstant.user_id);
                String user_desc=data.getStringExtra(appConstant.user_desc);
                String user_image=data.getStringExtra(appConstant.image);
                String time = data.getStringExtra(appConstant.time);
                String date = data.getStringExtra(appConstant.date);
                String owner_email=sessionmanager.getEmail();
                UserEntity newuser=new UserEntity();
                if(!time.isEmpty() && !date.isEmpty()){
                    newuser.setTime(true);
                    newuser.setAtime(time);
                    newuser.setDate(date);
                    setAlarm(sessionmanager.getEmail(),user_id,date,time);
                }else{
                    newuser.setTime(false);
                    newuser.setAtime("");
                    newuser.setDate("");
                }
                newuser.setName(user_name);
                newuser.setId(Integer.parseInt(user_id));
                newuser.setDesc(user_desc);
                newuser.setUser_image(user_image);
                newuser.setOwner_email(owner_email);

                addUserViewModel.updateUser(newuser);
                recyclerViewAdapter.updateItem(pos,newuser);
                Snackbar.make(binding.getRoot(), R.string.user_updated_success, Snackbar.LENGTH_LONG).show();
            }if(resultCode==Activity.RESULT_CANCELED){
                String temp="Cancel";
                Snackbar.make(binding.getRoot(),temp, Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private void CollectData() {
        List<UserEntity> res=addUserViewModel.getUserData();
        recyclerViewAdapter =new RecyclerViewAdapter(this, (ArrayList<UserEntity>) res);
        recyclerView.setAdapter(recyclerViewAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onEdit(UserEntity userEntity,int position){
        pos=position;
        String name=userEntity.getName();
        String userid=String.valueOf(userEntity.getId());
        String desc=userEntity.getDesc();
        String image=userEntity.getUser_image();
        String time = userEntity.getAtime();
        String date = userEntity.getDate();
        Log.i("MyDate"," "+time + " " + date );
        Intent i = new Intent(this, EditUserData.class);
        i.putExtra(appConstant.user_name,name);
        i.putExtra(appConstant.user_id,userid);
        i.putExtra(appConstant.user_desc,desc);
        i.putExtra(appConstant.User_image,image);
        i.putExtra(appConstant.time,time);
        i.putExtra(appConstant.date,date);
        i.putExtra(appConstant.User_image,image);
        startActivityForResult(i, LAUNCH_EDIT_ACTIVITY);
    }


    @Override
    public void onDelete(UserEntity userEntity,int position) {
        boolean temp=addUserViewModel.deleteUser(userEntity);
        if(temp){
            recyclerViewAdapter.removeItem(position);
            Snackbar.make(binding.getRoot(), R.string.User_deleted, Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(binding.getRoot(), R.string.user_not_exist , Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onToggleClick(UserEntity userEntity, int position, Boolean change) {
        userEntity.setTime(change);
        addUserViewModel.updateUser(userEntity);
        if(change) {
            setAlarmIntoDB(userEntity);
            Snackbar.make(binding.getRoot(), "Reminder Set Successfullly", Snackbar.LENGTH_LONG).show();
        }else{
            Snackbar.make(binding.getRoot(), "Reminder Cancled", Snackbar.LENGTH_LONG).show();
        }
    }

    private void setAlarmIntoDB(UserEntity userEntity) {

    }


    private void swipeDelte(){
        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                        return true;// true if moved, false otherwise
                    }
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int pos = viewHolder.getAdapterPosition();
                        if(direction==ItemTouchHelper.LEFT){
                            UserEntity obj = recyclerViewAdapter.getUser(pos);
                            //recyclerViewAdapter.removeItem(pos);
                            boolean temp=addUserViewModel.deleteUser(obj);
                            if(temp){
                                recyclerViewAdapter.removeItem(pos);
                                Snackbar.make(binding.getRoot(), R.string.User_deleted, Snackbar.LENGTH_LONG).show();
                            }else{
                                Snackbar.make(binding.getRoot(), R.string.user_not_exist , Snackbar.LENGTH_LONG).show();
                            }
                            Snackbar.make(recyclerView, "Reminder Removed", Snackbar.LENGTH_SHORT)
                                    .setAction("Undo", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerViewAdapter.addAtPostions(pos,obj);
                                            addUserViewModel.insertdata(obj);
                                        }
                                    }).show();
                        }
                    }
                });
        mIth.attachToRecyclerView(recyclerView);
    }
    private void setAlarm(String Oemail,String id, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("mail", Oemail);
        intent.putExtra("id", id);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}