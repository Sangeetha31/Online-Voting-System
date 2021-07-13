package com.example.onlinevoting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.firebase.database.collection.LLRBNode;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class recaptchaPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    CheckBox check_box;
    GoogleApiClient googleApiClient;
    String SiteKey="6Lf1lHEbAAAAAPDwCfABpQ7uCnCvzydJqTGNKwln";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recaptcha_page);
        getSupportActionBar().setTitle("Recaptcha Verification");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#185cab"));
        actionBar.setBackgroundDrawable(colorDrawable);
        check_box=findViewById(R.id.check_box);
       googleApiClient = new GoogleApiClient.Builder(this)
               .addApi(SafetyNet.API)
               .addConnectionCallbacks(recaptchaPage.this)
               .build();
       googleApiClient.connect();

       check_box.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(check_box.isChecked()){
                   SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                           .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                               @Override
                               public void onResult(@NonNull @NotNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                   Status status = recaptchaTokenResult.getStatus();
                                   if((status != null) && status.isSuccess()){
                                       Toast.makeText(getApplicationContext(),"Successfully verified",Toast.LENGTH_SHORT).show();
                                       check_box.setTextColor(Color.GREEN);
                                       Intent intent = new Intent(recaptchaPage.this,home.class);
                                       startActivity(intent);
                                   }
                               }
                           });
               }else{
                   check_box.setTextColor(Color.BLACK);
               }
           }
       });
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}