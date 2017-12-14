package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class TripInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "TripInfo";

    Bundle bundle;
    Intent intent;
    String userjson,passengerjson,cardjson,carjson,type,tripjson,URL,idtrip;
    Gson gson;
    ParserDirections parser;
    User passenger;
    String text;
    User user;
    Context context;
    ToastMessage tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        bundle = getIntent().getExtras();
        userjson = bundle.getString("User");
        passengerjson = bundle.getString("Passenger");
        cardjson = bundle.getString("Card");
        type = bundle.getString("Type");
        carjson = bundle.getString("Car");
        tripjson = bundle.getString("Trip");
        URL = bundle.getString("URL");
        idtrip = bundle.getString("idtrip");

        parser = new ParserDirections("{\"routes\":["+tripjson+"]}");
        gson = new Gson();
        passenger = gson.fromJson(passengerjson,User.class);
        user = gson.fromJson(userjson,User.class);

        Button button = findViewById(R.id.button15);
        EditText edittext = findViewById(R.id.editText33);

        button.setOnClickListener(this);

        text = "Username: " + passenger.getUsername() + ".\n";
        text = text + "Name: " + passenger.getFirstname() + ".\n";
        text = text + "Lastname: " + passenger.getLastName() + ".\n";
        text = text + "Origin: " + parser.getStartAddress(0) + ".\n";
        text = text + "Destination: " + parser.getEndAddress(0) + ".\n";
        text = text + "Time: " + parser.getSelectedTime(0) + ".\n";

        edittext.setText(text);
        edittext.setFocusable(false);

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View view) {
        PostRestApi post;
        Info url,info,answer,token;
        String endpoint;
        int status;

        if(view.getId() == R.id.button15){

            url = new Info();
            info = new Info();
            answer = new Info();
            token = new Info();

            post = new PostRestApi();

            endpoint = "/drivers/"+user.getId()+"/trip/confirmation";

            url.setInfo(URL + endpoint);
            info.setInfo("{\"trip_id\":"+ idtrip + "}");
            token.setInfo(user.getToken());

            try {
                post.execute(url,info,answer,token).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status = answer.getStatus();
            Log.i(TAG, "Status: " + String.valueOf(status));
            switch (status) {
                case 201:
                    intent = new Intent(TripInfoActivity.this,MapDoTripActivity.class);
                    intent.putExtra("User",userjson);
                    intent.putExtra("Car",carjson);
                    intent.putExtra("Card",cardjson);
                    intent.putExtra("Type",type);
                    intent.putExtra("Route",tripjson);
                    intent.putExtra("Passenger",passengerjson);
                    intent.putExtra("URL",URL);
                    intent.putExtra("OtherUser",passenger.getUsername());
                    startActivity(intent);
                    break;
                case 400:
                    tm.show(answer.getInfo());
                    break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                case 404:
                    tm.show(answer.getInfo());
                    break; //No existe recurso solicitado
                case 500:
                    tm.show(answer.getInfo());
                    break; //Unexpected Error
                default:
                    tm.show(answer.getInfo());
                    break;
            }
        }
    }
}
