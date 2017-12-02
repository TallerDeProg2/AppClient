package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    Card card;
    Card car;
    String URL, userjson, cardjson,carjson, routejson,type;
    Bundle bundle;
    Intent intent;
    ParserDirections parser;
    List<List<LatLng>> routes;
    Polyline polyline;
    int i,routeselected;
    ToastMessage tm;
    Context context;
    Button accept;
    Boolean start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_do_trip);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        type = bundle.getString("Type");
        if(type.equals("passenger"))
            cardjson = bundle.getString("Card");
        else
            carjson = bundle.getString("Car");

        URL = bundle.getString("URL");
        routejson = bundle.getString("Route");

        user = gson.fromJson(userjson,User.class);

        accept = findViewById(R.id.button14);

        accept.setOnClickListener(this);

        accept.setText("Start");

        accept.setEnabled(false);

        parser = new ParserDirections(routejson);
        routes = parser.getListroutes();

        polyline = mMap.addPolyline(new PolylineOptions());
        polyline.setPoints(routes.get(0));
        polyline.setPattern(PATTERN_POLYLINE_DOTTED);

        context = getApplicationContext();
        tm = new ToastMessage(context);

        start = false;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button14){

            if(!start){
                sendStartTrip();
                accept.setEnabled(false);
            } else {
                sendEndTrip();
            }
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

        Location mylocation = mMap.getMyLocation();

        size = routes.get(0).size();
        destpos = routes.get(0).get(size-1);
        oripos = routes.get(0).get(0);

        if(type.equals("driver")){
            dpos = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
            if(dpos.equals(oripos)){
                accept.setEnabled(true);
                accept.setText("Start");
            }

            if(dpos.equals(destpos)){
                accept.setEnabled(true);
                accept.setText("End");
            }
        } else {
            ppos = new LatLng(mylocation.getLatitude(),mylocation.getLongitude());
            if(ppos.equals(destpos)){
                intent = new Intent(MapDoTripActivity.this,MapActivity.class);
                intent.putExtra("URL",URL);
                intent.putExtra("User",userjson);
                intent.putExtra("Card",cardjson);
                intent.putExtra("Type",type);
                paytrip();
                startActivity(intent);
            }
        }
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

    }
    private void sendEndTrip(){}


    private void paytrip(){}
}
