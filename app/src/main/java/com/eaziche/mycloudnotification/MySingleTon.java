package com.eaziche.mycloudnotification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleTon {

    private static MySingleTon mInstance;
    private static Context context;
    private RequestQueue queue;

    public MySingleTon(Context c) {
        context = c.getApplicationContext();
        queue = getRequestQue();
        // TODO Auto-generated constructor stub
    }

    public static synchronized MySingleTon getInstance(Context c) {
        if (mInstance == null) {
            mInstance = new MySingleTon(c);
        }
        return mInstance;
    }

    public RequestQueue getRequestQue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    public <T> void addToRequestQue(Request<T> request) {
        queue.add(request);
    }
}