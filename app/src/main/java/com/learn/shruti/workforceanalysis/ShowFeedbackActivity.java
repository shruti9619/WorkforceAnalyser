package com.learn.shruti.workforceanalysis;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.util.ArrayList;
import java.util.List;


public class ShowFeedbackActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView reviewRecycleView;
    private List<Review> reviewList;
    private List<Review> reviewSearchList;
    private FeedbackAdapter fbdadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);
        reviewList = new ArrayList<>();
        reviewSearchList = new ArrayList<>();
        reviewRecycleView = (RecyclerView)findViewById(R.id.feedbackrecycleView);
        reviewRecycleView.setHasFixedSize(true);
        reviewRecycleView.setLayoutManager(new LinearLayoutManager(this));
        fbdadapter = new FeedbackAdapter(reviewList);

        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Review r = postSnapshot.getValue(Review.class);

                    if(r.rating!=0)
                        reviewList.add(r);

                    Toast.makeText(ShowFeedbackActivity.this,"com: " + r.comments + ", rate " + r.rating,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("SOme Tag: ", "Failed to read value.", error.toException());
            }
        });

        reviewRecycleView.setAdapter(fbdadapter);
    }




    //recycle view code





    // search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        //searchView.setSearchableInfo(
        //      searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(query.length()!=10)
                    Toast.makeText(ShowFeedbackActivity.this,"date format dd/mm/yyyy",Toast.LENGTH_SHORT).show();
                else
                {
                    Boolean flag = false;
                    for (Review r : reviewList)
                    {
                        if((r.dateOfReview == query))
                        {
                            //DONT CLEAR THE Main list . rather create a new list which has your single element and then
                            // pass it to the recycleview
                            reviewSearchList.add(r);
                            flag = true;
                        }
                    }
                    if (!flag)
                    {
                        searchView.setQuery("No Matches!", false);
                    }
                }

                Toast.makeText(ShowFeedbackActivity.this,"submitted",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(ShowDataActivity.this,"textchanged",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        return super.onCreateOptionsMenu(menu);
    }











}
