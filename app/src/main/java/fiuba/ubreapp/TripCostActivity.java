package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

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

        editcost.setText(cost);
        editcost.setFocusable(false);

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View view) {

        Info url,infojson,infoanswer,infotoken;
        PostRestApi post;
        String endpoint;
        int status;

        url = new Info();
        infoanswer = new Info();
        infojson = new Info();
        infotoken = new Info();
        post = new PostRestApi();

        endpoint = "/passenger/"+user.getId()+"/trips/request";

        if (view.getId() == R.id.button13){
            infotoken.setInfo(user.getToken());
            url.setInfo(URL+endpoint);
            infojson.setInfo(pr);
            try {
                post.execute(url,infojson,infoanswer,infotoken).get();
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
                    intent = new Intent(TripCostActivity.this,MapDoTripActivity.class);
                    intent.putExtra("User",userjson);
                    intent.putExtra("Card",cardjson);
                    intent.putExtra("Type",type);
                    intent.putExtra("Cost",cost);
                    intent.putExtra("Route",route);
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

        if (view.getId() == R.id.button13){

        }

    }
}
