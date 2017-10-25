package com.example.admin.googlegeocoderapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPressed(View view) {
        switch( view.getId() ) {
            case R.id.btnAPI:
                Intent intent = new Intent( this, APIActivity.class );
                startActivity( intent );
                break;
            case R.id.btnClass:
                Intent intent2 = new Intent( this, ClassActivity.class );
                startActivity( intent2 );
                break;
        }
    }
}