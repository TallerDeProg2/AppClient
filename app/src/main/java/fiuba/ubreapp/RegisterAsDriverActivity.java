package fiuba.ubreapp;

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
public class RegisterAsDriverActivity extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "RegisterAsDriver";
    private Driver driver;
    private Gson gson;
    private Intent intent;
    private String bundletext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_driver);

        Button acceptButton = (Button) findViewById(R.id.button7);
        acceptButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.button8);
        cancelButton.setOnClickListener(this);

        RadioButton radioNo = (RadioButton) findViewById(R.id.radioButton2);
        radioNo.toggle();

        Bundle bundle = getIntent().getExtras();
        bundletext=bundle.getString("Driver");
        gson = new Gson();
        driver = gson.fromJson(bundletext,Driver.class);
    }

    @Override
    public void onClick (View v) {

        EditText modelcar = (EditText) findViewById(R.id.editText9);
        EditText colour = (EditText) findViewById(R.id.editText13);
        EditText plate = (EditText) findViewById(R.id.editText14);
        EditText year = (EditText) findViewById(R.id.editText15);
        EditText state = (EditText) findViewById(R.id.editText16);
        EditText music = (EditText) findViewById(R.id.editText17);
        RadioButton radioYes = (RadioButton) findViewById(R.id.radioButton);

        String smodelcar,scolour,splate,syear,sstate,smusic;
        Boolean bmodelcar,bcolour,bplate,byear,bstate,bmusic;

        String userjson;

        PostRestApi post = new PostRestApi();
        String url = "http://demo1144105.mockable.io/Driver/";
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();
        int status;

        urlinfo.setInfo(url);

        smodelcar = modelcar.getText().toString();
        scolour = colour.getText().toString();
        splate = plate.getText().toString();
        syear = year.getText().toString();
        sstate = state.getText().toString();
        smusic = music.getText().toString();

        bmodelcar = smodelcar.isEmpty();
        bcolour = scolour.isEmpty();
        bplate = splate.isEmpty();
        byear = syear.isEmpty();
        bstate = sstate.isEmpty();
        bmusic = smusic.isEmpty();

        if(v.getId() == R.id.button7){

            if(!bmodelcar && !bcolour && !bplate && !byear && !bstate && !bmusic ){

                driver.setModelCar(smodelcar);
                driver.setColourCar(scolour);
                driver.setPlateCar(splate);
                driver.setYearCar(syear);
                driver.setStateCar(sstate);
                driver.setMusicCar(smusic);

                if(radioYes.isChecked())
                    driver.setAirConditioner(true);
                else
                    driver.setAirConditioner(false);

                gson = new Gson();
                userjson = gson.toJson(driver);

                userinfo.setInfo(userjson);

                try {
                    post.execute(urlinfo,userinfo,useranswer).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = useranswer.getStatus();

                switch (status) {
                    case 200:
                        intent = new Intent(RegisterAsDriverActivity.this, LoginActivity.class);
                        Log.i(TAG,"driver Registration As Driver");
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
                Log.e(TAG,"Error in Register As Driver");
            }
        }

        if (v.getId() == R.id.button8) {
            intent = new Intent(RegisterAsDriverActivity.this, RegisterActivity.class);
            intent.putExtra("Driver",bundletext);
            Log.i(TAG,"Cancel Registration As Driver.");
            startActivity(intent);
        }

    }

}
