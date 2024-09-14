package com.example.bookshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

import com.example.bookshop.Comman.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {
    TextView tvMobileno,tvResentOTP;
    EditText  etInputCodeOne,etInputCodeTwo,etInputCodeThree,etInputCodeFour,
            etInputCodeFive,etInputCodesix;
    AppCompatButton btnVerify;
    ProgressDialog progressDialog;
    private  String strVerificationCode,strName,strMobileNO,strEmail,strUsername,strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_otpactivity);
        tvMobileno= findViewById(R.id.tvVerifyOTPMobileNo);
        tvResentOTP=findViewById(R.id.tvVerifyOTPResentOTP);
        etInputCodeOne=findViewById(R.id.etVerifyOTPInputOne);
        etInputCodeTwo=findViewById(R.id.etVerifyOTPInputTwo);
        etInputCodeThree=findViewById(R.id.etVerifyOTPInputThree);
        etInputCodeFour=findViewById(R.id.etVerifyOTPInputFour);
        etInputCodeFive=findViewById(R.id.etVerifyOTPInputFive);
        etInputCodesix=findViewById(R.id.etVerifyOTPInputSix);

        btnVerify=findViewById(R.id.acbVerifyOTPVerify);
       strVerificationCode= getIntent().getStringExtra("verificationCode");
       strName = getIntent().getStringExtra("name");
       strMobileNO= getIntent().getStringExtra("mobileno");
       strEmail = getIntent().getStringExtra("email");
       strUsername = getIntent().getStringExtra("username");
       strPassword = getIntent().getStringExtra("password");

       btnVerify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              if (etInputCodeOne.getText().toString().trim().isEmpty() ||
                  etInputCodeTwo.getText().toString().trim().isEmpty() ||
                   etInputCodeThree.getText().toString().trim().isEmpty() ||
                   etInputCodeFour.getText().toString().trim().isEmpty() ||
                  etInputCodeFive.getText().toString().trim().isEmpty() ||
                  etInputCodesix.getText().toString().trim().isEmpty() )
              {
                  Toast.makeText(VerifyOTPActivity.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
              }
              String otpCode = etInputCodeOne.getText().toString()+
                      etInputCodeTwo.getText().toString()+
                      etInputCodeThree.getText().toString()+
                      etInputCodeFour.getText().toString()+
                      etInputCodeFive.getText().toString()+
                      etInputCodesix.getText().toString();

              if(strVerificationCode!=null)
              {
                  progressDialog= new ProgressDialog(VerifyOTPActivity.this);
                  progressDialog.setTitle("Verifying OTP");
                  progressDialog.setMessage("Please Wait....");
                  progressDialog.setCanceledOnTouchOutside(false);
                  progressDialog.show();

                PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                        strVerificationCode,otpCode);
                  FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful())
                                  {
                                      progressDialog.dismiss();
                                      userRegisterDetails();
                                  }
                                  else
                                  {
                                      progressDialog.dismiss();
                                      Toast.makeText(VerifyOTPActivity.this, "OTP Verification Fail ", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });


              }

           }
       });

       tvResentOTP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"
                               + strMobileNO,
                       60, TimeUnit.SECONDS,
                       VerifyOTPActivity.this,
                       new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                               Toast.makeText(VerifyOTPActivity.this,"Verification Success",
                                       Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               progressDialog.dismiss();
                               Toast.makeText(VerifyOTPActivity.this, "Verification Failed",
                                       Toast.LENGTH_SHORT).show();
                           }
                           //                              onCodeSend Methode Aahe
                           @Override
                           public void onCodeSent(@NonNull String newVerificationCode, @NonNull
                           PhoneAuthProvider.ForceResendingToken forceResendingToken)
                           {
                               strVerificationCode =newVerificationCode;
                           }
                       });
           }
       });

        setUpInputOTP();
    }

    private void userRegisterDetails() {
        //Client and server communication  and over network data transfer and maniputer
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();   //put the data
        params.put("name",strName);  //2 argument
        params.put("mobileno", strMobileNO);
        params.put("email",strEmail);
        params.put("Username",strUsername);
        params.put("password",strPassword);

        client.post(Urls.registrationUserWebServices,
                params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status=  response.getString("success");
                            if(status.equals("1"))
                            {
                                Toast.makeText(VerifyOTPActivity.this,"Registration Success",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(VerifyOTPActivity.this, LinkageError.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(VerifyOTPActivity.this,"Already Data Present",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        } catch (JSONException e)
                        {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(VerifyOTPActivity.this,"Server Error ",
                                Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void setUpInputOTP() {
        etInputCodeOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().trim().isEmpty())
            {
                etInputCodeTwo.requestFocus();
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputCodeTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().trim().isEmpty())
            {
                etInputCodeThree.requestFocus();
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputCodeThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(!s.toString().trim().isEmpty())
              {
                  etInputCodeFour.requestFocus();
              }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
 etInputCodeFour.addTextChangedListener(new TextWatcher() {
     @Override
     public void beforeTextChanged(CharSequence s, int start, int count, int after) {

     }

     @Override
     public void onTextChanged(CharSequence s, int start, int before, int count) {
    if(!s.toString().trim().isEmpty())
    {
    etInputCodeFive.requestFocus();
    }
     }

     @Override
     public void afterTextChanged(Editable s) {

     }
 });
        etInputCodeFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    etInputCodesix.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}