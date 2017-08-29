package com.learn.shruti.workforceanalysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FeedbackAnalysisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_analysis);
        final TextView trialtv = (TextView)findViewById(R.id.trialtv);

        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest postReq = new StringRequest(Request.Method.POST,
                "https://apis.paralleldots.com/keywords",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // We set the response data in the TextView
                        trialtv.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error ["+error+"]");
                        // handle the error here
                    }


                }){
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> params =
                        new HashMap<String, String>();
                // param1 is the name of the parameter and param its value
                params.put("q", "shruti iyyer another keyword aditya");
                params.put("apikey","ddqUK3gJCSCzveJUZprtLXjHsiERfEa6dz0df1ZGi9c");
                return params;
            }
        };

        rq.add(postReq);

    }
}
