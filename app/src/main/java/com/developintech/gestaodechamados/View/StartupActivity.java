package com.developintech.gestaodechamados.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developintech.gestaodechamados.R;

public class StartupActivity extends AppCompatActivity {

    //URL BASE DEFINIDA NO ARQUIVO api_configs.xml

    String storedEmail = "", storedPassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                //editor.clear();  //for bebugging
                //editor.commit(); //for bebugging
                storedEmail = pref.getString("email", null);
                storedPassword = pref.getString("password", null);

                if(storedEmail == null){
                    Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                }
                else{
                    Intent in = new Intent(getApplicationContext(), ListaChamadosActivity.class);
                    startActivity(in);
                }
                StartupActivity.this.finish();
            }
        }, 2000);
    }
}