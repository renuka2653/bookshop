package com.example.bookshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bookshop.Comman.NetworkChangeLister;
import com.example.bookshop.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
ImageView ivLoginLogo;
TextView  tvLogintitle, tvForgetPassword,tvLogintitle1;
EditText  etLoginUser,etLoginPass;
Button btnLogin;
CheckBox checkBox;
ProgressDialog progressDialog;
GoogleSignInOptions googleSignInOptions;//show option of email
GoogleSignInClient googleSignInClient;//selected option show
    AppCompatButton btnSigninGoogle;

NetworkChangeLister networkChangeLister=new NetworkChangeLister();

SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor=sharedPreferences.edit();


      ivLoginLogo=findViewById(R.id.ivLoginlogo);
      tvLogintitle=findViewById(R.id.tvLoginTitle);
      tvLogintitle1=findViewById(R.id.tvLoginNewUser);
      etLoginPass=findViewById(R.id.etLoginPassword);
      etLoginUser=findViewById(R.id.etLoginUserName);
      btnLogin=findViewById(R.id.btnLoginLogin);
      checkBox=findViewById(R.id.cbshowhidePassword);
      btnSigninGoogle=findViewById(R.id.btnLoginSingWithGoogle);
      tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);


     googleSignInOptions =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
             requestEmail().build();
     googleSignInClient= GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);
     btnSigninGoogle.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             signIn();
         }
     });



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                   etLoginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                   etLoginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

      btnLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (etLoginUser.getText().toString().isEmpty())
              {
                  etLoginUser.setError("Please Enter Your User Name");
              } else if (etLoginPass.getText().toString().isEmpty())
              {
                 etLoginPass.setError("Please Enter Password");
              } else if (etLoginUser.getText().toString().contains("@"))
              {
                  etLoginUser.setError("Please Enter valid User Name");
              } else if (etLoginPass.getText().toString().length()<8)
              {
                  etLoginPass.setError("Please Enter 8 Charecter ");
              }
              else
              {
                  progressDialog = new ProgressDialog(LoginActivity.this);
                  progressDialog.setTitle("Please Wait");
                  progressDialog.setMessage("Login User Process");
                  progressDialog.show();

                  userLogin();
              }
          }
      });

      tvForgetPassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(LoginActivity.this,
                      ConfirmRegisterMobileNoActivity.class);
              startActivity(intent);

          }
      });



      tvLogintitle1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
              startActivity(intent);
          }
      });
    }

    private void signIn() {
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 999)
        {
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent=new Intent(LoginActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();
            }
            catch (ApiException e)
            {
                Toast.makeText(this, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeLister,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeLister);
    }

    private void userLogin() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put("Username",etLoginUser.getText().toString());
        params.put("password",etLoginPass.getText().toString());

        client.post(Urls.loginUserWebService,
                params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            String status = response.getString("success");
                            if(status.equals("1"))
                            {
                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                              editor.putString("Username",etLoginUser.getText().toString()).commit();
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Invalil User Name and Password",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Server Error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                );

    }


}
