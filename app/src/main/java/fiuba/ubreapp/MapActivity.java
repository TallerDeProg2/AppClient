package fiuba.ubreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

/**Mapa con la ubicacion. Opciones para ver perfil y editarlo.*/
public class MapActivity extends AppCompatActivity {

    private Gson gson;
    private Driver driver;
    private Passenger passenger;
    private String bundlejson;
    private Boolean asdriver;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        asdriver = bundle.getBoolean("AsDriver");
        gson = new Gson();
        if(!asdriver){
            bundlejson = bundle.getString("Passenger");
//            passenger = gson.fromJson(bundlejson,Passenger.class);
        } else {
            bundlejson = bundle.getString("Driver");
//            driver = gson.fromJson(bundlejson,Driver.class);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            intent = new Intent(MapActivity.this, ModifyProfile.class);
            if(!asdriver)
                intent.putExtra("Passenger",bundlejson);
            else
                intent.putExtra("Driver",bundlejson);
            intent.putExtra("AsDriver",asdriver);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_settings2) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
