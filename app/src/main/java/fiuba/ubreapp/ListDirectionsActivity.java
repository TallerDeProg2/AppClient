package fiuba.ubreapp;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListDirectionsActivity extends ListActivity {

    private static String TAG = "List";
    Addresses adds;
    ListView listview;
    Gson gson;
    List<Address> addresses;
    ArrayAdapter<String> array;
    private Bundle bundle;
    String userjson,URL,cardjson,addressjson;
    Intent intent;
    User user;
    String type,payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_directions);

        intent = new Intent(ListDirectionsActivity.this,MapActivity.class); //Corregir

        listview = findViewById(android.R.id.list);

        bundle = getIntent().getExtras();

        addresses = new ArrayList<>();
        gson = new Gson();

        addressjson = bundle.getString("Addresses");
        userjson = bundle.getString("User");
        cardjson = bundle.getString("Card");
        URL = bundle.getString("URL");
        type = bundle.getString("Type");
//        payment = bundle.getString("Payment");
        user = gson.fromJson(userjson,User.class);

        Log.i(TAG,addressjson);

        adds = gson.fromJson(addressjson,Addresses.class);

        addresses = adds.getAddresses();

        array = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        for(Address address: addresses){
            array.add(address.getAddressLine(0));
        }

        listview.setAdapter(array);

        listview.setClickable(true);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                setProgressBarIndeterminateVisibility(true);

                sendTripPosition(addresses.get(position).getLatitude(),addresses.get(position).getLongitude());
            }
        });
    }

    private void sendTripPosition(double lat, double lng){

        Info url = new Info();
        Info latlonginfo = new Info();
        Info answer = new Info();
        Info token = new Info();
        PostRestApi post = new PostRestApi();
        LatLong latlong = new LatLong(lat,lng);
        String endpoint = "/passengers/" + user.getId() + "/directions";
        int status;

        latlonginfo.setInfo(gson.toJson(latlong));
        token.setInfo(user.getToken());
        url.setInfo(URL+endpoint);

        try {
            post.execute(url,latlonginfo,answer,token).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        status = answer.getStatus();

        Log.i(TAG,String.valueOf(status));

        switch (status) {
            case 201:
//                intent = new Intent(ListDirectionsActivity.this,MapTripActivity.class);
                intent = new Intent(ListDirectionsActivity.this,PayMethodSelectActivity.class);
                intent.putExtra("User",userjson);
                intent.putExtra("Card",cardjson);
                intent.putExtra("URL",URL);
                intent.putExtra("Routes",answer.getInfo());
//                intent.putExtra("Payment",payment);
                intent.putExtra("Type",type);
                Log.i(TAG,"Direction OK");
                startActivity(intent);
                break;
            case 400:
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 404:
                break; //No existe recurso solicitado
            case 500:
                break; //Unexpected Error
            default:
                break;
        }
    }
}
