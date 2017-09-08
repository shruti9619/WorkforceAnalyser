package com.learn.shruti.workforceanalysis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SentimentActivity extends AppCompatActivity {


    ArrayList<String> feedbackList;
    Double sumofsentiment = 0.0;
    Double avgsent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentiment);

        RequestQueue rq = Volley.newRequestQueue(this);

        feedbackList = getIntent().getStringArrayListExtra("feedbacklist");
        final TextView trtv=(TextView)findViewById(R.id.avgtv);

        //volley url call mechanism latest added by google to make http get method calls
        for(int i = 0; i<feedbackList.size();i++) {

            // trim and replace to remove spaces and add search character for url
            String url = "https://apis.paralleldots.com/sentiment?sentence1=" + feedbackList.get(i)
                    .trim()
                    .replaceAll("\\s", "%20")
                    + "&apikey=ddqUK3gJCSCzveJUZprtLXjHsiERfEa6dz0df1ZGi9c";

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            // the response is already constructed as a JSONObject!
                            try {
                                String sentiment = response.getString("sentiment");
                                //Toast.makeText(SentimentActivity.this,"Response: "+response.toString(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(SentimentActivity.this, "Sentiment: " + sentiment, Toast.LENGTH_SHORT).show();
                                //1327
                                sumofsentiment+=Double.valueOf(sentiment);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            rq.add(jsonRequest);


        }


        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {

                avgsent =100*(sumofsentiment/feedbackList.size());
                trtv.setText(avgsent.toString()+ " %");
//                Toast.makeText(SentimentActivity.this, "Sentiment avg: "
//                        + String.valueOf(sumofsentiment/feedbackList.size()), Toast.LENGTH_SHORT).show();



            }
        });

         }
}
