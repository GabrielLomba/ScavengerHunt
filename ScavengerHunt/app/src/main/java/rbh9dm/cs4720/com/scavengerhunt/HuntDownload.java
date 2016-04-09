package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HuntDownload extends AppCompatActivity {

    public static ArrayList<LineItem> itemList = new ArrayList<LineItem>();
    public static ArrayAdapter<LineItem> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt_download);

        /*** Toolbar ***/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String title = intent.getStringExtra(Tab2.TITLE);
        getSupportActionBar().setTitle(title);

        /*** Firebase ***/
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://cs4720scavhunt.firebaseio.com/");

        /*** Load all items from Firebase ***/
        itemList.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot = snapshot.child("hunts");
                Intent intent = getIntent();
                String selected = intent.getStringExtra(Tab2.TITLE);
                for (DataSnapshot child : snapshot.getChildren()) {
                    /*** Get the right scavenger hunt ***/
                    if (child.getKey().equals(selected)) {
                        Object picReq = true, locReq = true, latitude = 0, longitude = 0;
                        String name = "", description = "", nameOfLocation = "";
                        Map<String, String> read;

                        /*** Read the tasks from Firebase***/
                        for (DataSnapshot task : child.getChildren()) {
                            read = (Map<String, String>) task.getValue();
                            for (Map.Entry<String, String> entry : read.entrySet()) {
                                switch (entry.getKey()){
                                    case "name":
                                        name = entry.getValue().toString();
                                        break;
                                    case "description":
                                        description = entry.getValue().toString();
                                        break;
                                    case "pictureRequired":
                                        picReq = entry.getValue();
                                        break;
                                    case "locationRequired":
                                        locReq = entry.getValue();
                                        break;
                                    case "nameOfLocation":
                                        nameOfLocation = entry.getValue();
                                        break;
                                    case "latitude":
                                        latitude = entry.getValue();
                                        break;
                                    case "longitude":
                                        longitude = entry.getValue();
                                        break;
                                }
                            }

                            itemList.add(new LineItem(name, description, (boolean) picReq, (boolean) locReq, nameOfLocation, (float)latitude, (float)longitude));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ListView listView = (ListView) findViewById(R.id.downloadList);
        itemAdapter = new ArrayAdapter<LineItem>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(itemAdapter);

        /*** Download button ***/
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String selected = intent.getStringExtra(Tab2.TITLE);

                /** find a unique name for the hunt **/
                if (Tab1.myDB.exists(selected)) {
                    Integer i = 1;
                    while (Tab1.myDB.exists(selected + "(" + i.toString() + ")")) {
                        i++;
                    }
                    selected += "(" + i + ")";
                }
                for (LineItem task : itemList) {
                    Tab1.myHuntDB.insertHunt(selected, task.getName(), task.getDescription(), task.isPictureRequired(), task.isLocationRequired(), task.getNameOfLocation(), task.getLatitude(), task.getLongitude());
                }
                Tab1.myDB.insertHunt(selected, false);
                Tab1.huntList.add(new ScavengerHunt(selected));
                Tab1.huntsAdapter.notifyDataSetChanged();

                Context context = getApplicationContext();
                CharSequence text = "Scavenger Hunt " + selected + " downloaded!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }
}
