package com.example.roomassignment.View.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.roomassignment.Model.MyRoom.RegisterUser;
import com.example.roomassignment.Model.ProjectConstant.appConstant;
import com.example.roomassignment.Model.Session.Sessionmanager;
import com.example.roomassignment.R;
import com.example.roomassignment.View.HomeActivity;
import com.example.roomassignment.View.Utilities.LoginValidation;
import com.example.roomassignment.View.signUp.UserSignup;
import com.example.roomassignment.ViewModel.AddNewOwner;
import com.example.roomassignment.databinding.ActivityUserLoginBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UserLogin extends AppCompatActivity implements View.OnClickListener{

    private ActivityUserLoginBinding binding;
    private Sessionmanager sessionmanager;
    private AddNewOwner addNewOwner;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static int RC_SIGN_IN=100;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkallcompleted();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_login);
        addNewOwner=new AddNewOwner(this);
        LoginManager.getInstance().logOut();
        init();
        googlesignin();
        facebooklogin();
    }

    private void init(){
        binding.signup.setOnClickListener(this);
        binding.password.addTextChangedListener(textWatcher);
        binding.etEmail.addTextChangedListener(textWatcher);
        binding.continuebtn.setOnClickListener(this);
        sessionmanager=new Sessionmanager(this);
        checkallcompleted();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continuebtn:
                continueBtnClicked();
                break;

            case R.id.signup:
                Intent intent=new Intent(UserLogin.this, UserSignup.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void checkallcompleted(){
        String email=binding.etEmail.getText().toString();
        String password=binding.password.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            binding.continuebtn.setBackground(getResources().getDrawable(R.drawable.enablebtn));
            binding.continuebtn.setEnabled(true);
        }else{
            binding.continuebtn.setBackground(getResources().getDrawable(R.drawable.disable_btn));
            binding.continuebtn.setEnabled(false);
        }
    }

    private boolean checkValidations(){
        String etEmail = binding.etEmail.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        if (etEmail.length() == 0) {
            Snackbar.make(binding.getRoot(), R.string.please_enter_your_email, Snackbar.LENGTH_LONG).show();
        } else if (!LoginValidation.isvalidEmail(etEmail)) {
            Snackbar.make(binding.getRoot(), R.string.enter_your_email, Snackbar.LENGTH_LONG).show();
        } else if (password.length() == 0) {
            Snackbar.make(binding.getRoot(), R.string.enter_pass, Snackbar.LENGTH_LONG).show();
        } else if (password.length() <= 5) {
            Snackbar.make(binding.getRoot(), R.string.pass_greater_than_3, Snackbar.LENGTH_LONG).show();
        } else
            return true;
        return false;
    }

    public void continueBtnClicked(){
        if(checkValidations()){
            LoginApihit();
        }
    }

    private void LoginApihit(){
        String email=binding.etEmail.getText().toString().trim();
        String pass=binding.password.getText().toString().trim();
        boolean res=addNewOwner.UserExist(email);
        if(!res){
            Snackbar.make(binding.getRoot(), R.string.ownerNotReg, Snackbar.LENGTH_LONG).show();
        }else{
            boolean logined= addNewOwner.PassWordMathc(email,pass);
            if(!logined){
                Snackbar.make(binding.getRoot(), R.string.passIncorrect, Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(binding.getRoot(), R.string.owner_success, Snackbar.LENGTH_LONG).show();
                setSession(email,pass);
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                            LoginHandler();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    private void setSession(String eml,String pass) {
        sessionmanager.setEmail(eml);
        sessionmanager.setLogin(true);
        sessionmanager.setSession(eml+pass);
    }

    private void LoginHandler(){
        Intent intent=new Intent(UserLogin.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

 ///*********************Google SignIn*****************************************//
 private void googlesignin(){
     GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestEmail()
             .build();
     mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
     GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

     SignInButton signInButton = findViewById(R.id.sign_in_button);
     signInButton.setSize(SignInButton.SIZE_STANDARD);
     signInButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             signIn();
         }
     });
 }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();
                boolean ower_exist=addNewOwner.UserExist(personEmail);
                if(ower_exist){
                    setSession(personEmail,appConstant.default_password);
                    Snackbar.make(binding.getRoot(), R.string.owner_success, Snackbar.LENGTH_LONG).show();
                    Intent intent=new Intent(UserLogin.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    Bundle bundle=new Bundle();
                    bundle.putString(appConstant.firstname,personName);
                    bundle.putString(appConstant.email,personEmail);
                    bundle.putString(appConstant.givenname,personGivenName);
                    bundle.putString(appConstant.type,"google");
                    String img="";
                    if(personPhoto!=null){
                        img=personPhoto.toString();
                    }
                    bundle.putString(appConstant.image,img);
                    Intent intent = new Intent(UserLogin.this, UserSignup.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }else {
                Intent intent = new Intent(UserLogin.this, HomeActivity.class);
                startActivity(intent);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    //***************************************facebook Login**********************************************
    private void facebooklogin(){
        loginButton=findViewById(R.id.login_button);
        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(appConstant.email,"public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }
            @Override
            public void onCancel() {
                Snackbar.make(binding.getRoot(), R.string.cancel, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(binding.getRoot(), R.string.swm, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken != null) {
                loadfacebookprofile(currentAccessToken);
            }
        }
    };

    private void loadfacebookprofile(AccessToken accessToken){
        GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name=object.getString(appConstant.first_name);
                    String givenname=object.getString(appConstant.last_name);
                    String email=object.getString(appConstant.email);
                    String id=object.getString(appConstant.id);
                    String img="http://graph.facebook.com/" + id+ "/picture?type=normal";
                    boolean ower_exist=addNewOwner.UserExist(email);
                    if(ower_exist){
                        setSession(email,appConstant.default_password);
                        Intent intent=new Intent(UserLogin.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Snackbar.make(binding.getRoot(), R.string.owner_success, Snackbar.LENGTH_LONG).show();
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
                        Bundle bundle=new Bundle();
                        bundle.putString(appConstant.firstname,first_name);
                        bundle.putString(appConstant.email,email);
                        bundle.putString(appConstant.givenname,givenname);
                        bundle.putString(appConstant.image,img);
                        bundle.putString(appConstant.type,"facebook");
                        Intent intent = new Intent(UserLogin.this, UserSignup.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString(appConstant.fields," first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void OpenAlertForLogout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you Sure want to close the Application..?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

