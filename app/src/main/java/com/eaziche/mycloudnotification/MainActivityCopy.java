package com.eaziche.mycloudnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MainActivityCopy extends AppCompatActivity {

    Button button;
    EditText editTextTitle,editTextMessage,editTextId;
    String APP_SERVER_URL = "http://192.168.0.104/test/send_notification.php";
    String APP_FCM_SERVER_URL = "http://192.168.0.104/test/fcm.php";
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMessage =(EditText) findViewById(R.id.editTextMessage);
        editTextTitle =(EditText) findViewById(R.id.editTextTitle);
        editTextId =(EditText) findViewById(R.id.editTextId);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.PREFERENCE),MODE_PRIVATE);
                final String  token = preferences.getString(getString(R.string.TOKEN),"");

                TextView textView = (TextView) findViewById(R.id.tvToken);
                textView.setText("Got The TOKEN !!");

                StringRequest request = new StringRequest(POST, APP_FCM_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivityCopy.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("token",token);
                        return map;
                    }
                };
                MySingleTon.getInstance(MainActivityCopy.this).addToRequestQue(request);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(getString(R.string.token_filter)));
        button = (Button) findViewById(R.id.button_send_token);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.PREFERENCE),MODE_PRIVATE);
                final String  token = preferences.getString(getString(R.string.TOKEN),"");

                StringRequest request = new StringRequest(POST, APP_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivityCopy.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("id",editTextId.getText().toString().trim());
                        map.put("message",editTextMessage.getText().toString().trim());
                        map.put("title",editTextTitle.getText().toString().trim());
                        return map;
                    }
                };
                MySingleTon.getInstance(MainActivityCopy.this).addToRequestQue(request);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
