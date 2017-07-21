package com.learn.shruti.workforceanalysis;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    FloatingActionButton fab_newemp, fab_showfeedback;
    DatabaseReference mDatabase;
    List<Review> reviewList;
    double ratingAvg;
    TextView ratingtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ratingtext = (TextView) findViewById(R.id.ratingtext);
        fab_newemp = (FloatingActionButton)findViewById(R.id.addempfab);
        fab_showfeedback = (FloatingActionButton)findViewById(R.id.showfeedbackfab);
        reviewList = new ArrayList<Review>();

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

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    private double ratingCalculator()
    {


        //method to calculate ratings of the day
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        final String todayDateString = dateFormat.format(cal);
        //System.out.println(dateFormat.format(cal)); //31/09/2017

        Query reviewdatequery = mDatabase.child("reviews").orderByChild("dateOfReview").equalTo(todayDateString);
        reviewdatequery.addValueEventListener(new ValueEventListener() {
            double ratingSum = 0;
            long numOfEmp = 0;
            double rating = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(DashBoardActivity.this,"in on data changed",Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Review r = postSnapshot.getValue(Review.class);

                    //review is only added once user with auth email is found
                    if(r.dateOfReview.equalsIgnoreCase(todayDateString))
                    {
                        reviewList.add(r);

                        if (r.rating != 0)
                        {
                            //calculate avg and get it outside this without final keyword error
                            ratingSum+= r.rating;
                            numOfEmp++;

                        }

                    }

                }
                calculateAvg(numOfEmp,ratingSum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return 0.0;
    }

    public void calculateAvg(long n, double sum)
    {
        ratingAvg= sum/n;
        ratingtext.setText(String.valueOf(ratingAvg));


    }
}
