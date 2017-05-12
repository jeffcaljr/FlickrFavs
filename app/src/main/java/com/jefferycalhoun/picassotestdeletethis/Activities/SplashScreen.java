package com.jefferycalhoun.picassotestdeletethis.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jefferycalhoun.picassotestdeletethis.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.start_button)
    Button startButton;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.start_button)
    public void onViewClicked() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
