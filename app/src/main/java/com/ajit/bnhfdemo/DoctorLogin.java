package com.ajit.bnhfdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajit.bnhfdemo.Blog.DoctorBlog;
import com.ajit.bnhfdemo.Blog.LoginActivity;
import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorLogin extends AppCompatActivity {

    EditText edUsername, edPassword;
    UserService userService;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        edUsername = (EditText) findViewById(R.id.userid);
        edPassword = (EditText)findViewById(R.id.login_password);
        Button btnValidate = (Button)findViewById(R.id.loginBtn);
        final TextView forgot= (TextView)findViewById(R.id.fgtpass);
        //final TextView regUser = (TextView)findViewById(R.id.regUser);
        final CheckBox checkBox=findViewById(R.id.chPass);
        final TextView createAccount = (TextView)findViewById(R.id.createAccount);
        userService = ApiUtils.getUserService();
        mProgress = new ProgressDialog(this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorLogin.this,DoctorRegistration.class);
                startActivity(intent);
            }
        });



        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = edUsername.getText().toString();
                String pass = edPassword.getText().toString();

                Log.d("In Login","Success");


                if(uname.isEmpty() || pass.isEmpty()){
                    toast("Please Fill block Properly");
                }else {

                    drLogin(uname,pass);
                    //toast("Success");

                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {

                // If it is checkec then show password else hide
                // password
                if (isChecked) {

                    checkBox.setText(R.string.hide_pwd);// change
                    // checkbox
                    // text

                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                } else {
                    checkBox.setText(R.string.show_pwd);// change
                    // checkbox
                    // text

                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edPassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password

                }

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast("Password send to your registered EmailId");
               getEmailId();
            }
        });



    }

    private void drLogin(final String drId, String password){

        Call<Doctor> call = userService.login(drId,password);


        call.enqueue(new Callback<Doctor>() {

            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {

                Log.d("Response:","In Response");
                if(response.isSuccessful()){
                    Doctor drObj = response.body();

                    Log.d("Response", "Success");
                    //toast("isSuccessfull");

                    /*toast(drObj.getDoctorId().toString());
                    toast(drObj.getPassword().toString());
*/
                    Intent intent = new Intent(DoctorLogin.this, DoctorProfile.class);
                    intent.putExtra("id", drObj.getId());
                    intent.putExtra("drId", drObj.getDoctorId());
                    intent.putExtra("password", drObj.getPassword());

                    loginInFirebase(drObj.getEmailId(),drObj.getPassword());

                    startActivity(intent);
                }else{
                    //toast("Invalid Id.");
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("In Failure..");
                edPassword.setError("Invalid Password");

            }
        });
    }

    public void loginInFirebase(String email, String password){


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    checkUserExistence();
                }else {
                    Toast.makeText(DoctorLogin.this, "Couldn't login, User not found For Blog", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void checkUserExistence() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)) {
                    toast("U Have Permission to write Blog Also");
                    //startActivity(new Intent(LoginActivity.this, DoctorBlog.class));
                } else {
                    Toast.makeText(DoctorLogin.this, "User dont have permission to write Blog!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getEmailId(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLogin.this);
        final EditText mailId = new EditText(DoctorLogin.this);

        builder.setView(mailId);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mProgress.show();

                String emailId = mailId.getText().toString();
                mAuth= FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(emailId)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // do something when mail was sent successfully.

                                    mProgress.cancel();
                                    toast("Password send to your registered EmailId");
                                } else {
                                    // ...
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getEmailId();
            }
        });
        builder.show();
    }


    private void toast(String success) {
        Toast.makeText(this,success,Toast.LENGTH_SHORT).show();
    }
}
