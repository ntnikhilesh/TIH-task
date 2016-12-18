package com.example.dell.tih_task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by DELL on 12/18/2016.
 */
public class Chat_Room extends AppCompatActivity{

    private Button btn_send_msg;
    private EditText et_input_msg;
    private TextView tv_chat_conv;
    private String user_name,room_name;
    private DatabaseReference root;
    private String temp_key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        btn_send_msg=(Button)findViewById(R.id.button_send_msg);
        et_input_msg=(EditText)findViewById(R.id.et_input_msg);
        tv_chat_conv=(TextView)findViewById(R.id.tv_chat_conv);
        user_name=getIntent().getExtras().get("user_name").toString();
        room_name=getIntent().getExtras().get("room_name").toString();
        setTitle("Room - "+room_name);

        root=FirebaseDatabase.getInstance().getReference().child(room_name);
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map=new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root=root.child(temp_key);
                Map<String,Object> map2=new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",et_input_msg.getText().toString());
                Log.d("msg",map2.toString());
                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_convertation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_convertation(dataSnapshot);

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
    }

    private String chat_msg,chat_user_name;
    private void append_chat_convertation(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {
           chat_msg=(String)((DataSnapshot)iterator.next()).getValue();
            chat_user_name=(String)((DataSnapshot)iterator.next()).getValue();

            tv_chat_conv.append(chat_user_name+" : "+chat_msg+"\n");
        }
    }

}
