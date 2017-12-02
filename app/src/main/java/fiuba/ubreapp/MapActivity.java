package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**Mapa con la ubicacion. Opciones para ver perfil y editarlo.*/
public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MapActivity";

    private Gson gson;
    private User user;
    private Car car;
    private Card card;
    private String bundlejson, cardjson, carjson;
    private String type;
    private Intent intent;
    private String URL;
    private DriversPosition driverspositions;
    private ToastMessage tm;
    private Context context;
    DriversPosition dp;
    Geocoder geocoder;
    GoogleApiClient mGoogleApiClient;
    Location mylocation;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("Type");
        bundlejson = bundle.getString("User");

        gson = new Gson();

        user = gson.fromJson(bundlejson, User.class);

//        if(type == "Passenger"){
//            cardjson = bundle.getString("Card");
//            card = gson.fromJson(bundlejson,Card.class);
//        } else {
//            carjson = bundle.getString("Car");
//            car = gson.fromJson(bundlejson,Car.class);
//        }

        URL = bundle.getString("URL");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView username = (TextView) headerView.findViewById(R.id.textViewUsername);
        TextView email = (TextView) headerView.findViewById(R.id.textViewEmail);

        username.setText(user.getUsername());
        email.setText(user.getEmail());

        Menu menu = navigationView.getMenu();

        MenuItem typedata = menu.findItem(R.id.view_type_data);
        MenuItem changedata = menu.findItem(R.id.change_type_data);
        MenuItem trip = menu.findItem(R.id.trip);

        if (user.getType().equals("passenger")) {
            typedata.setTitle("View Card Data");
            changedata.setTitle("Change Card Data");
            trip.setTitle("Make a trip");
        } else {
            typedata.setTitle("View Car Data");
            changedata.setTitle("Change Car Data");
            trip.setTitle("Available trips");
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getApplicationContext();
        tm = new ToastMessage(context);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//
//        mGoogleApiClient.connect();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.trip){
            if(user.getType().equals("passenger"))
                intent = new Intent(MapActivity.this, TripSearchActivity.class);
            else
                intent = new Intent(MapActivity.this, AvailableTripActivity.class);
            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            startActivity(intent);
        }


        if (id == R.id.profile) {
            intent = new Intent(MapActivity.this, ViewProfile.class);
            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"View Profile");
            startActivity(intent);

        }

        if (id == R.id.change_data) {
            intent = new Intent(MapActivity.this, ModifyProfile.class);
            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"Modify Profile");
            startActivity(intent);

        }

        if (id == R.id.view_type_data) {
            if(user.getType().equals("passenger")){
                intent = new Intent(MapActivity.this, ViewCard.class);
                intent.putExtra("Card",cardjson);
            } else{
                intent = new Intent(MapActivity.this, ViewCar.class);
                intent.putExtra("Car",carjson);
            }

            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"View Information");
            startActivity(intent);

        }

        if (id == R.id.change_type_data) {
            if(user.getType().equals("passenger")){
                intent = new Intent(MapActivity.this, ModifyCard.class);
                intent.putExtra("Card",cardjson);
            } else{
                intent = new Intent(MapActivity.this, ModifyCar.class);
                intent.putExtra("Car",carjson);
            }
            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"Change Information");
            startActivity(intent);
        }

        if (id == R.id.change_password) {
            intent = new Intent(MapActivity.this, ChangePassword.class);
            bundlejson = gson.toJson(user);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"Change Password");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        List<LatLong> positions;
        mMap.setOnMyLocationButtonClickListener(this);
//        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
//
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

//            location = mMap.getMyLocation();



            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                sendLocation(location);
                                Log.i(TAG,"Latitude: "+String.valueOf(location.getLatitude()));
                                Log.i(TAG,"Longitud: "+String.valueOf(location.getLongitude()));
                                // Logic to handle location object
                            }
                        }
                    });


        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

//    @Override
//    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void sendLocation(Location location){
        Info url, infoanswer, info, infotoken;
        String endpoint;
        LatLong latlong;
        int status;
        PutRestApi put;

        url = new Info();
        infoanswer = new Info();
        info = new Info();
        infotoken = new Info();

        latlong = new LatLong(location.getLatitude(),location.getLongitude());

        put = new PutRestApi();

        if(type.equals("passenger"))
            endpoint = "/passengers/" + user.getId() + "/location";
        else
            endpoint = "/drivers/" + user.getId() + "/location";

        url.setInfo(URL+endpoint);
        infotoken.setInfo(user.getToken());
        info.setInfo(gson.toJson(latlong));

        try {
            put.execute(url,info,infoanswer,infotoken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        status = infoanswer.getStatus();
        Log.i(TAG, "Status: " + String.valueOf(status));
        switch (status) {
            case 200:
                break;
            case 400:
                tm.show(infoanswer.getInfo());
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
                tm.show(infoanswer.getInfo());
                break; //No existe recurso solicitado
            case 500:
                tm.show(infoanswer.getInfo());
                break; //Unexpected Error
            default:
                tm.show(infoanswer.getInfo());
                break;
        }

    }

    private void receiveLocationDrivers(){
        Info url, infoanswer, infotoken;
        String endpoint;
        int status;
        GetRestApi get;

        url = new Info();
        infoanswer = new Info();
        infotoken = new Info();

        get = new GetRestApi();

        endpoint = "/passengers/" + user.getId() + "/drivers";

        url.setInfo(URL+endpoint);
        infotoken.setInfo(user.getToken());

        try {
            get.execute(url,infoanswer,infotoken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        status = infoanswer.getStatus();
        Log.i(TAG, "Status: " + String.valueOf(status));
        switch (status) {
            case 200:
                driverspositions = gson.fromJson(infoanswer.getInfo(),DriversPosition.class);
                break;
            case 400:
                tm.show(infoanswer.getInfo());
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
                tm.show(infoanswer.getInfo());
                break; //No existe recurso solicitado
            case 500:
                tm.show(infoanswer.getInfo());
                break; //Unexpected Error
            default:
                tm.show(infoanswer.getInfo());
                break;
        }
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
