package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class PayTripActivity extends AppCompatActivity implements View.OnClickListener {

    Gson gson;
    User user;
    String URL, userjson, cardjson,type,idtrip;
    Bundle bundle;
    Intent intent;
    ToastMessage tm;
    Context context;
    Button pay;
    TextView text;
    RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_trip);

        gson = new Gson();
        bundle = getIntent().getExtras();
        URL = bundle.getString("URL");
        userjson = bundle.getString("User");
        cardjson = bundle.getString("Card");
        type = bundle.getString("Type");
        idtrip = bundle.getString("idtrip");

        user = gson.fromJson(userjson,User.class);

        context = getApplicationContext();
        tm = new ToastMessage(context);

        pay = findViewById(R.id.button16);
        text = findViewById(R.id.textView31);

        pay.setOnClickListener(this);
        text.setText("Seleccione metodo de pago");
        rb = findViewById(R.id.radioButton10);
        rb.toggle();
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button16){
            String endpoint = "/trips/" + idtrip +"/end";
            String paymethod;
            Info url,info,answer,token;
            int status;
            PostRestApi post = new PostRestApi();
            url = new Info();
            info = new Info();
            answer = new Info();
            token = new Info();

            if(rb.isChecked())
                paymethod = "cash";
            else
                paymethod = "card";

            url.setInfo(URL + endpoint);
            token.setInfo(user.getToken());
            info.setInfo("{\"paymethod\":\""+paymethod+"\"}");

            Log.i("TAG",info.getInfo());

            try {
                post.execute(url,info,answer,token).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status = answer.getStatus();

            Log.i("PAY","Status: " + status);
            Log.i("PAY","Mensaje: " + answer.getInfo());

            switch (status) {
                case 201:
                    endtrip(answer.getInfo());
                    break;
                case 400:
                    break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                case 404:
                    break; //No existe recurso solicitado
                case 500:
                    break; //Unexpected Error
            }
        }

    }

    private void endtrip(String cost){
        intent = new Intent(PayTripActivity.this,EndTripActivity.class);
        intent.putExtra("User",userjson);
        intent.putExtra("Type",type);
        intent.putExtra("URL",URL);
        intent.putExtra("Card",cardjson);
        intent.putExtra("Cost",cost);
        startActivity(intent);
    }


}
