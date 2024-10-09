package com.example.bookshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.bookshop.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {
    ImageView ivProfilePhoto;
TextView tvmyname,tvMobileNo,tvEmail,tvUserName;
AppCompatButton btnEditProfile,btnUpdateProfie,btnSignOut;
GoogleSignInOptions googleSignInOptions;
GoogleSignInClient googleSignInClient;
SharedPreferences preferences;
String strUserName;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        preferences= PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);

        strUserName=preferences.getString("Username","");

        ivProfilePhoto = findViewById(R.id.ivmyProfilePhoto);
       tvmyname = findViewById (R.id.tvMyProfileName);
       tvMobileNo=findViewById(R.id.tvMyProfileMobileNo);
        tvEmail= findViewById (R.id.tvMyProfileEmail);
        tvUserName=findViewById(R.id.tvMyProfileUserName);
        btnSignOut = findViewById (R.id.btnMyProfileSignOut);
        btnEditProfile=findViewById(R.id.btnMyprofileEditProfile);
        btnUpdateProfie=findViewById(R.id.btnMyProfileUpdate);


        googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this,googleSignInOptions);

        GoogleSignInAccount googleSignInAccount= GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount!=null)
        {
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();
            tvmyname.setText(name);
            tvEmail.setText(email);


            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(MyProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog=new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetails();
    }

    private void getMyDetails() {
        AsyncHttpClient client=new AsyncHttpClient();   //client server Communication
        RequestParams params=new RequestParams();  //put data in AsyncHttpClient object
        params.put("Username",strUserName);
        client.post(Urls.myDetailsWebServices,params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray= response.getJSONArray("getMyDetails");
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String strid =jsonObject.getString("id");
                                String strimage = jsonObject.getString("image");
                                String strname =jsonObject.getString("name");
                                String strmobileno =jsonObject.getString("mobileno");
                                String stremail=jsonObject.getString("email");
                                String strusername =jsonObject.getString("Username");
                               // String password =jsonObject.getString("password");

                                tvmyname.setText(strname);
                                tvMobileNo.setText(strmobileno);
                                tvEmail.setText(stremail);
                                tvUserName.setText(strusername);

                                Glide.with(MyProfileActivity.this)
                                        .load("http://192.168.31.60:80/onlinebookshopdbAPI/images/"+
                                                strimage)
                                        .skipMemoryCache(true)
                                        .error(R.drawable.imagenotfound)
                                        .into(ivProfilePhoto);

                                btnUpdateProfie.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(MyProfileActivity.this,
                                                UpdateProfileActivity.class);
                                        intent.putExtra("name",strname);
                                        intent.putExtra("mobileno",strmobileno);
                                        intent.putExtra("email",stremail);
                                        intent.putExtra("Username",strusername);

                                        startActivity(intent);
                                    }
                                });
                            }



                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(MyProfileActivity.this, "Server Error ", Toast.LENGTH_SHORT).show();
                    }
                }

        );


    }
}