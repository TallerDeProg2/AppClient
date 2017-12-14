package fiuba.ubreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TripCostActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "Trip Cost";

    Gson gson;
    Intent intent;
    Bundle bundle;
    String userjson,cardjson,URL,type,cost,route,pr;
    User user;
    ToastMessage tm;
    Context context;
    String idtrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_cost);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        cardjson = bundle.getString("Card");
        URL = bundle.getString("URL");
        type = bundle.getString("Type");
        cost = bundle.getString("Cost");
        route = bundle.getString("Route");
        pr = bundle.getString("PR");

        user = gson.fromJson(userjson,User.class);

        TextView text = findViewById(R.id.textView27);
        EditText editcost = findViewById(R.id.editText32);
        Button accept = findViewById(R.id.button13);
        Button cancel = findViewById(R.id.button12);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        text.setText("Would you like to realize this trip?");

        try {
            JSONObject obj = new JSONObject(cost);
            JSONObject obj2 = obj.getJSONObject("cost");
            cost = obj2.getString("value") + " " + obj2.getString("currency");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editcost.setText(cost);
        editcost.setFocusable(false);

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View view) {

        Info url,infojson,infoanswer,infotoken;
        String endpoint;
//        int status;

        url = new Info();
        infoanswer = new Info();
        infojson = new Info();
        infotoken = new Info();

        endpoint = "/passengers/"+user.getId()+"/trips/request";

        if (view.getId() == R.id.button13){
            infotoken.setInfo(user.getToken());
            url.setInfo(URL+endpoint);
            infojson.setInfo(pr);

            try {
                Request(URL+endpoint,new JSONObject(pr),user.getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void Request(String url, final JSONObject mRequestBody, final String token){
        final Info info = new Info();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,mRequestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            idtrip = response.getString("trip_id");
                            Log.i(TAG,"IDTRIP: "+ idtrip);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startAct(info.getStatus(),mRequestBody,idtrip);
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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.i(TAG, "Status code: " + response.statusCode);
                info.setInfo(new String(response.data));
                info.setStatus(response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(postRequest);
    }


    private void startAct(int status,JSONObject mR,String id){
        String aux;
        Singleton singleton = Singleton.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(TripCostActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        switch (status) {
            case 201:
                singleton.setUser(userjson);
                singleton.setCar(cardjson);
                singleton.setUrl(URL);
                singleton.setType(type);
                singleton.setIdtrip(idtrip);

//                intent = new Intent(TripCostActivity.this,MapDoTripActivity.class);
//                intent.putExtra("User",userjson);
//                intent.putExtra("Card",cardjson);
//                intent.putExtra("Type",type);
//                intent.putExtra("idtrip",idtrip);
//                intent.putExtra("URL",URL);
                try {
                    aux = mR.getJSONObject("trip").toString();
//                    intent.putExtra("Route",aux);
                    singleton.setRoute(aux);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Waiting for driver...");
                progressDialog.show();

//                startActivity(intent);
                break;
            case 400:
                tm.show("Error 400");
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 401:
                tm.show("Error 401");
                break; //Unauthorized
            case 500:
                tm.show("Error 500");
                break; //Unexpected Error
            default:
                break;
        }
    }
}
