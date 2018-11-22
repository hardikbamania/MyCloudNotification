package com.eaziche.mycloudnotification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MainActivity extends AppCompatActivity {

    Button buttonSend;
    Button buttonAdd;
    EditText editTextMessage;
    Uri downloadUri = Uri.EMPTY, uploadUri;
    RecyclerView recyclerView;
    ArrayList<MessageModel> messages = new ArrayList<>();
    ChatAdapter chatAdapter;
    SimpleDraweeView draweeView;
    DatabaseReference drMessages;
    MessageModel myMessageModel;
    String APP_SERVER_URL = "http://192.168.0.104/test/send_notification.php";
    private StorageReference mStorageRef;
    private ImagePicker imagePicker = new ImagePicker();
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        draweeView = (SimpleDraweeView) findViewById(R.id.drawee);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        myMessageModel = getIntent().getParcelableExtra("chat_person");
        String chat_id = getIntent().getStringExtra("chat_id");
        setTitle(chat_id);
        chatAdapter = new ChatAdapter(this, messages, myMessageModel.getFrom());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(chatAdapter);
        drMessages = FirebaseDatabase.getInstance().getReference("messages").child(chat_id);

        //chatAdapter.setList(new ArrayList());

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        drMessages.keepSynced(true);
        drMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                setAllMessages(dataSnapshot);
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                setAllMessages(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startChooser();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp_key = drMessages.push().getKey();
                final String date = new SimpleDateFormat("dd-MM-yyyy ", Locale.ENGLISH).format(Calendar.getInstance().getTime());
                final String time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Calendar.getInstance().getTime());

                draweeView.setImageURI("");

                final MessageModel msgModel = new MessageModel(myMessageModel.getTo(), myMessageModel.getFrom(), editTextMessage.getText().toString(), date, downloadUri.toString(), time);

                DatabaseReference message = drMessages.child(temp_key);
                message.setValue(msgModel);


                StringRequest request = new StringRequest(POST, APP_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, "This Person Is Offline But Still You Can Send Messages", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", msgModel.getTo());
                        map.put("message", msgModel.getMsg());
                        map.put("title", msgModel.getFrom() + " Messaged You");
                        return map;
                    }
                };
                MySingleTon.getInstance(MainActivity.this).addToRequestQue(request);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void startChooser() {
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {

            }

            @Override
            public void onCropImage(Uri imageUri) {
                uploadUri = imageUri;
                draweeView.setVisibility(View.VISIBLE);
                draweeView.setImageURI(imageUri);
            }

            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        .setMultiTouchEnabled(false)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setRequestedSize(draweeView.getWidth(), draweeView.getHeight())
                        .setAspectRatio(9, 9);
            }

            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }

    private void setAllMessages(DataSnapshot dataSnapshot) {
        messages.add(dataSnapshot.getValue(MessageModel.class));
        chatAdapter.notifyDataSetChanged();
    }


}
