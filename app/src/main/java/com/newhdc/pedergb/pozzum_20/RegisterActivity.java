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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, password2;
    Button regUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.editUsername);
        password  = (EditText) findViewById(R.id.editPassword);
        password2  = (EditText) findViewById(R.id.editPassword2);

        regUser = (Button) findViewById(R.id.bRegiserUser);

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clientsideValidtion()) {
                    postUser(username.getText().toString(), password.getText().toString());
                    //showToastMessage("User sent to database", 1000);
                }
            }
        });
    }

    // --------------------------------------- POST ---------------------------------------- \\
    public void postUser(String inputUsername, String inputPassword){//Endret: tok vek "final" foran inputUsername

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        //String url = "http://192.168.1.135:3000/addUser"; //Tidenes verste feil...
        //String url = "http://10.22.46.153:3000/addUser";
        String url = Globals.IP + "/addUser";
            /* ----------------Post data----------------- */
        Map<String, String> jsonParams = new HashMap<>();

        jsonParams.put("username", inputUsername);
        jsonParams.put("password", inputPassword);

        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, url,

                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")){
                                showToastMessage("User added to database", 1000);
                                Intent mainmenuIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                RegisterActivity.this.startActivity(mainmenuIntent);
                            }
                            else username.setError("Username is taken");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO - LEGGE INN RESPONS VED ERROR
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

    // -------------------------- Validation ---------------------------- \\
    public boolean clientsideValidtion(){
        if (username.getText().toString().equals("") || password.getText().toString().equals("") ||
                password2.getText().toString().equals("")){
            if (username.getText().toString().equals(""))username.setError("Please fill in all fields");
            if (password.getText().toString().equals(""))password.setError("Please fill in all fields");
            if (password2.getText().toString().equals(""))password2.setError("Please fill in all fields");
            showToastMessage("Please fill in missing fields", 1000);
            return false;
        }
        else if (password.getText().toString().length() < 5){
            password.setError("Password must contain at least 5 characters");
            showToastMessage("Password to short", 1000);
            return false;
        }
        else if (!password.getText().toString().equals(password2.getText().toString())){
            password.setError("Does not match");
            password2.setError("Does not match");
            showToastMessage("Fields does not match", 1000);
            return false;
        }
        return true;
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

//ToDo Dette gjelder for GET requests
// ----------------/*Json Request* ---------------------------------/
        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //add request to queue
        requestQueue.add(jsonObjectRequest);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //add request to queue
        requestQueue.add(jsonArrayRequest);*/