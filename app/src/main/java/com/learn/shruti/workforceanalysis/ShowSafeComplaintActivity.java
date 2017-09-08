package com.learn.shruti.workforceanalysis;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AnalogClock;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.learn.shruti.workforceanalysis.Model.AnonymousComplaint;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShowSafeComplaintActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView compRecycleView;
    private List<AnonymousComplaint> compList;
    private List<AnonymousComplaint> compSearchList;
    private SafeComplaintAdapter safeComplaintAdapter;
    MenuItem searchItem;
    SearchView searchView;
    FloatingActionButton plotfab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_safe_complaint);

        plotfab = (FloatingActionButton)findViewById(R.id.issueplotfab);


        compList = new ArrayList<>();
        compSearchList = new ArrayList<>();
        compRecycleView = (RecyclerView)findViewById(R.id.comprecycleView);
        //reviewRecycleView.setHasFixedSize(true);
        compRecycleView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<String> issuesArray = new ArrayList<>();
        issuesArray.add( "Facilities");
        issuesArray.add( "Workspace");
        issuesArray.add( "Time Schedule");
        issuesArray.add( "Others");


        mDatabase = FirebaseDatabase.getInstance().getReference("anonym");

        safeComplaintAdapter = new SafeComplaintAdapter(compList);
        // fetching data from firebase to listen for changes in data and populating the list
        mDatabase.addValueEventListener(new ValueEventListener() {

            int facilities =0;
            int workspace = 0;
            int timeschedule = 0;
            int others = 0;



            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                compList.clear();
                safeComplaintAdapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    AnonymousComplaint c = postSnapshot.getValue(AnonymousComplaint.class);

                    compList.add(c);

                    if(c.Issue.equalsIgnoreCase(issuesArray.get(0)))
                    {
                        facilities+=1;
                    }

                    else if(c.Issue.equalsIgnoreCase(issuesArray.get(1)))
                    {
                        workspace+=1;
                    }

                    else if(c.Issue.equalsIgnoreCase(issuesArray.get(2)))
                    {
                        timeschedule+=1;
                    }

                    else if(c.Issue.equalsIgnoreCase(issuesArray.get(3)))
                    {
                        others+=1;
                    }
                }


                storeIssueOccurance(facilities,workspace,timeschedule,others);

                Collections.sort(compList, new Comparator<AnonymousComplaint>() {
                    DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                    @Override
                    public int compare(AnonymousComplaint o1, AnonymousComplaint o2) {
                        try {
                            return f.parse(o2.dateOfIssue).compareTo(f.parse(o1.dateOfIssue));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                safeComplaintAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        compRecycleView.setAdapter(safeComplaintAdapter);

        plotfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent plotintent = new Intent(ShowSafeComplaintActivity.this, PlotActivity.class);
                plotintent.putExtra("plot_issues",true);
                startActivity(plotintent);
            }
        });



    }



    // method to fetch categories of various issue numbers showing there urgency and commonness
    public void storeIssueOccurance(int facilities, int workspace, int timeschedule, int others)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("WorkForceAnalyser", MODE_PRIVATE);
        sharedPreferences.edit().putInt("facilities_count",facilities).commit();
        sharedPreferences.edit().putInt("workspace_count",workspace).commit();
        sharedPreferences.edit().putInt("time_schedule_count",timeschedule).commit();
        sharedPreferences.edit().putInt("others_count",others).commit();
    }




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
                    //Toast.makeText(ShowFeedbackActivity.this,"not focused",Toast.LENGTH_SHORT).show();
                    searchView.setQuery("", false);
                    compSearchList.clear();
                    safeComplaintAdapter = new SafeComplaintAdapter(compList);
                    safeComplaintAdapter.notifyDataSetChanged();
                    compRecycleView.setAdapter(safeComplaintAdapter);
                    //safeComplaintAdapter.notifyDataSetChanged();
                }
            }
        });

        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(query.length()!=10)
                    Toast.makeText(ShowSafeComplaintActivity.this,"date format dd/mm/yyyy",Toast.LENGTH_SHORT).show();
                else
                {
                    Boolean flag = false;
                    for (AnonymousComplaint r : compList)
                    {
                        if((r.dateOfIssue.equalsIgnoreCase(query)))
                        {
                            //DONT CLEAR THE Main list . rather create a new list which has your single element and then
                            // pass it to the recycleview
                            compSearchList.add(r);

                            flag = true;
                        }
                    }
                    if (!flag)
                    {
                        searchView.setQuery("No Matches!", false);
                    }
                    else
                    {

                        safeComplaintAdapter = new SafeComplaintAdapter(compSearchList);
                        safeComplaintAdapter.notifyDataSetChanged();
                        compRecycleView.setAdapter(safeComplaintAdapter);
                        safeComplaintAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                compSearchList.clear();
                safeComplaintAdapter.notifyDataSetChanged();
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

            case R.id.employees:
                startActivity(new Intent(ShowSafeComplaintActivity.this,ShowEmployeeActivity.class));
                break;

            case R.id.safecompmenu:
                startActivity(new Intent(ShowSafeComplaintActivity.this,ShowSafeComplaintActivity.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ShowSafeComplaintActivity.this,LoginSignupActivity.class));
                break;
        }

        return true;
    }



    @Override
    public void onBackPressed() {

        searchItem.expandActionView();
        searchView.setQuery("", false);
        searchView.clearFocus();
        compSearchList.clear();
        safeComplaintAdapter = new SafeComplaintAdapter(compList);
        safeComplaintAdapter.notifyDataSetChanged();
        compRecycleView.setAdapter(safeComplaintAdapter);
        safeComplaintAdapter.notifyDataSetChanged();
        super.onBackPressed();
    }



}
