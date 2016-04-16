package rbh9dm.cs4720.com.scavengerhunt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class More_info extends AppCompatActivity {

    private int pos;
    private String nameOfHunt;

    private boolean locReq;
    private boolean locOk;
    private boolean picReq;
    private boolean picOk;

    private CheckBox chk;
    private boolean taskDone;

    private double lat;
    private double lon;
    private double currLat = 0;
    private double currLon = 0;

    LocationManager locationManager;
    LocationListener locationListener;

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        pos = intent.getIntExtra(HuntItems.POSITION, 0);
        nameOfHunt = intent.getStringExtra("nameOfHunt");

        getSupportActionBar().setTitle(HuntItems.itemList.get(pos).getName());

        /************ Set description text ****************/
        TextView description = (TextView) findViewById(R.id.Mdescription);
        description.setText(HuntItems.itemList.get(pos).getDescription());

        /*********** What is required? What is ok? ******************/
        locReq = HuntItems.itemList.get(pos).isLocationRequired();
        locOk = HuntItems.itemList.get(pos).isLocationOk();
        picReq = HuntItems.itemList.get(pos).isPictureRequired();
        picOk = HuntItems.itemList.get(pos).isPictureOk();

        /*********** Is the task complete? ****************/
        taskDone = HuntItems.itemList.get(pos).isComplete();
        chk = (CheckBox) findViewById(R.id.complete);
        if ((locReq && !locOk) || (picReq && !picOk))
            chk.setEnabled(false);
        else if (taskDone) {
            chk.setChecked(true);
        }
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // Adjust hunt item in list
                HuntItems.itemList.get(pos).setComplete(isChecked);
                HuntItems.itemAdapter.notifyDataSetChanged();

                // Update DB
                Tab1.myHuntDB.updateComplete(nameOfHunt, HuntItems.itemList.get(pos).getName(), isChecked);
            }
        });

        /************* Is location required? Is it ok? **************/
        TextView Mloc = (TextView) findViewById(R.id.Mlocation);

        if (locReq)
            Mloc.setText("Location: Required");
        if (locOk)
            Mloc.setTextColor(Color.parseColor("#00ff00"));

        /************** Set Destination ******************/
        TextView dest = (TextView) findViewById(R.id.dest);
        dest.setText("Destination: " + HuntItems.itemList.get(pos).getNameOfLocation());


        /*********** Set lat/long of destination **************/
        lat = HuntItems.itemList.get(pos).getLatitude();
        lon = HuntItems.itemList.get(pos).getLongitude();

        /************* Set up location listener ***************/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                // Find current location
                currLat = location.getLatitude();
                currLon = location.getLongitude();

                // Get distance from goal
                float[] dist = new float[3];
                Location.distanceBetween(lat, lon, currLat, currLon, dist);
                float distance = dist[0];

                // Display Distance
                TextView distText = (TextView) findViewById(R.id.distance);
                distText.setText("Distance: "+distance);

                // Adjust if appropriate
                if (distance < 1000 && !locOk) {
                    TextView Mloc = (TextView) findViewById(R.id.Mlocation);
                    Mloc.setTextColor(Color.parseColor("#00ff00"));
                    locOk = true;

                    // Adjust hunt item in list
                    HuntItems.itemList.get(pos).setLocationOk(true);
                    HuntItems.itemAdapter.notifyDataSetChanged();

                    // Update DB
                    Tab1.myHuntDB.updateLocOk(nameOfHunt, HuntItems.itemList.get(pos).getName(), true);

                    // Enable check box?
                    if (!(picReq && !picOk))
                        chk.setEnabled(true);
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            TextView distText = (TextView) findViewById(R.id.distance);
            distText.setText("Distance: GPS not enabled");
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        /************** Is picture required? Is it ok? ************/
        TextView Mpic = (TextView) findViewById(R.id.Mpic);

        picReq = HuntItems.itemList.get(pos).isPictureRequired();
        if (picReq)
            Mpic.setText("Picture: Required");
        picOk = HuntItems.itemList.get(pos).isPictureOk();
        if (picOk)
            Mpic.setTextColor(Color.parseColor("#00ff00"));

        /************** Retrieve picture **********************/
        iv = (ImageView) findViewById(R.id.iv);
        Bitmap bp = Tab1.myImgDB.getImage(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName());
        if (bp != null)
            iv.setImageBitmap(bp);


        /**************** Set up camera button *************/
        Button camera = (Button) this.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the
                // calling application Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // start the image capture Intent
                startActivityForResult(intent, 0);

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bp = (Bitmap) extras.get("data");
            if (Tab1.myImgDB.exists(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName()))
                Tab1.myImgDB.updateHunt(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName(), bp);
            else
                Tab1.myImgDB.insertHunt(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName(), bp);
            iv.setImageBitmap(bp);

            if (!picOk) {
                TextView Mpic = (TextView) findViewById(R.id.Mpic);
                Mpic.setTextColor(Color.parseColor("#00ff00"));
                picOk = true;

                // Adjust hunt item in list
                HuntItems.itemList.get(pos).setPictureOk(true);
                HuntItems.itemAdapter.notifyDataSetChanged();

                // Update DB
                Tab1.myHuntDB.updatePicOk(nameOfHunt, HuntItems.itemList.get(pos).getName(), true);

                // Enable Checkbox?
                // Enable check box?
                if (!(locReq && !locOk))
                    chk.setEnabled(true);
            }
        }
    }

}
