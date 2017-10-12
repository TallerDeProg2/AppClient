package fiuba.ubreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**Vista de los datos basicos del usuario con posibilidad de modificarlos.*/
public class ModifyProfile extends AppCompatActivity implements OnClickListener{

    private static final String TAG = "ModifyProfile";

    private Bundle bundle;
    private User user;
    private Boolean asdriver;
    private String userjson,bundlejson;
    private Gson gson;
    private PutRestApi put;
    private Intent intent;

    private Passenger passenger;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_profile);

        bundle = getIntent().getExtras();
        asdriver = bundle.getBoolean("AsDriver");

        if(!asdriver){
            userjson = bundle.getString("Passenger");
            passenger = gson.fromJson(userjson,Passenger.class);
            user = passenger.getUser();
        } else {
            userjson = bundle.getString("Driver");
            driver = gson.fromJson(userjson,Driver.class);
            user = driver.getUser();
        }

        Button changeButton = (Button) findViewById(R.id.button10);
        changeButton.setOnClickListener(this);

        EditText name = (EditText) findViewById(R.id.editText4);
        EditText lastname = (EditText) findViewById(R.id.editText5);
        EditText country = (EditText) findViewById(R.id.editText6);
        EditText email = (EditText) findViewById(R.id.editText24);
        EditText date = (EditText) findViewById(R.id.editText25);

        gson = new Gson();

        user = gson.fromJson(userjson,User.class);

        name.setText(user.getName());
        lastname.setText(user.getLastName());
        country.setText(user.getCountry());
        email.setText(user.getEmail());
//        date.getText(user.getBirthdate());

    }

    public void onClick (View v) {
        EditText name = (EditText) findViewById(R.id.editText4);
        EditText lastname = (EditText) findViewById(R.id.editText5);
        EditText country = (EditText) findViewById(R.id.editText6);
        EditText email = (EditText) findViewById(R.id.editText24);
        EditText date = (EditText) findViewById(R.id.editText25);

        String sname,slastname,scountry,semail,sdate;
        boolean bname,blastname,bcountry,bemail,bdate,isempty;

        sname = name.getText().toString();
        slastname = lastname.getText().toString();
        scountry = country.getText().toString();
        semail = email.getText().toString();

        bname = sname.equals(user.getName());
        blastname = slastname.equals(user.getLastName());
        bcountry = scountry.equals(user.getCountry());
        bemail = semail.equals(user.getEmail());

        isempty = sname.isEmpty() && slastname.isEmpty() && scountry.isEmpty() && semail.isEmpty();

        String url = "http://demo1144105.mockable.io";
        String parameters;
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();

        int status;

        intent = new Intent(ModifyProfile.this, MapActivity.class);

        if((!bname || !blastname || !bcountry || !bemail) && !isempty){
            user.setName(sname);
            user.setLastname(slastname);
            user.setEmail(semail);
            user.setCountry(scountry);

            put = new PutRestApi();

            if(!asdriver)
                parameters = "/Passenger/" +user.getId() + "/";
            else
                parameters = "/Driver/" +user.getId() + "/";

            urlinfo.setInfo(url+parameters);
            userinfo.setInfo(gson.toJson(user));

            try {
                put.execute(urlinfo,userinfo,useranswer).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status = useranswer.getStatus();

            switch (status) {
                case 200:
                    bundlejson = useranswer.getInfo();
                    user = gson.fromJson(bundlejson,User.class);
                    if (!asdriver){
                        passenger.setName(user.getName());
                        passenger.setLastname(user.getLastName());
                        passenger.setEmail(user.getEmail());
                        passenger.setCountry(user.getCountry());
                        bundlejson = gson.toJson(passenger);
                        intent.putExtra("Passenger",bundlejson);
                    } else {
                        driver.setName(user.getName());
                        driver.setLastname(user.getLastName());
                        driver.setEmail(user.getEmail());
                        driver.setCountry(user.getCountry());
                        bundlejson = gson.toJson(driver);
                        intent.putExtra("Driver",bundlejson);
                    }
                    intent.putExtra("AsDriver",asdriver);
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
            if(isempty){

            } else {
                if (!asdriver){
                    intent.putExtra("Passenger",userjson);
                } else {
                    intent.putExtra("Driver",userjson);
                }
                intent.putExtra("AsDriver",asdriver);
                Log.i(TAG,"No Changes");
                startActivity(intent);
            }
        }
    }
}
