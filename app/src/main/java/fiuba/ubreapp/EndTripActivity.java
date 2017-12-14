package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class EndTripActivity extends AppCompatActivity implements View.OnClickListener {

    Gson gson;
    User user;
    String URL, userjson, cardjson,type,cost;
    Bundle bundle;
    Intent intent;
    ToastMessage tm;
    Context context;
    Button accept;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_trip);

        gson = new Gson();
        bundle = getIntent().getExtras();
        URL = bundle.getString("URL");
        userjson = bundle.getString("User");
        cardjson = bundle.getString("Card");
        type = bundle.getString("Type");
        cost = bundle.getString("Cost");


        try {
            JSONObject obj = new JSONObject(cost);
            JSONObject obj2 = obj.getJSONObject("cost");
            cost = obj2.getString("value") + " " + obj2.getString("currency");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        context = getApplicationContext();
        tm = new ToastMessage(context);

        accept = findViewById(R.id.button17);
        text = findViewById(R.id.textView36);

        accept.setOnClickListener(this);

        text.setText("El viaje te salio: $"+cost);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button17){
            intent = new Intent(EndTripActivity.this,MapActivity.class);
            intent.putExtra("User",userjson);
            intent.putExtra("Type",type);
            intent.putExtra("URL",URL);
            intent.putExtra("Card",cardjson);
            startActivity(intent);
        }

    }
}
