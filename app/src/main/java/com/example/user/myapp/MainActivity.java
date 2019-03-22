package com.example.user.myapp;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button sehirDetay;
    Button button;
    TextView text;
    TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    private static final int MY_PERMISSINIOSN_REQUEST_LOCATION = 1;


    public boolean internetKontrol() {
        ConnectivityManager cm =
                (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null;
        activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "İnternet bağlantısı: " + internetKontrol(), Toast.LENGTH_LONG).show();


        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView);
        t = (TextView) findViewById(R.id.textView2);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sehirDetay = (Button) findViewById(R.id.sehirDetay);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                t.append("\n " + location.getLongitude() + " " + location.getLatitude());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {


                Toast.makeText(MainActivity.this, "enable provider", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };


        sehirDetay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Detay.class);
                startActivity(intent);
            }
        });

        configure_button();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSINIOSN_REQUEST_LOCATION);

                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSINIOSN_REQUEST_LOCATION);

                    }
                } else {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        text.setText(hereLocation(location.getLatitude(), location.getLongitude()));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Notfound", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        locationManager.requestLocationUpdates("gps", 5000, 0, listener);
    }


    public String hereLocation(double lat, double lon) {
        String curCity = "";
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
            if (addressList.size() > 0) {
                curCity = addressList.get(0).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curCity;

    }


}