package com.learn.shruti.workforceanalysis;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    FloatingActionButton fab_newemp, fab_showfeedback, fab_showplotdata;
    DatabaseReference mDatabase;

    double ratingAvg;
    TextView ratingtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ratingtext = (TextView) findViewById(R.id.ratingtext);
        fab_newemp = (FloatingActionButton)findViewById(R.id.addempfab);
        fab_showfeedback = (FloatingActionButton)findViewById(R.id.showfeedbackfab);
        fab_showplotdata = (FloatingActionButton)findViewById(R.id.showplotfab);

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

        fab_showplotdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this,PlotActivity.class));
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();


        //method to calculate average rating for the day
        ratingCalculator();

    }


    private void ratingCalculator()
    {

        // to fetch today's date in string format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String todayDateString = dateFormat.format(date); //31/09/2017

        //query to fetch reviews if they are of current date to find daily rating
        Query reviewdatequery = mDatabase.child("reviews").orderByChild("dateOfReview").equalTo(todayDateString);


        reviewdatequery.addValueEventListener(new ValueEventListener() {
            double ratingSum = 0;
            long numOfEmp = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(DashBoardActivity.this,"in on data changed",Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Review r = postSnapshot.getValue(Review.class);

                    //review is only added once user with auth email is found
                    if(r.dateOfReview.equalsIgnoreCase(todayDateString))
                    {

                        if (r.rating != 0)
                        {
                            //calculate avg and get it outside this without final keyword error
                            ratingSum+= r.rating;
                            ++numOfEmp;

                        }

                    }

                }
                //separate function call because variables cant be returned from this inner class and final variables
                //can't be changed
                calculateAvg(numOfEmp,ratingSum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void calculateAvg(long n, double sum)
    {
        //Toast.makeText(DashBoardActivity.this,"numofemp "+n+" sum "+sum,Toast.LENGTH_SHORT).show();
        try{
        ratingAvg= sum/n;
        }
        catch (Exception e)
        {
            ratingAvg = 0;
        }
        if(Double.isNaN(ratingAvg))
            ratingAvg = 0;
        ratingtext.setText(String.valueOf(ratingAvg+"/10"));


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuaftersearch, menu);


        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {

                return true;
            }
        };

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.employees:
                startActivity(new Intent(DashBoardActivity.this,ShowEmployeeActivity.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DashBoardActivity.this,LoginSignupActivity.class));
                break;
        }

        return true;
    }
}
