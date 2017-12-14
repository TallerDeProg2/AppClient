package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapDoTripActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final PatternItem DOT = new Dot();
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final PatternItem GAP = new Gap(POLYLINE_STROKE_WIDTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    private static String TAG = "MapDoTripActivity";

    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    Gson gson;
    User user;
    String URL, userjson, cardjson,carjson, routejson,type,idtrip,otheruser;
    Bundle bundle;
    Intent intent;
    ParserDirections parser;
    List<List<LatLng>> routes;
    Polyline polyline;
    int i,routeselected;
    ToastMessage tm;
    Context context;
    Button accept,chat;
    Boolean start;
    Singleton singleton;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_do_trip);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        type = bundle.getString("Type");
        cardjson = bundle.getString("Card");
        if(type.equals("driver"))
            carjson = bundle.getString("Car");

        URL = bundle.getString("URL");
        routejson = bundle.getString("Route");

        idtrip = bundle.getString("idtrip");

        user = gson.fromJson(userjson,User.class);

        otheruser = bundle.getString("OtherUser");

//        fab = findViewById(R.id.floatingActionButton);
        accept = findViewById(R.id.button14);

        accept.setOnClickListener(this);

//        chat = findViewById(R.id.button21);

        accept.setText("Start");

        if(type.equals("drivers")){
            accept.setEnabled(false);
        }

        routejson = "{\"routes\":["+routejson+"]}";

        parser = new ParserDirections(routejson);
        routes = parser.getListroutes();
        Log.i(TAG,routejson);
        Log.i(TAG,"size routes: "+routes.size());

        context = getApplicationContext();
        tm = new ToastMessage(context);

        start = false;

        singleton = Singleton.getInstance();

        singleton.setUser(userjson);
        singleton.setCar(carjson);
        singleton.setCard(cardjson);
        singleton.setUrl(URL);
        singleton.setType(type);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button14){

            if(!start){
                sendStartTrip();
            } else {
                sendEndTrip();
            }

//            if(type.equals("passenger"))
//
//            else{
//                confirmTrip();
//                accept.setText("END");
//                endTrip();
//            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng ppos,dpos,oripos,destpos;
        int size;
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        polyline = mMap.addPolyline(new PolylineOptions());
        polyline.setPoints(routes.get(0));
        polyline.setPattern(PATTERN_POLYLINE_DOTTED);

//        size = routes.get(0).size();
//        destpos = routes.get(0).get(size-1);
//        oripos = routes.get(0).get(0);
//
//        if(type.equals("driver")){
//            dpos = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
//            if(dpos.equals(oripos)){
//                accept.setEnabled(true);
//                accept.setText("Start");
//            }
//
//            if(dpos.equals(destpos)){
//                accept.setEnabled(true);
//                accept.setText("End");
//            }
//        } else {
//            ppos = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
//            if(ppos.equals(destpos)){
//                intent = new Intent(MapDoTripActivity.this,MapActivity.class);
//                intent.putExtra("URL",URL);
//                intent.putExtra("User",userjson);
//                intent.putExtra("Card",cardjson);
//                intent.putExtra("Type",type);
//                paytrip();
//                startActivity(intent);
//            }
//        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private void sendStartTrip(){
        String endpoint = "/trips/" + idtrip +"/start";
        Info url,info,answer,token;
        int status;
        PostRestApi post = new PostRestApi();
        url = new Info();
        info = new Info();
        answer = new Info();
        token = new Info();

        url.setInfo(URL + endpoint);
        token.setInfo(user.getToken());

        try {
            post.execute(url,info,answer,token).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        status = answer.getStatus();

        switch (status) {
            case 201:
                accept.setText("End");
                start = true;
                break;
            case 400:
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
                break; //No existe recurso solicitado
            case 500:
                break; //Unexpected Error
        }
    }
    private void sendEndTrip(){
        intent = new Intent(MapDoTripActivity.this,PayTripActivity.class);
        intent.putExtra("User",userjson);
        intent.putExtra("Type",type);
        intent.putExtra("URL",URL);
        intent.putExtra("Card",cardjson);
        intent.putExtra("idtrip",idtrip);
        startActivity(intent);
    }

//    private void confirmTrip() {
//        String endpoint;
//        Info url, info, answer, token;
//        PostRestApi post;
//        int status;
//
//        url = new Info();
//        info = new Info();
//        answer = new Info();
//        token = new Info();
//        post = new PostRestApi();
//
//        endpoint = "/drivers/" + user.getId() + "/trip/confirmation";
//
//        url.setInfo(URL + endpoint);
//        info.setInfo("{\"trip_id\":" + idtrip + "}");
//
//        Log.i(TAG,info.getInfo());
//
//        token.setInfo(user.getToken());
//
//        try {
//            post.execute(url, info, answer, token).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        status = answer.getStatus();
//        Log.i(TAG, "Status: " + String.valueOf(status));
//        Log.i(TAG, "Mensaje: " + answer.getInfo());
//        switch (status) {
//            case 201:
//
//                break;
//            case 400:
//                tm.show(answer.getInfo());
//                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
//            case 404:
//                tm.show(answer.getInfo());
//                break; //No existe recurso solicitado
//            case 500:
//                tm.show(answer.getInfo());
//                break; //Unexpected Error
//            default:
//                tm.show(answer.getInfo());
//                break;
//        }
//    }
//
//    private void endTrip(){
//        intent = new Intent(MapDoTripActivity.this,MapActivity.class);
//        intent.putExtra("User",userjson);
//        intent.putExtra("Type",type);
//        intent.putExtra("URL",URL);
//        intent.putExtra("Card",cardjson);
//        startActivity(intent);
//    }


}