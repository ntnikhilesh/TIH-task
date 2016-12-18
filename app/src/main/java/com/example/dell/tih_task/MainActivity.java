package com.example.dell.tih_task;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private String name;
    private Button add_room;
    private EditText room_name;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms=new ArrayList<>();
    //get root reference of the real time databas
    // ...so that we can fetch its child(root name)
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        add_room=(Button)findViewById(R.id.button_add_room);
        room_name=(EditText)findViewById(R.id.et_room_name);
        listView=(ListView)findViewById(R.id.listview);


        //Array list for store active chat rooms
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.list_item,list_of_rooms);
        listView.setAdapter(arrayAdapter);

        //After running the app , first will take user name ,without user name , we can t proceed on app
        request_user_name();


        //add rooms in data base
        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inside data base , every thing store as Key - Value pair.So will use Map for maintain such record
                Map<String,Object> map=new HashMap<String, Object>();
                map.put(room_name.getText().toString(),"");//here 2nd paramenter is null bz we need only chat room as root element
                //update room name in database
                root.updateChildren(map);


            }
        });


        //handle event on database update
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //to avoid dublicacy , will use Set.Her two Room name will be same but they can store diff values
                Set<String> set=new HashSet<String>();
                Iterator iterator=dataSnapshot.getChildren().iterator();
                //read database line by line
                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                //clear old data of list view
                list_of_rooms.clear();
                //load new data in list view
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //handle click of list view item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //After clink on any Room name(Group name) user will enter on chat page
                Intent intent=new Intent(getApplicationContext(),Chat_Room.class);
                //will send room name and user name to chat page
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });
    }
    private void request_user_name()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("User name");

        final EditText input_field=new EditText(this);
        builder.setView(input_field);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name=input_field.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_user_name();
            }
        });
        builder.show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
