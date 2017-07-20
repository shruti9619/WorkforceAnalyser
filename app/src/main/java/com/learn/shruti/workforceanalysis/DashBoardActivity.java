package com.learn.shruti.workforceanalysis;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DashBoardActivity extends AppCompatActivity {

    FloatingActionButton fab_newemp, fab_showfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        fab_newemp = (FloatingActionButton)findViewById(R.id.addempfab);
        fab_showfeedback = (FloatingActionButton)findViewById(R.id.showfeedbackfab);

        fab_newemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, AddEmployeeActivity.class));
            }
        });

        fab_showfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this,ShowFeedbackActivity.class));
            }
        });
    }


    private double ratingCalculator()
    {
        //method to calculate ratings of the day
        return 0.0;
    }
}
