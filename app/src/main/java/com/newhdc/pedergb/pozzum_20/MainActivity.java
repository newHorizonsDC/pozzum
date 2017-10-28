package com.newhdc.pedergb.pozzum_20;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    TextView txtRegister;
    TextView txtWebview;
    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.inputUsername);
                password = (EditText) findViewById(R.id.inputPassword);
                login(username.getText().toString(), password.getText().toString(), new VolleyCallback() {
                    @Override
                    public void onSuccess() {
                        Globals.logedInUser = username.getText().toString();
                        online(username.getText().toString(), "true");
                        Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                        MainActivity.this.startActivity(loginIntent);
                    }
                });
                //Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                //MainActivity.this.startActivity(loginIntent);
            }
        });

        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        txtWebview = (TextView) findViewById(R.id.txtWebview);
        txtWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webviewIntent = new Intent(MainActivity.this, WebviewActivity.class);
                MainActivity.this.startActivity(webviewIntent);
            }
        });
    }
    // ------------------------- GET (stil using post method though..) -------------------------- \\
    public void login(String inputUsername, String inputPassword, final VolleyCallback callback){

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        String url = Globals.IP + "/login";
            /* ----------------Post data----------------- */
        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("username", inputUsername);
        jsonParams.put("password", inputPassword);

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,
            //TODO - FINNE UT HVORFOR VI BRUKER POST METODE SOM GETTER
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")){
                                showToastMessage("Logging in..", 1000);
                                callback.onSuccess();
                            }
                            else if (response.getString("status").equals("wrong"))password.setError("Wrong password");
                            else username.setError("No user with that name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToastMessage("Can not connect to server", 1000);
                        error.printStackTrace();
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
