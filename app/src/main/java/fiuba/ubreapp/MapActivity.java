package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private String URL,driverspositions;
    private ToastMessage tm;
    private Context context;
    Geocoder geocoder;
    GoogleApiClient mGoogleApiClient;
    Location mylocation;
    Boolean sendloc;
    Thread timerTread;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

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

        cardjson = bundle.getString("Card");

        if(type.equals("driver"))
            carjson = bundle.getString("Car");

        URL = bundle.getString("URL");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView username = headerView.findViewById(R.id.textViewUsername);
        TextView email = headerView.findViewById(R.id.textViewEmail);

        username.setText(user.getUsername());
        email.setText(user.getEmail());

        Menu menu = navigationView.getMenu();

        MenuItem changecar = menu.findItem(R.id.change_car);
        MenuItem trip = menu.findItem(R.id.trip);



        if (type.equals("passenger")) {
            changecar.setVisible(false);
            trip.setTitle("Realizar viaje");
        } else {
            trip.setTitle("Viajes disponibles");

        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getApplicationContext();
        tm = new ToastMessage(context);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sendloc = true;



        FirebaseMessaging.getInstance().subscribeToTopic(user.getId());
        Log.i(TAG,user.getId());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String trips = "";
        if (id == R.id.trip){
            if(type.equals("passenger")){
                intent = new Intent(MapActivity.this, TripSearchActivity.class);
                intent.putExtra("User", bundlejson);
                intent.putExtra("URL",URL);
                intent.putExtra("Type",type);
                intent.putExtra("Card",cardjson);
                startActivity(intent);
            } else {
                intent = new Intent(MapActivity.this, AvailableTripActivity.class);
                String endpoint;
                endpoint = "/drivers/"+user.getId()+"/trips";
                requestAvailableTrips(URL+endpoint,user.getToken());
            }

        }

        if (id == R.id.historial){
            intent = new Intent(MapActivity.this, HistorialActivity.class);
            String endpoint;
            if(type.equals("passenger"))
                endpoint ="/passengers/"+ user.getId() + "/trips/history";
            else
                endpoint ="/drivers/"+ user.getId() + "/trips/history";
            requestHistorial(URL+endpoint,user.getToken());
        }

        if (id == R.id.profile) {
            intent = new Intent(MapActivity.this, ViewProfile.class);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"View Profile");
            startActivity(intent);

        }

        if (id == R.id.change_data) {
            intent = new Intent(MapActivity.this, ModifyProfile.class);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            intent.putExtra("Card",cardjson);
            if(type.equals("driver"))
                intent.putExtra("Car",carjson);
            Log.i(TAG,"Modify Profile");
            startActivity(intent);

        }

        if (id == R.id.change_card) {
            intent = new Intent(MapActivity.this, DataCard.class);
            intent.putExtra("Card",cardjson);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            if(type.equals("driver"))
                intent.putExtra("Car",carjson);
            Log.i(TAG,"View Card Information");
            timerTread.interrupt();
            try {
                timerTread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }

        if (id == R.id.change_car) {
            intent = new Intent(MapActivity.this, DataCar.class);
            intent.putExtra("Card",cardjson);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            intent.putExtra("Car",carjson);
            Log.i(TAG,"View Car Information");
            startActivity(intent);
        }

        if (id == R.id.change_password) {
            intent = new Intent(MapActivity.this, ChangePassword.class);
            intent.putExtra("User", bundlejson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            Log.i(TAG,"Change Password");
            startActivity(intent);
        }

        if (id == R.id.logout) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.getId());
            String endpoint = "/users/"+user.getId()+"/logout";
            intent = new Intent(MapActivity.this, LoginActivity.class);
            sendloc = false;
            if(type.equals("passenger")){
                Log.i(TAG,"Log Out");
                startActivity(intent);
            } else {
                try {
                    timerTread.join();
                    logout(URL+endpoint,user.getToken());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);

            timerTread = new Thread(){
                public void run(){
                    try{
                        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        while(sendloc){
                            mFusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(MapActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                sendLocation(location);
                                                try {
                                                    sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                if(type.equals("passenger")){
                                                    String endpoint = "/passengers/" + user.getId() + "/drivers";
                                                    requestDrivers(URL+endpoint,user.getToken());
                                                }
                                            }
                                        }
                                    });
                            sleep(15000);
                        }

                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timerTread.start();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

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
        Log.i(TAG, "Status Send Location: " + String.valueOf(status));
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

    private void requestDrivers(String url, final String token){
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        addDrivers(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,error.getMessage());


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG,"Status code: "+response.statusCode);
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }

    private void addDrivers(String driverspositions){
        ParserDrivers pd = new ParserDrivers(driverspositions);
        List<LatLng> pos = pd.getListlocation();
        List<String> drivers = pd.getListdriver();
        User driver;
        int i;
        if (pos != null){
            mMap.clear();
            for(i = 0; i < pos.size(); i++){
                driver = gson.fromJson(drivers.get(i),User.class);
                mMap.addMarker(new MarkerOptions().position(pos.get(i)).title("Driver").snippet(driver.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    private void requestAvailableTrips(String url, final String token){
        final Info info = new Info();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        startActAvailable(info.getInfo(),info.getStatus());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG,error.getMessage());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG,"Status code: "+response.statusCode);
                info.setStatus(response.statusCode);
                info.setInfo(new String(response.data));
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }

    private void startActAvailable(String request,int status){

        switch (status) {
            case 200:
                intent.putExtra("Trips",request);
                intent.putExtra("User", bundlejson);
                intent.putExtra("URL",URL);
                intent.putExtra("Type",type);
                intent.putExtra("Card",cardjson);
                intent.putExtra("Car",carjson);

                startActivity(intent);
                break;
            case 400:
//                tm.show(infoanswer.getInfo());
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
//                tm.show(infoanswer.getInfo());
                break; //No existe recurso solicitado
            case 500:
//                tm.show(infoanswer.getInfo());
                break; //Unexpected Error
            default:
//                tm.show(infoanswer.getInfo());
                break;
        }
    }

    private void requestHistorial(String url, final String token){
        final Info info = new Info();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,response.toString());
                        startHistorial(info.getInfo(),info.getStatus());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG,error.getMessage());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG,"Status code: "+response.statusCode);
                info.setStatus(response.statusCode);
                info.setInfo(new String(response.data));
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }

    private void startHistorial(String request,int status){

        switch (status) {
            case 200:
                intent.putExtra("Trips",request);
                intent.putExtra("User", bundlejson);
                intent.putExtra("URL",URL);
                intent.putExtra("Type",type);
                intent.putExtra("Card",cardjson);
                intent.putExtra("Car",carjson);

                startActivity(intent);
                break;
            case 400:
//                tm.show(infoanswer.getInfo());
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
//                tm.show(infoanswer.getInfo());
                break; //No existe recurso solicitado
            case 500:
//                tm.show(infoanswer.getInfo());
                break; //Unexpected Error
            default:
//                tm.show(infoanswer.getInfo());
                break;
        }
    }

    private void logout(String url,final String token){
        final Info info = new Info();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        startLogOut(info.getStatus());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG,error.getMessage());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG,"Status code LogOut: "+response.statusCode);
                info.setStatus(response.statusCode);
                startLogOut(info.getStatus());
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }

    private void startLogOut(int status){

        switch (status) {
            case 201:
                Log.i(TAG,"LogOut");
                startActivity(intent);
                break;
            case 400:
//                tm.show(infoanswer.getInfo());
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
//                tm.show(infoanswer.getInfo());
                break; //No existe recurso solicitado
            case 500:
//                tm.show(infoanswer.getInfo());
                break; //Unexpected Error
            default:
//                tm.show(infoanswer.getInfo());
                break;
        }
    }



}
