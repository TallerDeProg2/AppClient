package fiuba.ubreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.view.View.OnClickListener;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class DataCar extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "DataCar";

    private Bundle bundle;
    private Car car;
    private User user;
    private String carjson, userjson, type, URL,cardjson;
    private Gson gson;

    EditText brand;
    EditText model;
    EditText colour;
    EditText plate;
    EditText year;
    EditText state;
    EditText radio;
    RadioButton airyes;
    RadioButton airno;
    Button editaccept;
    Button backcancel;

    Boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_car);

        edit = false;

        bundle = getIntent().getExtras();

        gson = new Gson();

        cardjson = bundle.getString("Card");
        carjson = bundle.getString("Car");
        car = gson.fromJson(carjson,Car.class);

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        URL = bundle.getString("URL");

        type = user.getType();

        brand = findViewById(R.id.editText9);
        model = findViewById(R.id.editText13);
        colour = findViewById(R.id.editText14);
        plate = findViewById(R.id.editText15);
        year = findViewById(R.id.editText16);
        state = findViewById(R.id.editText17);
        radio = findViewById(R.id.editText26);
        airyes = findViewById(R.id.radioButton);
        airno = findViewById(R.id.radioButton2);
        editaccept = findViewById(R.id.button7);
        backcancel = findViewById(R.id.button19);


        brand.setText(car.getBrand());
        model.setText(car.getModel());
        colour.setText(car.getColor());
        plate.setText(car.getPlate());
        year.setText(car.getYear());
        state.setText(car.getStatus());
        radio.setText(car.getRadio());

        if(car.getAirconditioner())
            airyes.toggle();
        else
            airno.toggle();

        editaccept.setOnClickListener(this);
        backcancel.setOnClickListener(this);

        brand.setFocusable(false);
        model.setFocusable(false);
        colour.setFocusable(false);
        plate.setFocusable(false);
        year.setFocusable(false);
        state.setFocusable(false);
        radio.setFocusable(false);
    }


    @Override
    public void onClick(View v) {

        String sbrand, smodel, scolour, splate,syear,sstate,sradio;
        boolean bbrand,bmodel,bcolour,bplate,byear,bstate,bradio,bair;
        boolean bebrand,bemodel,becolour,beplate,beyear,bestate,beradio,beair;
        boolean nonempty, nonequal;

        Info urlinfo,infocar,infoanswer,infotoken;

        String url,endpoint;

        Car car1;

        PutRestApi put;

        Intent intent;

        int status;

        intent = new Intent(DataCar.this,MapActivity.class);
        intent.putExtra("Type",user.getType());
        intent.putExtra("User", userjson);
        intent.putExtra("URL",URL);
        intent.putExtra("Card",cardjson);

        if (v.getId() == R.id.button19){

            if(edit){
                brand.setText(car.getBrand());
                model.setText(car.getModel());
                colour.setText(car.getColor());
                plate.setText(car.getPlate());
                year.setText(car.getYear());
                state.setText(car.getStatus());
                radio.setText(car.getRadio());

                if(car.getAirconditioner())
                    airyes.toggle();
                else
                    airno.toggle();

                brand.setFocusable(false);
                model.setFocusable(false);
                colour.setFocusable(false);
                plate.setFocusable(false);
                year.setFocusable(false);
                state.setFocusable(false);
                radio.setFocusable(false);

                editaccept.setText("Edit");
                backcancel.setText("Back");
                edit = false;

            } else {
                intent.putExtra("Car",carjson);
                startActivity(intent);
            }
        }


        if(v.getId() == R.id.button7){

            if(edit){
                sbrand = brand.getText().toString();
                smodel = model.getText().toString();
                scolour = colour.getText().toString();
                splate = plate.getText().toString();
                syear = year.getText().toString();
                sstate = state.getText().toString();
                sradio = radio.getText().toString();

                bbrand = sbrand.isEmpty();
                bmodel = smodel.isEmpty();
                bcolour = scolour.isEmpty();
                bplate = splate.isEmpty();
                byear = syear.isEmpty();
                bstate = sstate.isEmpty();
                bradio = sradio.isEmpty();

                if(airyes.isChecked())
                    bair = true;
                else
                    bair = false;

                bebrand = sbrand.equals(car.getBrand());
                bemodel = smodel.equals(car.getModel());
                becolour = scolour.equals(car.getColor());
                beplate = splate.equals(car.getPlate());
                beyear = syear.equals(car.getYear());
                bestate = sstate.equals(car.getStatus());
                beradio = sradio.equals(car.getRadio());

                if((bair && car.getAirconditioner()) || (!bair && !car.getAirconditioner()) )
                    beair = true;
                else
                    beair = false;

                nonempty = !bbrand && !bmodel && !bcolour && !bplate && !byear && !bstate && !bradio;
                nonequal = !bebrand || !bemodel || !becolour || !beplate || !beyear || !bestate || !beradio || beair;



                if(nonempty && nonequal){
                    car1 = new Car();
                    urlinfo = new Info();
                    infocar = new Info();
                    infoanswer = new Info();
                    infotoken = new Info();
                    put = new PutRestApi();

                    car1.setBrand(sbrand);
                    car1.setModel(smodel);
                    car1.setColor(scolour);
                    car1.setPlate(splate);
                    car1.setYear(syear);
                    car1.setStatus(sstate);
                    car1.setRadio(sradio);
                    car1.setAirconditioner(bair);

                    endpoint = "/drivers/" + user.getId() + "/cars" ;

                    url = URL + endpoint;

                    urlinfo.setInfo(url);

                    infocar.setInfo(gson.toJson(car1));
                    infotoken.setInfo(user.getToken());

                    try {
                        put.execute(urlinfo,infocar,infoanswer,infotoken).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    status = infoanswer.getStatus();

                    Log.i(TAG, "Status Change Car: " + String.valueOf(status));

                    switch (status) {
                        case 200:
                            intent.putExtra("Car",infoanswer.getInfo());
                            Log.i(TAG,"Modification Success");
                            startActivity(intent);
                            break;
                        case 400:
                            break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                        case 404:
                            break; //No existe recurso solicitado
                        case 500:
                            break; //Unexpected Error
                    }

                } else {
                    if(!nonequal){
                        intent.putExtra("Car",carjson);
                        startActivity(intent);
                    } else {
                        if(bbrand)
                            brand.setError("Brand can't be blank.");
                        if(bmodel)
                            model.setError("Model can't be blank.");
                        if(bcolour)
                            colour.setError("Colour can't be blank.");
                        if(bplate)
                            plate.setError("Plate can't be blank.");
                        if(byear)
                            year.setError("Year can't be blank.");
                        if(bstate)
                            state.setError("State can't be blank.");
                        if(bradio)
                            radio.setError("Radio can't be blank.");
                    }
                }

            } else {
                brand.setFocusableInTouchMode(true);
                model.setFocusableInTouchMode(true);
                colour.setFocusableInTouchMode(true);
                plate.setFocusableInTouchMode(true);
                year.setFocusableInTouchMode(true);
                state.setFocusableInTouchMode(true);
                radio.setFocusableInTouchMode(true);
                brand.setFocusable(true);
                model.setFocusable(true);
                colour.setFocusable(true);
                plate.setFocusable(true);
                year.setFocusable(true);
                state.setFocusable(true);
                radio.setFocusable(true);
                editaccept.setText("Accept");
                backcancel.setText("Cancel");
                edit = true;
            }
        }

    }


}
