package fiuba.ubreapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TripSearchActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = "Search";

    Button button;
    AutoCompleteTextView in;
    static ArrayAdapter<String> places;
    static Geocoder geocoder;
    String URL,type;
    Bundle bundle;
    User user;
    Card card;
    String userjson,cardjson;
    Gson gson;
    RadioButton cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());

        button = findViewById(R.id.button11);
        in = findViewById(R.id.autoCompleteTextView);

        button.setOnClickListener(this);

        gson = new Gson();
        bundle = getIntent().getExtras();

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        cardjson = bundle.getString("Card");
        card = gson.fromJson(cardjson,Card.class);

        URL = bundle.getString("URL");
        type = bundle.getString("Type");

        cash = findViewById(R.id.radioButton13);

        cash.toggle();

    }

    @Override
    public void onClick(View view) {

        List<Address> addresses;

        String aux;

        Intent intent;

        Addresses adds;

        if (view.getId() == R.id.button11) {

            addresses = getAddressFromLocation(in.getText().toString());
            if (addresses != null) {
                intent = new Intent(TripSearchActivity.this, ListDirectionsActivity.class);
                adds = new Addresses();
                adds.setAddresses(addresses);
                aux = gson.toJson(adds);
                Log.i(TAG, aux);
                intent.putExtra("Addresses", aux);
                intent.putExtra("URL",URL);
                intent.putExtra("User",userjson);
                intent.putExtra("Card",cardjson);
                intent.putExtra("Type",type);
                if(cash.isChecked())
                    intent.putExtra("Payment","cash");
                else
                    intent.putExtra("Payment","card");

                startActivity(intent);
            }

        }
    }

    public static List<Address> getAddressFromLocation(final String location){

        try {
            List<Address> list = geocoder.getFromLocationName(location, 10);
            if (list != null && list.size() > 0) {
                return list;
            }
            return null;
        } catch (IOException e) {
            Log.e(TAG, "Impossible to connect to Geocoder", e);
            return null;
        }
    }

}
