package fiuba.ubreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class PayMethodSelectActivity extends AppCompatActivity implements View.OnClickListener{

    Bundle bundle;
    Intent intent;
    String user,card,type,URL,routes;
    RadioButton cash;
    Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method_select);

        bundle = getIntent().getExtras();
        user = bundle.getString("User");
        card = bundle.getString("Card");
        type = bundle.getString("Type");
        URL = bundle.getString("URL");
        routes = bundle.getString("Routes");

        cash = findViewById(R.id.radioButton8);
        cash.toggle();

        accept = findViewById(R.id.button18);

        accept.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button18){
            intent = new Intent(PayMethodSelectActivity.this,MapTripActivity.class);
            intent.putExtra("User",user);
            intent.putExtra("Card",card);
            intent.putExtra("URL",URL);
            intent.putExtra("Routes",routes);
            intent.putExtra("Type",type);
            if(cash.isChecked())
                intent.putExtra("Payment","cash");
            else
                intent.putExtra("Payment","card");
            startActivity(intent);
        }

    }
}
