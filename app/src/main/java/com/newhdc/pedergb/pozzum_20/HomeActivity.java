package com.newhdc.pedergb.pozzum_20;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button nav1, nav2, nav3, nav4, nav5, logout;
    JSONArray usernames;
    LinearLayout levelsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //------------------------------------ NavBar ------------------------------------------- \\
        nav1 = (Button) findViewById(R.id.navbar1);
        nav2 = (Button) findViewById(R.id.navbar2);
        nav3 = (Button) findViewById(R.id.navbar3);
        nav4 = (Button) findViewById(R.id.navbar4);
        nav5 = (Button) findViewById(R.id.navbar5);
        logout = (Button) findViewById(R.id.logout);


        nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage(nav1.getText().toString(), 1000);
                Intent buzzerIntent = new Intent(HomeActivity.this, BuzzerActivity.class);
                HomeActivity.this.startActivity(buzzerIntent);
            }
        });
        nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage(nav2.getText().toString(), 1000);
                Intent chatIntent = new Intent(HomeActivity.this, ChatActivity.class);
                HomeActivity.this.startActivity(chatIntent);
            }
        });
        nav3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage(nav3.getText().toString(), 1000);
                Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                HomeActivity.this.startActivity(homeIntent);
            }
        });
        nav4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage(nav4.getText().toString(), 1000);
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                HomeActivity.this.startActivity(settingsIntent);
            }
        });
        nav5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage(nav5.getText().toString(), 1000);
                Intent myUserIntent = new Intent(HomeActivity.this, MyUserActivity.class);
                HomeActivity.this.startActivity(myUserIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                online(Globals.logedInUser, "false");
                Globals.logedInUser = null;
                Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
                HomeActivity.this.startActivity(logoutIntent);
            }
        });

        login(new VolleyCallback() {
            @Override
            public void onSuccess() {
                addUserButtons();
            }
        });

}

    // ------------------------- GET (stil using post method though..) -------------------------- \\
    public void login(final HomeActivity.VolleyCallback callback){

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        String url =Globals.IP + "/find";

            /* ----------------Post data----------------- */
        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                //TODO - FINNE UT HVORFOR VI BRUKER POST METODE SOM GETTER
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            usernames = response.getJSONArray("users");
                            callback.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastMessage("Can not connect to server", 1000);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        requestQueue.add(postRequest);
    }


    // ------------------------- Send user-logged-in message to server -------------------------- \\
    public void online(String inputUsername, String isOnline){

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        String url =Globals.IP + "/online";

        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("username", inputUsername);
        jsonParams.put("isOnline", isOnline);

            /* ----------------Post data----------------- */
        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showToastMessage("ONLINE!", 1000);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastMessage("ERROR GOING ONLINE", 1000);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);
    }

    public interface VolleyCallback{ //This is used to get result from onResponse thread
        void onSuccess();
    }

    // --------------------------- Username list -------------------------------- \\

    private void addUserButtons(){
        levelsLayout = (LinearLayout) findViewById(R.id.levelList);

        for (int i = 0; i < usernames.length(); i++) {
            final int FINAL_NUM = i;
            Button button = new Button(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(10, 5, 10, 5);
            button.setLayoutParams(lp);
            button.setBackgroundColor(Color.parseColor("#d39858"));

            try {
                button.setText(usernames.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            button.setPadding(0, 10, 0, 0);

            levelsLayout.addView(button);
            //usersList.add(button);
        }
    }


    // -------------------------- Toast message ---------------------------- \\
    public void showToastMessage(String text, int duration){
        final Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

}
