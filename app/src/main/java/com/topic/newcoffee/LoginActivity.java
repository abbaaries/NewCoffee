package com.topic.newcoffee;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference userRef = db.getReference("users");
    private String userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        Firebase firebaseRef = new Firebase("https://coffee-20171127.firebaseio.com/");
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("onAuthStateChanged","登入:"+user.getUid());
                    userUID = user.getUid();
                }else {
                    Log.d("onAuthStateChanged","已登出");
                }
            }
        };

        firebaseRef.child( userUID ).child("phone").setValue("1234567");
        firebaseRef.child( userUID ).child("name").setValue("Sam");
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    public void login(View v) {
        final String email =((EditText)findViewById(R.id.et_loginEmail)).getText().toString();
        final String password =((EditText)findViewById(R.id.et_loginPassword)).getText().toString();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("onComplete","登入失敗");
                    register(email,password);
                }

            }
        });
        Log.d("AUTH",email+"/"+password);
    }

    private void register(final String email, final String password) {
        new AlertDialog.Builder(LoginActivity.this).setTitle("登入問題")
                .setMessage("無此帳號,是否要以此帳號密碼註冊?")
                .setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createUser(email,password);
                    }
                }).
                setNegativeButton("取消", null).show();
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if(task.isComplete()) {
                            message = "註冊成功";
                        }else {
                            message = "註冊失敗";
                        }
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage(message).setPositiveButton("OK",null).show();
                    }
                });
    }
}
