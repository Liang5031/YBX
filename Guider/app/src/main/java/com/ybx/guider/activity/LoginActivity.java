package com.ybx.guider.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ybx.guider.R;
import com.ybx.guider.VolleyRequestQueue;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

//        tv = (TextView)this.findViewById(R.id.response);
        String url ="http://www.baidu.com";

        // Request a string response from the provided URL.
//        XMLRequest xmlRequest = new XMLRequest(Request.Method.GET, url, this, this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,this,this);
        // Add the request to the RequestQueue.
        VolleyRequestQueue.getInstance(this).add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        tv.setText("That didn't work!");
    }

    @Override
    public void onResponse(String response) {
        // Display the first 500 characters of the response string.
//        tv.setText("Response is: "+ response.substring(0,500));
    }
}
