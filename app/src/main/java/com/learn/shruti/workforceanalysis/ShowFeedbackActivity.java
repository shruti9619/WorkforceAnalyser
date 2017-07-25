package com.learn.shruti.workforceanalysis;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
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
    MenuItem searchItem;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);
        reviewList = new ArrayList<>();
        reviewSearchList = new ArrayList<>();
        reviewRecycleView = (RecyclerView)findViewById(R.id.feedbackrecycleView);
        reviewRecycleView.setHasFixedSize(true);
        reviewRecycleView.setLayoutManager(new LinearLayoutManager(this));


        mDatabase = FirebaseDatabase.getInstance().getReference("reviews");

        fbdadapter = new FeedbackAdapter(reviewList);
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
                fbdadapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("SOme Tag: ", "Failed to read value.", error.toException());
            }
        });


        reviewRecycleView.setAdapter(fbdadapter);


    }



    // search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        getMenuInflater().inflate(R.menu.menuaftersearch, menu);
        searchItem = menu.findItem(R.id.search);

        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(!isFocused)
                {
                    Toast.makeText(ShowFeedbackActivity.this,"not focused",Toast.LENGTH_SHORT).show();
                    searchView.setQuery("", false);
                    reviewSearchList.clear();
                    fbdadapter = new FeedbackAdapter(reviewList);
                    fbdadapter.notifyDataSetChanged();
                    reviewRecycleView.setAdapter(fbdadapter);
                    //fbdadapter.notifyDataSetChanged();
                }
            }
        });
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search) + "</font>"));
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
                        if((r.dateOfReview.equalsIgnoreCase(query)))
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
                    else
                    {
                        //reviewRecycleView.setAdapter(new FeedbackAdapter(reviewSearchList));
                        fbdadapter = new FeedbackAdapter(reviewSearchList);
                        fbdadapter.notifyDataSetChanged();
                        reviewRecycleView.setAdapter(fbdadapter);
                        fbdadapter.notifyDataSetChanged();
                    }
                }

                Toast.makeText(ShowFeedbackActivity.this,"submitted",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(ShowDataActivity.this,"textchanged",Toast.LENGTH_SHORT).show();
                reviewSearchList.clear();
                fbdadapter.notifyDataSetChanged();
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




    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ShowFeedbackActivity.this,LoginSignupActivity.class));
                break;
        }

        return true;
    }


    @Override
    public void onBackPressed() {

        searchItem.expandActionView();
        searchView.setQuery("", false);
        searchView.clearFocus();
        reviewSearchList.clear();
        fbdadapter = new FeedbackAdapter(reviewList);
        fbdadapter.notifyDataSetChanged();
        reviewRecycleView.setAdapter(fbdadapter);
        fbdadapter.notifyDataSetChanged();
        super.onBackPressed();
    }




}
