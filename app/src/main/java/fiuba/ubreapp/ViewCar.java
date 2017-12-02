package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;

public class ViewCar extends AppCompatActivity {

    private Bundle bundle;
    private Car car;
    private String carjson;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        bundle = getIntent().getExtras();

        gson = new Gson();

        carjson = bundle.getString("Car");
        car = gson.fromJson(carjson,Car.class);

        EditText brand = (EditText) findViewById(R.id.editText9);
        EditText model = (EditText) findViewById(R.id.editText13);
        EditText colour = (EditText) findViewById(R.id.editText14);
        EditText plate = (EditText) findViewById(R.id.editText15);
        EditText year = (EditText) findViewById(R.id.editText16);
        EditText state = (EditText) findViewById(R.id.editText17);
        EditText radio = (EditText) findViewById(R.id.editText26);
        EditText air = (EditText) findViewById(R.id.editText30);

        brand.setText(car.getBrand());
        model.setText(car.getModel());
        colour.setText(car.getColour());
        plate.setText(car.getPlate());
        year.setText(car.getYear());
        state.setText(car.getState());
        radio.setText(car.getRadio());
        if(car.getAirconditioner())
            air.setText("YES");
        else
            air.setText("NO");

        brand.setFocusable(false);
        model.setFocusable(false);
        colour.setFocusable(false);
        plate.setFocusable(false);
        year.setFocusable(false);
        state.setFocusable(false);
        radio.setFocusable(false);
        air.setFocusable(false);
    }
}
