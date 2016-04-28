package rbh9dm.cs4720.com.scavengerhunt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class Add_Hunt_Item extends AppCompatActivity {

    public String nameOfLocation = "";
    private double latitude = Double.POSITIVE_INFINITY;
    private double longitude= Double.POSITIVE_INFINITY;

    //private int position = -1;

    public static ArrayList<String> itemList = new ArrayList<>();
    public static ArrayList<LatLng> coordinates = new ArrayList<>();
    public static ArrayAdapter<String> itemAdapter;

   // public static final String LATITUDE = "latitude";
    //public static final String LONGITUDE = "longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__hunt__item);
        ListView listView = (ListView) findViewById(R.id.SearchResult);
        itemAdapter = new ArrayAdapter<>(Add_Hunt_Item.this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(itemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Intent intent = new Intent(Add_Hunt_Item.this, MapsActivity.class);
                //intent.putExtra(LATITUDE, coordinates.get(position).latitude);
                //intent.putExtra(LONGITUDE, coordinates.get(position).longitude);
                //coordinates.get(position).latitude;
                //coordinates.get(position).longitude;
                //startActivity(intent);
                //Add_Hunt_Item.this.position = position;

                nameOfLocation = itemList.get(position);
                latitude = coordinates.get(position).latitude;
                longitude = coordinates.get(position).longitude;
                EditText locationField = (EditText) findViewById(R.id.searchLoc);
                locationField.setText(Add_Hunt_Item.this.itemList.get(position));
                Context context = getApplicationContext();
                CharSequence text = "You selected " + Add_Hunt_Item.this.itemList.get(position);
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        /**** Set up toolbar ***/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a Task");

        /*** Set up Search Button ***/
        ImageButton search = (ImageButton) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationField = (EditText) findViewById(R.id.searchLoc);
                //nameOfLocation = ""+locationField.getText();
                //String newName = nameOfLocation.replaceAll(" ", "%20");
                String newName = (""+ locationField.getText()).replaceAll(" ", "%20");
                String url = "http://maps.google.com/maps/api/geocode/json?address="+newName+"&sensor=false";
                new DataLongOperationAsynchTask(getApplicationContext()).execute(url);
            }
        });


        /*** Set up FAB ***/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText locationField = (EditText) findViewById(R.id.searchLoc);
                //nameOfLocation = ""+locationField.getText();
                EditText nameField = (EditText) findViewById(R.id.nameHuntItem);
                String name = ""+nameField.getText();
                /*** Don't add if the name is empty ***/
                if (name.equals("")) {
                    Context context = getApplicationContext();
                    CharSequence text = "Please name your task.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /** Don't add if the location wasn't set **/
                else if(latitude == Float.POSITIVE_INFINITY){
                    Context context = getApplicationContext();
                    CharSequence text = "Please set a location";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /** Don't add if the name is not unique **/
                else if(Tab1.myHuntDB.exists(getIntent().getStringExtra("name"),name)){
                    Context context = getApplicationContext();
                    CharSequence text = "Names of the Tasks must be unique";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                /*** Add otherwise ***/
                else {
                    EditText descField = (EditText) findViewById(R.id.description);
                    String desc = descField.getText().toString();
                    CheckBox picReqField = (CheckBox) findViewById(R.id.picReq);
                    boolean picReq = picReqField.isChecked();
                    CheckBox locReqField = (CheckBox) findViewById(R.id.locReq);
                    boolean locReq = locReqField.isChecked();


                    LineItem item = new LineItem(name, desc, picReq, locReq, false, false, nameOfLocation, latitude, longitude, false);
                    HuntItems.itemList.add(item);

                    Intent intent = getIntent();

                    Tab1.myHuntDB.insertHunt(intent.getStringExtra("name"), name, desc, picReq, locReq, false, false, nameOfLocation, latitude, longitude, false);

                    HuntItems.itemAdapter.notifyDataSetChanged();

                    Tab1.huntDoneList.set(Tab1.huntList.indexOf(intent.getStringExtra("name")), "Incomplete");
                    Tab1.myDB.updateDone(intent.getStringExtra("name"), false);
                    Tab1.huntsAdapter.notifyDataSetChanged();

                    finish();
                }
            }
        });


    }

}
