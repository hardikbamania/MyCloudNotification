package com.eaziche.mycloudnotification;


import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String APP_FCM_SERVER_URL = "http://192.168.0.104/test/fcm.php";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG_TOKEN", "Refreshed token: " + token);
        registerToken(token);
    }

    private void registerToken(final String token) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.PREFERENCE),MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(getString(R.string.TOKEN),token);
        edit.apply();

        //sendBroadcast(new Intent("TOKEN_REQUEST"));
        Log.e("TOKEN",preferences.getString("TOKEN",""));

        StringRequest request = new StringRequest(POST, APP_FCM_SERVER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Congratulation","New Token Saved !!");
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
        MySingleTon.getInstance(this).addToRequestQue(request);

    }
}
