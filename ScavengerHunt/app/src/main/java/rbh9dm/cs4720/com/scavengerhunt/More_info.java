package rbh9dm.cs4720.com.scavengerhunt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class More_info extends AppCompatActivity {

    private double lat = 50;
    private double lon = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        int pos = intent.getIntExtra(HuntItems.POSITION, 0);
        getSupportActionBar().setTitle(HuntItems.itemList.get(pos).getName());

        TextView description = (TextView) findViewById(R.id.Mdescription);
        description.setText(HuntItems.itemList.get(pos).getDescription());

        TextView location = (TextView) findViewById(R.id.Mlocation);
        location.setText(HuntItems.itemList.get(pos).getNameOfLocation());

        CheckBox chk = (CheckBox) findViewById(R.id.chkcamera);

        if (HuntItems.itemList.get(pos).isPictureRequired())
            chk.setText("Required");
        chk.setChecked(HuntItems.itemList.get(pos).isPictureOk());
        chk = (CheckBox) findViewById(R.id.chklocation);

        if (HuntItems.itemList.get(pos).isLocationRequired())
            chk.setText("Required");
        chk.setChecked(HuntItems.itemList.get(pos).isLocationOk());

        Button locButton = (Button) this.findViewById(R.id.location);
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) More_info.this.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(More_info.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    TextView distText = (TextView) findViewById(R.id.distance);
                    distText.setText("GPS not enabled");
                }
                else {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    TextView distText = (TextView) findViewById(R.id.distance);
                    float[] dist = new float[100];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), lat, lon, dist);
                    distText.setText(""+dist[0]);
                }
            }
        });
    }



}
