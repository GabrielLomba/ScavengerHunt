package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;

public class HuntItems extends AppCompatActivity {

    public static ArrayList<LineItem> itemList = new ArrayList<>();
    public static ArrayAdapter<LineItem> itemAdapter;
    public static final String POSITION = "position";
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt_items);

        Intent intent = getIntent();
        pos = intent.getIntExtra(Tab1.ID, 0);

        /*** Firebase ***/
        Firebase.setAndroidContext(this);


        /*** Load items ***/
        itemList = Tab1.myHuntDB.getAllItems(Tab1.huntList.get(pos));

        ListView listView = (ListView)findViewById(R.id.listview2);
        //itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        itemAdapter = new ArrayAdapter<LineItem>(this, android.R.layout.simple_list_item_2, android.R.id.text1, itemList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(itemList.get(position).toString());
                text2.setText(itemList.get(position).isComplete() ? "Complete" : "Incomplete");
                return view;
            }
        };
        listView.setAdapter(itemAdapter);

        /*** Navigate to more info screen ***/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(HuntItems.this, More_info.class);
                intent.putExtra(POSITION, position);
                intent.putExtra("nameOfHunt", Tab1.huntList.get(pos));
                startActivity(intent);
            }
        });

        /*** Add new task FAB ***/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = getIntent();
                int pos = getIntent.getIntExtra(Tab1.ID, 0);
                Intent intent = new Intent(HuntItems.this, Add_Hunt_Item.class);
                intent.putExtra("name", Tab1.huntList.get(pos));
                startActivity(intent);
            }
        });


        Button edit = (Button) findViewById(R.id.editHunt);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HuntItems.this, AddScavengerHunt.class);
                intent.putExtra("name", Tab1.huntList.get(pos));
                startActivity(intent);
            }});

        /*** Share button ***/
        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*** Only share if at least one task ***/
                if (itemList.size() > 0) {
                    //   Intent intent = getIntent();
                    //  int pos = intent.getIntExtra(Tab1.ID, 0);
                    Firebase myFirebaseRef = new Firebase("https://cs4720scavhunt.firebaseio.com/hunts");
                    final String name = Tab1.huntList.get(pos);
                    /*** Check if there is already a hunt with this name ***/
                    myFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*** Don't share if a hunt with that name already exists ***/
                            if (dataSnapshot.child(name).exists()) {
                                Context context = getApplicationContext();
                                CharSequence text = "A hunt with that name already exists. Please rename before adding.";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            /*** If everything is okay, go ahead and share ***/
                            else {
                                Firebase thisHunt = dataSnapshot.getRef().child(name);
                                thisHunt.setValue(itemList);

                                Context context = getApplicationContext();
                                CharSequence text = "Scavenger Hunt shared!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                /*** Otherwise do not share ***/
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Attempt to share failed. Please create a task first.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tab1.myHuntDB.deleteHunt(Tab1.huntList.get(pos));
                Tab1.myImgDB.deleteHunt(Tab1.huntList.get(pos));
                Tab1.myDB.deleteHunt(Tab1.huntList.get(pos));

                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*** Toolbar ***/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Tab1.huntList.get(pos));
    }

}
