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

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private static String TAG = "List";

    ListView listview;
    ArrayAdapter<String> array;
    private Bundle bundle;
    String strips,type;
    ParserHistorial ph;
    List<String> waittime;
    List<String> traveltime;
    List<String> totaltime;
    List<String> starttime;
    List<String> start;
    List<String> end;
    List<String> cost;
    List<String> otheruser;

    Intent intent;
    String aux;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        int i;

        listview = findViewById(android.R.id.list);
        bundle = getIntent().getExtras();

        waittime = new ArrayList<>();
        traveltime = new ArrayList<>();
        totaltime = new ArrayList<>();
        starttime = new ArrayList<>();
        start = new ArrayList<>();
        end = new ArrayList<>();
        cost = new ArrayList<>();
        otheruser = new ArrayList<>();

        strips = bundle.getString("Trips");
        type = bundle.getString("Type");

        Log.i(TAG,strips);

        ph = new ParserHistorial(type,strips);

        waittime = ph.getWaittime();
        traveltime = ph.getTraveltime();
        totaltime = ph.getTotaltime();
        starttime = ph.getStarttime();
        start = ph.getStart();
        end = ph.getEnd();
        cost = ph.getCost();
        otheruser = ph.getOtheruser();

        array = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        for(i = 0; i < start.size(); i++){
            aux = "Start: " + start.get(i) + "\n";
            aux = aux + "End: " + end.get(i);
            array.add(aux);
        }

        listview.setAdapter(array);

        listview.setClickable(true);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                setProgressBarIndeterminateVisibility(true);

                intent = new Intent(HistorialActivity.this,InfoHistorialActivity.class);

                aux = "Comienzo: " + start.get(position) + "\n";
                aux = aux + "Fin: " + end.get(position) + "\n";
                aux = aux + "Costo: " + cost.get(position) + "\n";
                if (type.equals("passenger"))
                    aux = aux + "ID Chofer: " + otheruser.get(position) + "\n";
                else
                    aux = aux + "ID Pasajero: " + otheruser.get(position) + "\n";
                aux = aux + "Hora comienzo: " + starttime.get(position) + "\n";
                aux = aux + "Tiempo de viaje: " + traveltime.get(position) + "\n";
                aux = aux + "Tiempo de espera: " + waittime.get(position) + "\n";
                aux = aux + "Tiempo total: " + totaltime.get(position);

                intent.putExtra("Data",aux);

                Log.i(TAG,"List Historial");
                startActivity(intent);

            }
        });


    }
}
