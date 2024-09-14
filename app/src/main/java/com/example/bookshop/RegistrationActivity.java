package com.example.bookshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookshop.Comman.NetworkChangeLister;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {
    EditText etName, etmobileNo, etEmailId, etUsername, etPassword;
    Button btnRegister;
    boolean doubletap = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
NetworkChangeLister networkChangeLister=new NetworkChangeLister();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
       preferences= PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        editor = preferences.edit();

        etName = findViewById(R.id.etRegisterName);
        etmobileNo = findViewById(R.id.etRegisterMobileN0);
        etEmailId = findViewById(R.id.etRegisterEmailId);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.etRegisterRegister);


        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etName.getText().toString().isEmpty())
                {
                    etName.setError("Please Enter Name");
                } else if (etmobileNo.getText().toString().isEmpty())
                {
                    etmobileNo.setError("Please Enter Mobile No");
                } else if (etmobileNo.getText().toString().length() != 10)
                {
                    etmobileNo.setError("Please Enter 10 Digit Mobile No");
                } else if (etEmailId.getText().toString().isEmpty())
                {
                    etEmailId.setError("Please Enter Email Id");
                } else if (!etEmailId.getText().toString().contains("@") || !etEmailId.getText().toString().contains(".com"))
                {
                    etEmailId.setError("Please Enter Valid Email Id");
                } else if (etUsername.getText().toString().isEmpty())
                {
                    etUsername.setError("Please Enter User name");
                } else if (etPassword.getText().toString().isEmpty())
                {
                    etPassword.setError("Please Enter Password");
                } else if (etName.getText().toString().isEmpty())
                {
                    etName.setError("Please Enter name");
                } else if (etUsername.getText().toString().length() < 8)
                {
                    etUsername.setError("Please Enter atleast 8 Character ");
                } else if (etPassword.getText().toString().length() < 8)
                {
                    etPassword.setError("Please Enter Atleast 8 character");
                } else
                {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please Wait.....");
                    progressDialog.setMessage("Registration is in Process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    //verifyPhoneNumber methode 5 argument
                    //1 arg jya mobile number varti otp verify karacha to
                    //2 arg time mili second
                    //current activity name
                    //arg 5 verification succerss zalkka nahi
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"
                                    + etmobileNo.getText().toString(),
                            60, TimeUnit.SECONDS,
                            RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                            {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this,"Verification Success",
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                  progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
//                              onCodeSend Methode Aahe
                                @Override
                                public void onCodeSent(@NonNull String verificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken)
                                {
                                    Intent intent=new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("verificationCode",verificationCode);
                                    intent.putExtra("name",etName.getText().toString());
                                    intent.putExtra("mobileno",etmobileNo.getText().toString());
                                    intent.putExtra("email",etEmailId.getText().toString());
                                    intent.putExtra("username",etUsername.getText().toString());
                                    intent.putExtra("password",etPassword.getText().toString());
                                   startActivity(intent);
                                }
                            });

                }
            }
        });
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


   @Override
      public void onBackPressed ()
       {
           if (doubletap)
           {
                finishAffinity();
           }
           else
            {
              Toast.makeText(RegistrationActivity.this, "Press Again to Exit App", Toast.LENGTH_SHORT).show();
               doubletap = true;
                Handler h = new Handler();
                h.postDelayed(new Runnable()
               {
                    @Override
                   public void run()
                   {

                        doubletap = false;
                    }
                }, 2000);
            }
       }
}
