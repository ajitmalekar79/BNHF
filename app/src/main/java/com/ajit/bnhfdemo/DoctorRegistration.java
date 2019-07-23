package com.ajit.bnhfdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajit.bnhfdemo.Blog.ProfileActivity;
import com.ajit.bnhfdemo.Blog.RegisterActivity;
import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRegistration extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String label="SignUp";

    LinearLayout Reg_sec;
    EditText mob,reg,pass,repass;
    TextView name;
    Button btn_sign,btn_log;
    static int ch=0;
    Doctor drObj;

    static String uemail, firstName, lastName;

    UserService userService;


    /*
    ** For OpenAuth
     */

    private SignInButton login;
    private GoogleApiClient googleApiClient;
    private static final int Req_code =9001;

    /*
    ** For Firebase
     */

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        /*
        **
        * Code For Registration
         */
        name = findViewById(R.id.editText1);
        reg = findViewById(R.id.editText3);
        pass = findViewById(R.id.editText5);
        repass = findViewById(R.id.editText6);
        btn_sign = findViewById(R.id.btnSign);
        btn_log = findViewById(R.id.button2);
        Reg_sec = findViewById(R.id.regSec);
        login = findViewById(R.id.btn_login);
        userService = ApiUtils.getUserService();
        Reg_sec.setVisibility(View.GONE);



        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorRegistration.this,DoctorLogin.class);
                startActivity(intent);
            }
        });


        /*
        ** Code For OpenAuth
         */


        login.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();



    }

    /*
    ** Methods For Open Auth
     */

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:
                logIn();
                break;
        }

    }



    private void logIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, Req_code);

    }

    /*private void logOut(){

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }*/

    private void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String uname = account.getDisplayName();
            uemail = account.getEmail();
            firstName = account.getGivenName();
            lastName = account.getFamilyName();

            name.setText("Welcome "+uname);
            toast(uname);
            updateUI(true);
        }
        else {
            updateUI(false);
            toast("Fail to load");
        }

    }

    private void updateUI(boolean isLogin){

        if(isLogin){
            toast("Login Done!");
            Reg_sec.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }
        else {
            Reg_sec.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Req_code){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }



    /*
    ** All Methods For Registration
     */


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() throws Exception {
        if (isEmpty(reg)) {
            reg.setError("Reg.No.Required!");
        } else if (isEmpty(pass)) {
            pass.setError("Password Required!");
        } else if (isEmpty(repass)) {
            repass.setError("Password Required!");
        } else {
            String passw = pass.getText().toString();
            String repassw = repass.getText().toString();

            if (passw.equals(repassw)) {

                    toast("Data Inserted!");
                    ch=1;

            } else {
                repass.setError("Password is not matched!");

                ch = 0;
            }
        }


    }
    void toast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void RegisterDoctor(View view) {
        try {
            checkDataEntered();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ch==0){
                toast("Reinsert Data");
            }else if(ch==1) {

                        drObj = new Doctor();
                        //drObj.setId("5");
                        drObj.setFirstName(firstName);
                        drObj.setLastName(lastName);
                        drObj.setEmailId(uemail);
                        drObj.setRegistrationNo(reg.getText().toString());
                        drObj.setPassword(pass.getText().toString());
                        drObj.setDoctorId(firstName+drObj.getId());

                        AddDoctor(drObj);
                //toast("User Registerd Successfully..");

                //toast(firstName);


            }


    }




    private void AddDoctor(final Doctor drObj) {



        Call<Doctor> call = userService.AddDoctor(drObj);
        Log.d("Details", "UserService");



        call.enqueue(new Callback<Doctor>() {

            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {

                Log.d("Response:","In Response");
                if(response.isSuccessful()){
                //drObj = response.body();

                Log.d("Response", "Success");
                toast("Doctor Registered Successfully");

                    Intent intent = new Intent(DoctorRegistration.this, DoctorProfile.class);
                    intent.putExtra("id", drObj.getId());
                    intent.putExtra("drId", drObj.getDoctorId());
                    intent.putExtra("password", drObj.getPassword());

                    registerInFirebase(drObj.getFirstName(),drObj.getEmailId(),drObj.getPassword());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);

                }else{
                    toast("Invalid Request..");
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {

                Log.d("Response", "Fail");
                toast("Data Not Updated..");

            }
        });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void registerInFirebase(String userName, String emailid, String passwrd){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");

        final String username = userName;
        final String email = emailid;
        final String password = passwrd;
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    toast("Current User="+user_id);
                    Log.d("In Registration","Success");
                    DatabaseReference current_user_db = mDatabase.child(user_id);
                    current_user_db.child("Username").setValue(username);
                    current_user_db.child("Image").setValue("Default");
                    Toast.makeText(DoctorRegistration.this, "Registeration Succesful", Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            Toast.makeText(DoctorRegistration.this, "Complete all fields", Toast.LENGTH_SHORT).show();
        }
    }


}
