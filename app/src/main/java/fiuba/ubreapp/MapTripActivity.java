package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

public class MapTripActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,GoogleMap.OnMyLocationButtonClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final PatternItem DOT = new Dot();
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final PatternItem GAP = new Gap(POLYLINE_STROKE_WIDTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    private static String TAG = "MapTripActivity";

    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    Gson gson;
    User user;
    Card card;
    String URL, userjson, cardjson, roadsjson,type,payment;
    Bundle bundle;
    Intent intent;
    ParserDirections parser;
    List<List<LatLng>> routes;
    List<Polyline> polylines;
    Polyline polyline;
    int i,routeselected;
    ToastMessage tm;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_trip);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        cardjson = bundle.getString("Card");
        URL = bundle.getString("URL");
        roadsjson = bundle.getString("Routes");
        type = bundle.getString("Type");
        payment = bundle.getString("Payment");

        user = gson.fromJson(userjson,User.class);

        Button accept = findViewById(R.id.button8);
        Button cancel = findViewById(R.id.button9);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        parser = new ParserDirections(roadsjson);
        routes = parser.getListroutes();

        polylines = new ArrayList<>();

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View view) {
        String cost,route,endpoint;
        Info inforoute,url,infoanswer,infotoken;
        PostRestApi post;
        int status;
        PaymentRoute pr;

        endpoint = "/trips/estimate";

        if(view.getId() == R.id.button9){
            intent = new Intent(MapTripActivity.this,MapActivity.class);
            intent.putExtra("User",userjson);
            intent.putExtra("Card",cardjson);
            intent.putExtra("Type",type);
            intent.putExtra("URL",URL);
            startActivity(intent);
        }

        if(view.getId() == R.id.button8){

            infoanswer = new Info();
            inforoute = new Info();
            url = new Info();
            infotoken = new Info();
            post = new PostRestApi();

            route = parser.getSelectedRoute(routeselected);

            pr = new PaymentRoute(payment,route);

            inforoute.setInfo(gson.toJson(pr));

            url.setInfo(URL + endpoint);
            infotoken.setInfo(user.getToken());

            try {
                post.execute(url,inforoute,infoanswer,infotoken).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status = infoanswer.getStatus();

            Log.i(TAG,"Status Post: "+status);

            switch (status) {
                case 201:
                    cost = infoanswer.getInfo();
                    intent = new Intent(MapTripActivity.this,TripCostActivity.class);
                    intent.putExtra("User",userjson);
                    intent.putExtra("Card",cardjson);
                    intent.putExtra("Type",type);
                    intent.putExtra("Cost",cost);
                    intent.putExtra("Route",route);
                    intent.putExtra("PR",inforoute.getInfo());
                    startActivity(intent);
                    break;
                case 400:
                    tm.show(infoanswer.getInfo());
                    break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                case 401:
                    tm.show(infoanswer.getInfo());
                    break; //Unauthorized
                case 500:
                    tm.show(infoanswer.getInfo());
                    break; //Unexpected Error
            }


        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        if (polyline.getPattern().contains(DOT)){
            routeselected = (int) polyline.getTag();
            for(Polyline pol:polylines){
                if((int)pol.getTag() != routeselected)
                    pol.setPattern(PATTERN_POLYLINE_DOTTED);
                else
                    pol.setPattern(null);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true));

        i = 0;
        for(List<LatLng> route:routes){
            polylines.add(mMap.addPolyline(new PolylineOptions()
                    .clickable(true)));
//            polyline = mMap.addPolyline(new PolylineOptions()
//                    .clickable(true));
            polylines.get(i).setPoints(route);
            polylines.get(i).setPattern(PATTERN_POLYLINE_DOTTED);
//            polylines.add(polyline);
            polylines.get(i).setTag(i);
            i++;
        }

        routeselected = 0;
        polylines.get(routeselected).setPattern(null);
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
