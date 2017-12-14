package fiuba.ubreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.http.RequestQueue;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    String info;
    int i,routeselected;
    ToastMessage tm;
    Context context;
    public Info infoanswer,inforoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_trip);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        infoanswer = new Info();

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
        Log.i(TAG,"Size routes: " + String.valueOf(routes.size()));
        for(int j = 0; j < routes.size(); j++)
            Log.i(TAG,"Size locations: " + String.valueOf(routes.get(j).size()));

        polylines = new ArrayList<>();

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View view) {
        String endpoint;
        Info url,infotoken;

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

            inforoute = new Info();
            url = new Info();
            infotoken = new Info();

            JSONObject o = parser.getSelectedRoute(routeselected);

            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put("paymethod",payment);
                jsonObj.put("trip",o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            info = jsonObj.toString();


            Log.e(TAG,info);

            inforoute.setInfo(info);

            url.setInfo(URL + endpoint);
            infotoken.setInfo(user.getToken());

            Request(URL+endpoint,jsonObj,user.getToken());

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

        routeselected = (int) polyline.getTag();
        for(Polyline pol:polylines) {
            if ((int) pol.getTag() != routeselected)
                pol.setPattern(PATTERN_POLYLINE_DOTTED);
            else
                pol.setPattern(null);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        i = 0;
        for(List<LatLng> route:routes){

            polylines.add(googleMap.addPolyline(new PolylineOptions()
                    .clickable(true).visible(true)));
            polylines.get(i).setPoints(route);
            polylines.get(i).setPattern(PATTERN_POLYLINE_DOTTED);
            polylines.get(i).setTag(i);
            i++;
        }

        routeselected = 0;
        polylines.get(routeselected).setPattern(null);

        googleMap.setOnPolylineClickListener(this);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    private void Request(String url,final JSONObject mRequestBody,final String token){

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,mRequestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        startAct(response.toString(),201,mRequestBody);
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void startAct(String cost,int status,JSONObject mR){
        switch (status) {
            case 201:
                intent = new Intent(MapTripActivity.this,TripCostActivity.class);
                intent.putExtra("URL",URL);
                intent.putExtra("User",userjson);
                intent.putExtra("Card",cardjson);
                intent.putExtra("Type",type);
                intent.putExtra("Cost",cost);
//                intent.putExtra("Route",info);
                intent.putExtra("PR",mR.toString());
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
