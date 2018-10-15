package coolgroup.com.aline.Maps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.IOException;
import java.util.List;

import coolgroup.com.aline.R;
import coolgroup.com.aline.view.AuthenticateActivity;
import coolgroup.com.aline.view.ChatsActivity;

public class NewHomepageActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    List<Address> addressList;
    Address destinationAddress;
    double end_latitude;
    double end_longitude;
    LatLng myLocation;

    private GoogleMap mMap;
    private GoogleApiClient client;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
            mUserReference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(mAuth.getCurrentUser().getUid());

        setContentView(R.layout.new_activity_homepage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkLocationPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // User is not signed in
        if (mAuth.getCurrentUser() == null)
            backToAuth();
        else
            mUserReference.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuth.getCurrentUser() != null)
            mUserReference.child("online").setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
        case REQUEST_LOCATION_CODE:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (client == null)
                    buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker args) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker args) {
                TextView title = (TextView) findViewById(R.id.edtMapSearch);
                title.setText(args.getTitle());
                return null;
            }
        });

        float dp = 112f;
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        mMap.setPadding(0, (int) px, 0, 0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4.0f));
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;
        myLocation = new LatLng(longitude, latitude);

        if (currentLocationMarker != null)
            currentLocationMarker.remove();

        Log.d("lat = ", String.valueOf(latitude));
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        switch (v.getId()) {
        case R.id.btnMapSearch:
            EditText tf_location = (EditText) findViewById(R.id.edtMapSearch);
            String location = tf_location.getText().toString();
            List<Address> addressList;

            if (!location.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 5);

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            LatLng latLng = new LatLng(addressList.get(i).getLatitude(),
                                    addressList.get(i).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(location);

                            mMap.clear();
                            mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;

        case R.id.btnParks:
            mMap.clear();
            String url = getUrl(latitude, longitude, "park");
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;

            getNearbyPlacesData.execute(dataTransfer);
            Toast.makeText(NewHomepageActivity.this, "Showing nearby parks",
                    Toast.LENGTH_SHORT).show();
            break;


        case R.id.btnCafes:
            mMap.clear();
            url = getUrl(latitude, longitude, "park");
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;

            getNearbyPlacesData.execute(dataTransfer);
            Toast.makeText(NewHomepageActivity.this, "Showing nearby parks",
                    Toast.LENGTH_SHORT).show();
            break;

        case R.id.btnShops:
            mMap.clear();
            url = getUrl(latitude, longitude, "shops");
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;

            getNearbyPlacesData.execute(dataTransfer);
            Toast.makeText(NewHomepageActivity.this, "Showing Nearby Restaurants",
                    Toast.LENGTH_SHORT).show();
            break;
        }
    }

    public void getDirection(View view) {
        EditText destination = (EditText)findViewById(R.id.edtMapSearch);
        String destinationAddressString = destination.getText().toString();
        Log.d("location = ", destinationAddressString);

        if (!destinationAddressString.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(destinationAddressString, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList != null) {
                destinationAddress = addressList.get(0);
                end_latitude = destinationAddress.getLatitude();
                end_longitude = destinationAddress.getLongitude();
                myLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
        }
        Object dataTransfer[];
        dataTransfer = new Object[3];
        String url = getDirectionsUrl();
        Log.i("URL",url);
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        mMap.clear();
        LatLng location = new LatLng(end_latitude, end_longitude);
        dataTransfer[2] = location;

        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .anchor(0.5f, 0.5f)
                .title("My Current Location")
                .snippet("Snippet1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(location)
                .anchor(0.5f, 0.5f)
                .title("Destination")
                .snippet("Snippet2")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

        getDirectionsData.execute(dataTransfer);
    }

    private String getDirectionsUrl() {
        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=" +
                latitude + "," + longitude + "&destination=" + end_latitude + "," + end_longitude +
                "&key=" + "AIzaSyAxdy52TGsv0WZOTG0veLdvlZSv3JhwJic";
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlaceUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=").append(nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + "AIzaSyAFJ7pmLNiMVJYalkfF-djKfaeXspVq-tQ");

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            return false;
        } else {
            return true;
        }
    }

    public void startChat(View view) {
        Intent chatIntent = new Intent(NewHomepageActivity.this, ChatsActivity.class);

        // If email and password is authenticated open the welcome layout
        startActivity(chatIntent);
    }

    public void startSOS(View view) {
        // Go to SOS Activity
    }

    public void startTrack(View view) {
        // Start Tracking
    }

    // Send user to Authentication page
    private void backToAuth() {
        Intent authIntent = new Intent(NewHomepageActivity.this, AuthenticateActivity.class);

        // Validation to stop user from going to the authenticate activity again
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Take user to authentication
        startActivity(authIntent);
        finish();
    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
