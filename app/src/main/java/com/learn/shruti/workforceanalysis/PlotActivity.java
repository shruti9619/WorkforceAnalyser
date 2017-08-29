package com.learn.shruti.workforceanalysis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;


public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        int unhappy_count = 0;
        int satisfied_count = 0;
        int happy_count = 0;




        SharedPreferences sharedPreferences = getSharedPreferences("WorkForceAnalyser", MODE_PRIVATE);

        if(sharedPreferences.contains("unhappy_count"))
           unhappy_count = sharedPreferences.getInt("unhappy_count",0);

        if(sharedPreferences.contains("happy_count"))
            happy_count = sharedPreferences.getInt("happy_count",0);


        if(sharedPreferences.contains("satisfied_count"))
            satisfied_count = sharedPreferences.getInt("satisfied_count",0);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, unhappy_count),
                new DataPoint(2, satisfied_count),
                new DataPoint(3, happy_count)
        });

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Unhappy", "Satisfied", "Happy"});
//
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.addSeries(series);
        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plot, menu);


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

            case R.id.keywordgen:
                startActivity(new Intent(PlotActivity.this,FeedbackAnalysisActivity.class));
                break;

            case R.id.sentimentgen:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PlotActivity.this,LoginSignupActivity.class));
                break;
        }

        return true;
    }
}





