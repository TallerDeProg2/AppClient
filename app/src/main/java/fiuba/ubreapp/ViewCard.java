package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;

public class ViewCard extends AppCompatActivity {

    private static final String TAG = "ViewCard";

    private Bundle bundle;
    private Card card;
    private String cardjson;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        bundle = getIntent().getExtras();

        gson = new Gson();

        cardjson = bundle.getString("Card");
        card = gson.fromJson(cardjson,Card.class);

        EditText name = (EditText) findViewById(R.id.editText8);
        EditText number = (EditText) findViewById(R.id.editText10);
        EditText year = (EditText) findViewById(R.id.editText11);
        EditText month = (EditText) findViewById(R.id.editText12);
        EditText type = (EditText) findViewById(R.id.editText31);

        name.setText(card.getName());
        number.setText(card.getNumber());
        year.setText(card.getExpireYearCard());
        month.setText(card.getExpireMonthCard());
        type.setText(card.getType());

        name.setFocusable(false);
        number.setFocusable(false);
        month.setFocusable(false);
        year.setFocusable(false);
        type.setFocusable(false);

    }
}
