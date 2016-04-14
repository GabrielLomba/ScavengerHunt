package rbh9dm.cs4720.com.scavengerhunt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class More_info extends AppCompatActivity {

    private double lat;
    private double lon;
    public int pos;
    public String nameOfHunt;
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

        TextView description = (TextView) findViewById(R.id.Mdescription);
        description.setText(HuntItems.itemList.get(pos).getDescription());

        TextView loc = (TextView) findViewById(R.id.Mlocation);
        loc.setText(HuntItems.itemList.get(pos).getNameOfLocation());

        iv = (ImageView) findViewById(R.id.iv);

        Bitmap bp = Tab1.myImgDB.getImage(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName());

        if (bp != null)
            iv.setImageBitmap(bp);

        lat = HuntItems.itemList.get(pos).getLatitude();
        lon = HuntItems.itemList.get(pos).getLongitude();

        CheckBox chk = (CheckBox) findViewById(R.id.chkcamera);

        if (HuntItems.itemList.get(pos).isPictureRequired())
            chk.setText("Required");
        chk.setChecked(HuntItems.itemList.get(pos).isPictureOk());
        chk = (CheckBox) findViewById(R.id.chklocation);

        if (HuntItems.itemList.get(pos).isLocationRequired())
            chk.setText("Required");
        chk.setChecked(HuntItems.itemList.get(pos).isLocationOk());

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                currLat = location.getLatitude();
                currLon = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            TextView distText = (TextView) findViewById(R.id.distance);
            distText.setText("GPS not enabled");
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        Button locButton = (Button) this.findViewById(R.id.location);
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[] dist = new float[3];
                Location.distanceBetween(lat, lon, currLat, currLon, dist);
                TextView distText = (TextView) findViewById(R.id.distance);
                distText.setText(""+currLat+", "+currLon+": "+dist[0]);
            }
        });

        Button camera = (Button) this.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create Intent to take a picture and return control to the
                // calling application Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // create a file to save the image
                // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                // set the image file name
                // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // start the image capture Intent
                startActivityForResult(intent, 0);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        Tab1.myImgDB.insertHunt(More_info.this.nameOfHunt, HuntItems.itemList.get(pos).getName(), bp);
        iv.setImageBitmap(bp);
    }

}
