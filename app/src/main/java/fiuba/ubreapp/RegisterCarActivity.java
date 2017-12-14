package fiuba.ubreapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**Agregado de los datos del auto que maneja el conductor.*/
public class RegisterCarActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "RegisterAsDriver";
    private User user;
    private Car car;
    private Gson gson;
    private Intent intent;
    private String bundletext;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_car);

        Button acceptButton = (Button) findViewById(R.id.button7);
        acceptButton.setOnClickListener(this);

        RadioButton radioNo = (RadioButton) findViewById(R.id.radioButton2);
        radioNo.toggle();

        Bundle bundle = getIntent().getExtras();
        bundletext=bundle.getString("User");
        URL = bundle.getString("URL");
        gson = new Gson();
        user = gson.fromJson(bundletext,User.class);
    }

    @Override
    public void onClick (View v) {

        EditText brand = (EditText) findViewById(R.id.editText9);
        EditText model = (EditText) findViewById(R.id.editText13);
        EditText colour = (EditText) findViewById(R.id.editText14);
        EditText plate = (EditText) findViewById(R.id.editText15);
        EditText year = (EditText) findViewById(R.id.editText16);
        EditText state = (EditText) findViewById(R.id.editText17);
        EditText radio = (EditText) findViewById(R.id.editText26);
        RadioButton radioYes = (RadioButton) findViewById(R.id.radioButton);

        String sbrand, smodel,scolour,splate,syear,sstate,sradio;
        Boolean bbrand,bmodel,bcolour,bplate,byear,bstate,bradio;

        String userjson;
        String url,endpoint;
        PostRestApi post = new PostRestApi();

//        String url = "http://demo1144105.mockable.io/Car/";
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();
        int status;

        endpoint = "/drivers/"+user.getId()+"/cars";
        url = URL + endpoint;

        urlinfo.setInfo(url);

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

        if(v.getId() == R.id.button7){

            if(!bbrand && !bmodel && !bcolour && !bplate && !byear && !bstate && !bradio ){

                final ProgressDialog progressDialog = new ProgressDialog(RegisterCarActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Registering Car...");
                progressDialog.show();


                car = new Car();

                car.setBrand(sbrand);
                car.setModel(smodel);
                car.setColor(scolour);
                car.setPlate(splate);
                car.setYear(syear);
                car.setStatus(sstate);
                car.setRadio(sradio);

                if(radioYes.isChecked())
                    car.setAirconditioner(true);
                else
                    car.setAirconditioner(false);

                userjson = gson.toJson(car);

                userinfo.setInfo(userjson);

                try {
                    post.execute(urlinfo,userinfo,useranswer).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = useranswer.getStatus();

                progressDialog.hide();

                switch (status) {
                    case 201:
                        intent = new Intent(RegisterCarActivity.this, LoginActivity.class);
                        intent.putExtra("URL",URL);
                        Log.i(TAG,"Car Registration");
                        startActivity(intent);
                        break;
                    case 400:
                        break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                    case 401:
                        break; //Unauthorized
                    case 500:
                        break; //Unexpected Error
                }
            } else {
                if(bbrand)
                    brand.setError("Brand can't be blank");
                if(bmodel)
                    model.setError("Model can't be blank");
                if(bcolour)
                    colour.setError("Colour can't be blank");
                if(bplate)
                    plate.setError("Plate can't be blank");
                if(byear)
                    year.setError("Year can't be blank");
                if(bstate)
                    state.setError("State can't be blank");
                if(bradio)
                    radio.setError("Radio can't be blank");
                Log.e(TAG,"Incomplete Data");
            }
        }

    }

}
