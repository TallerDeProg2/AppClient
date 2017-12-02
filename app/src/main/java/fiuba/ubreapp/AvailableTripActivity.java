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
    private TripList triplist;
    private List<Trip> trips;
    private ArrayAdapter<String> array;
    private Bundle bundle;
    private String userjson,URL,carjson,addressjson,tripsjson,type;
    private Intent intent;
    private User user;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_trip);

        gson = new Gson();

        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        URL = bundle.getString("URL");
        carjson = bundle.getString("Car");
        tripsjson = bundle.getString("Trips");
        type = bundle.getString("Type");

        user = gson.fromJson(userjson,User.class);

        triplist = gson.fromJson(tripsjson,TripList.class);

        trips = triplist.getTrips();

        array = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        for(i = 0; i < trips.size(); i++){
//            array.add(trips.get(i).);
        }

        listview.setAdapter(array);

        listview.setClickable(true);

        intent = new Intent(AvailableTripActivity.this,MapDoTripActivity.class);

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
                startActivity(intent);
            }
        });
    }
}
