package com.ajit.bnhfdemo.Blog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ajit.bnhfdemo.DoctorLogin;
import com.ajit.bnhfdemo.DoctorRegistration;
import com.ajit.bnhfdemo.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private SignInButton login;
    private GoogleApiClient googleApiClient;
    private static final int Req_code =9001;
    private static int checkUser=0;

    static String uemail, password;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        login = findViewById(R.id.btnSign);

        login.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSign:
                logIn();
                break;
        }
    }

    private void logIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, Req_code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Req_code){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String uname = account.getDisplayName();
            uemail = account.getEmail();

            loginInFirebase(uname,uemail,uemail);

            if(checkUser==0) {

                registerInFirebase(uname, uemail, uemail);
                loginInFirebase(uname,uemail,uemail);

            }

            toast(uname);
        }
        else {
            toast("Fail to load");
        }

    }


    public void registerInFirebase(String userName, String emailid, String passwrd){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final String username = userName;
        final String email = emailid;
        final String password = passwrd;
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = mDatabase.child(user_id);
                    current_user_db.child("Username").setValue(username);
                    current_user_db.child("Image").setValue("Default");
                    Toast.makeText(UserLogin.this, "Registeration Succesful", Toast.LENGTH_SHORT).show();
                    /*Intent regIntent = new Intent(DoctorRegistration.this, ProfileActivity.class);
                    regIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(regIntent);*/
                }
            });
        }else {

            Toast.makeText(UserLogin.this, "Complete all fields", Toast.LENGTH_SHORT).show();
        }
    }



    public void loginInFirebase(final String uName, String email, String password){


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    checkUserExistence(uName);

                }else {
                    Toast.makeText(UserLogin.this, "Couldn't login, User not found For Blog", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void checkUserExistence(final String uName) {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)) {
                    toast("U Have Permission to read Blog");
                    checkUser = 1;
                    Intent intent = new Intent(UserLogin.this, DoctorBlog.class);
                    intent.putExtra("code","1");
                    intent.putExtra("name",uName);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserLogin.this, "User dont have permission to write Blog!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
