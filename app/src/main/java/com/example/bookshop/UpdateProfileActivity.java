package com.example.bookshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookshop.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText etName,etMobileNo,etEmail,etUserName;
    AppCompatButton btnUpdatePeofile;
    String strName,strMobileNo,strEmail,strUserName;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etName=findViewById(R.id.etUpdateProfileName);
        etMobileNo=findViewById(R.id.etUpdateProfileMobileNo);
        etEmail=findViewById(R.id.etUpdateProfileEmail);
        etUserName=findViewById(R.id.etUpdateProfileUserName);

        btnUpdatePeofile=findViewById(R.id.btnProfileUpdate);

        strName = getIntent().getStringExtra("name");
        strMobileNo =getIntent().getStringExtra("mobileno");
        strEmail = getIntent().getStringExtra("email");
        strUserName = getIntent().getStringExtra("Username");

        etName.setText(strName);
        etMobileNo.setText(strMobileNo);
        etEmail.setText(strEmail);
        etUserName.setText(strUserName);

        btnUpdatePeofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(UpdateProfileActivity.this);
                progressDialog.setTitle("Updating Profile ");
                progressDialog.setMessage("Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                updateProfile();
            }
        });

    }

    private void updateProfile() {
        AsyncHttpClient client=new AsyncHttpClient(); //client server cummunication
        RequestParams params=new RequestParams();   //put data client
        params.put("name",etName.getText().toString());
        params.put("mobileno",etMobileNo.getText().toString());
        params.put("email",etEmail.getText().toString());
        params.put("Username",etUserName.getText().toString());

        //data update karne
        client.post(Urls.updateProgileWebService,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                //success 1 otherwise 0
                try {
                    String status=response.getString("success");
                    if (status.equals(1))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully",
                                Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UpdateProfileActivity.this,
                                MyProfileActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Update Not Done",
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
                Toast.makeText(UpdateProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}