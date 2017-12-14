package fiuba.ubreapp;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.List;

public class AvailableTripActivity extends AppCompatActivity {

    private static String TAG = "AvailableTrip";
    private ListView listview;
    private Gson gson;
    private ArrayAdapter<String> array;
    private ParserTrips parser;
    private ParserDirections pd;
    private Bundle bundle;
    private String userjson,URL,carjson,addressjson,tripsjson,type;
    private Intent intent;
    private User user,passenger;
    private List<String> passengers,trips,ids;
    private String resumen;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_trip);

        listview = findViewById(android.R.id.list);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        URL = bundle.getString("URL");
        carjson = bundle.getString("Car");
        tripsjson = bundle.getString("Trips");
        type = bundle.getString("Type");

        user = gson.fromJson(userjson,User.class);

        parser = new ParserTrips(tripsjson);

        passengers = parser.getPassengers();
        trips = parser.getTrips();
        ids = parser.getIds();

        array = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        for(i = 0; i < trips.size(); i++){
            passenger = gson.fromJson(passengers.get(i),User.class);
            pd = new ParserDirections("{\"routes\":["+trips.get(i)+"]}");
            resumen = "Passenger: " + passenger.getFirstname() + " " + passenger.getLastName() +".\n";
            resumen = resumen + "Origin: " + pd.getStartAddress(0) +".\n";
            resumen = resumen + "Destination: " + pd.getEndAddress(0) + ".\n";
            array.add(resumen);
        }


        if(trips.size() == 0){
            intent = new Intent(AvailableTripActivity.this,MapActivity.class);
            intent.putExtra("User",userjson);
            intent.putExtra("Car",carjson);
            intent.putExtra("URL",URL);
            intent.putExtra("Type",type);
            startActivity(intent);
        } else {
            listview.setAdapter(array);

            listview.setClickable(true);

            intent = new Intent(AvailableTripActivity.this,TripInfoActivity.class);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int position, long arg) {

                    setProgressBarIndeterminateVisibility(true);

                    intent.putExtra("User",userjson);
                    intent.putExtra("Car",carjson);
                    intent.putExtra("URL",URL);
                    intent.putExtra("Type",type);
                    intent.putExtra("Route",tripsjson);
                    intent.putExtra("Passenger",passengers.get(position));
                    intent.putExtra("Trip",trips.get(position));
                    intent.putExtra("idtrip",ids.get(position));
                    startActivity(intent);
                }
            });
        }
    }
}
