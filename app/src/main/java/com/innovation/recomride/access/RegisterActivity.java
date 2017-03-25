package com.innovation.recomride.access;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.innovation.recomride.R;
import com.innovation.recomride.basic.MainActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }



    protected void initViews() {
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(this);
        next = (Button) findViewById(R.id.bt_go2);
        next.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab2:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.bt_go2:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
