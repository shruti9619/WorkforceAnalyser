package com.learn.shruti.workforceanalysis;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.learn.shruti.workforceanalysis.Model.Employee;
import com.learn.shruti.workforceanalysis.Model.Review;

import java.util.ArrayList;
import java.util.List;

public class ShowEmployeeActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    RecyclerView empRecycleView;
    private List<Employee> empList;
    private List<Review> reviewList;
    private List<Employee> empSearchList;
    private EmployeeAdapter empadapter;
    MenuItem searchItem;
    SearchView searchView;
    RelativeLayout recyclelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);
        reviewList = new ArrayList<>();
        empList = new ArrayList<>();
        empSearchList = new ArrayList<>();
        empRecycleView = (RecyclerView)findViewById(R.id.employeerecycleView);
        recyclelayout = (RelativeLayout)findViewById(R.id.recyclelayout);


        //reviewRecycleView.setHasFixedSize(true);
        empRecycleView.setLayoutManager(new LinearLayoutManager(this));


        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        empadapter = new EmployeeAdapter(empList);

        // data change listener for list population
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                empList.clear();
                empadapter.notifyDataSetChanged();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Employee e = postSnapshot.getValue(Employee.class);

                        if(!empList.contains(e))
                            empList.add(e);

                    //Toast.makeText(ShowFeedbackActivity.this,"com: " + r.comments + ", rate " + r.rating,
                    //      Toast.LENGTH_SHORT).show();
                }
                empadapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("SOme Tag: ", "Failed to read value.", error.toException());
            }
        });


        empRecycleView.setAdapter(empadapter);



        // event to accept click to edit employee details and long press to delete employee
        empRecycleView.addOnItemTouchListener(new RecycleTouchListener(this, empRecycleView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Employee selectedEmp = empList.get(position);
                Intent editEmployeeIntent = new Intent(ShowEmployeeActivity.this,
                        AddEmployeeActivity.class);
                editEmployeeIntent.putExtra("empname", selectedEmp.empName);
                editEmployeeIntent.putExtra("empemail", selectedEmp.empEmail);
                editEmployeeIntent.putExtra("empphone", selectedEmp.Phone);
                editEmployeeIntent.putExtra("emppass", selectedEmp.Password);
                editEmployeeIntent.putExtra("empdesig", selectedEmp.designation);
                editEmployeeIntent.putExtra("empid", selectedEmp.employeeID);
                startActivity(editEmployeeIntent);

            }

            @Override
            public void onLongClick(View view, final int position) {

              // generating alert to ask user to delete employee detail
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowEmployeeActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Delete");


                // Setting Dialog Message
                alertDialog.setMessage("Delete this employee?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_delete_black);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // removing from list
                        Employee removed = empList.remove(position);

                        // removing from firebase db
                        mDatabase.child(removed.employeeID).setValue(null);
                        empadapter.notifyDataSetChanged();

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }));

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
                    //Toast.makeText(ShowEmployeeActivity.this,"not focused",Toast.LENGTH_SHORT).show();
                    searchView.setQuery("", false);
                    empSearchList.clear();
                    empadapter = new EmployeeAdapter(empList);
                    empadapter.notifyDataSetChanged();
                    empRecycleView.setAdapter(empadapter);
                    //fbdadapter.notifyDataSetChanged();
                }
            }
        });
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + "search name" + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                    //Toast.makeText(ShowEmployeeActivity.this,"Search name",Toast.LENGTH_SHORT).show();

                    Boolean flag = false;
                    for (Employee e : empList)
                    {
                        if((e.empName.equalsIgnoreCase(query)))
                        {
                            //DONT CLEAR THE Main list . rather create a new list which has your single element and then
                            // pass it to the recycleview
                            empSearchList.add(e);

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
                        empadapter = new EmployeeAdapter(empSearchList);
                        empadapter.notifyDataSetChanged();
                        empRecycleView.setAdapter(empadapter);
                        empadapter.notifyDataSetChanged();
                    }


                //Toast.makeText(ShowFeedbackActivity.this,"submitted",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(ShowDataActivity.this,"textchanged",Toast.LENGTH_SHORT).show();
                empSearchList.clear();
                empadapter.notifyDataSetChanged();
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
                startActivity(new Intent(ShowEmployeeActivity.this,ShowEmployeeActivity.class));
                break;


            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ShowEmployeeActivity.this,LoginSignupActivity.class));
                break;
        }

        return true;
    }


    @Override
    public void onBackPressed() {

        searchItem.expandActionView();
        searchView.setQuery("", false);
        searchView.clearFocus();
        empSearchList.clear();
        empadapter = new EmployeeAdapter(empList);
        empadapter.notifyDataSetChanged();
        empRecycleView.setAdapter(empadapter);
        empadapter.notifyDataSetChanged();
        super.onBackPressed();
    }
}
