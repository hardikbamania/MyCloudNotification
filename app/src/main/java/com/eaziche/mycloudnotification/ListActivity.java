package com.eaziche.mycloudnotification;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    public static DatabaseReference online;
    ArrayList<String> arrayListName = new ArrayList<>();
    ArrayList<MessageModel> arrayList = new ArrayList<>();
    ArrayList<String> chatIdList = new ArrayList<>();
    ListView recyclerView;
    ArrayAdapter<String> arrayAdapter;
    private String MYID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListName);

        recyclerView = (ListView) findViewById(R.id.recyclerView1);
        recyclerView.setAdapter(arrayAdapter);
        recyclerView.setOnItemClickListener(this);

        MYID = getSharedPreferences(getString(R.string.PREFERENCE), MODE_PRIVATE).getString("ID", "");

        if (MYID.equalsIgnoreCase(""))
            setMyID();
        else {
            online = FirebaseDatabase.getInstance().getReference("online").child(String.valueOf(MYID)).child("status");
            online.setValue(true);
            setList();
        }
    }

    private void setMyID() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter ID");

        final EditText id = new EditText(this);

        builder.setView(id);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MYID = id.getText().toString();
                online = FirebaseDatabase.getInstance().getReference("online").child(String.valueOf(MYID)).child("status");
                online.setValue(true);
                SharedPreferences preferences = getSharedPreferences(getString(R.string.PREFERENCE), MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("ID", MYID);
                edit.apply();
                setList();
            }
        });

        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                setMyID();
            }
        });

        builder.show();
    }

    private void setList() {
        JsonArrayRequest request = new JsonArrayRequest("http://192.168.0.104/test/get_id.php?ID=" + MYID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(ListActivity.this, response.toString() + "", Toast.LENGTH_SHORT).show();
                Log.e("TAG", response.toString());
                convertJsonToArrayList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleTon.getInstance(this).addToRequestQue(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        online.setValue(false);
    }

    private void convertJsonToArrayList(JSONArray response) {
        int i = 0;
        while (i < response.length()) {
            try {
                JSONObject object = (JSONObject) response.get(i);
                arrayList.add(new MessageModel(object.getString("you"), object.getString("me")));
                chatIdList.add(object.getString("chat_id"));
                arrayListName.add(object.getString("you"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chat_person", arrayList.get(position));
        intent.putExtra("chat_id", chatIdList.get(position));
        startActivity(intent);
    }
}
