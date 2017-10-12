package fiuba.ubreapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**Comienzo de registro de un nuevo usuario. Por default se es pasajero.*/
public class RegisterActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText name;
        EditText lastname;
        EditText username;

        Gson gson;
        Passenger passenger;
        String jtext;
        Bundle bundle;

        Button nextButton = (Button) findViewById(R.id.button3);
        nextButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.button5);
        cancelButton.setOnClickListener(this);

        TextView paymentData = (TextView) findViewById(R.id.textView9);
        paymentData.setOnClickListener(this);

        TextView asDriver = (TextView) findViewById(R.id.textView10);
        asDriver.setOnClickListener(this);

        name = (EditText) findViewById(R.id.editText3);
        lastname = (EditText) findViewById(R.id.editText4);
        username = (EditText) findViewById(R.id.editText5);

        if (getIntent().hasExtra("Passenger")){
            Log.i(TAG,"Bundle no vacio");
            gson = new Gson();
            bundle = getIntent().getExtras();
            jtext = bundle.getString("Passenger");
            passenger = gson.fromJson(jtext,Passenger.class);

            name.setText(passenger.getName());
            lastname.setText(passenger.getLastName());
            username.setText(passenger.getUsername());
        } else {
            Log.i(TAG,"Bundle vacio");
        }

    }

    @Override
    public void onClick (View v) {

        EditText username = (EditText) findViewById(R.id.editText3);
        EditText email = (EditText) findViewById(R.id.editText7);
        EditText name = (EditText) findViewById(R.id.editText18);
        EditText lastname = (EditText) findViewById(R.id.editText19);
        EditText country = (EditText) findViewById(R.id.editText20);
        EditText birthdate = (EditText) findViewById(R.id.editText21);
        EditText password = (EditText) findViewById(R.id.editText22);
        EditText password2 = (EditText) findViewById(R.id.editText23);

        String sname,slastname,susername,spassword,spassword2;
        Boolean bname,blastname,busername,bpassword,bpassword2,bequal;

        Passenger passenger;
        Driver driver;
        User user;
        Intent intent;
        Gson gson;
        String userjson;

        PostRestApi post = new PostRestApi();
        String url = "http://demo1144105.mockable.io/Passenger/";
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();

        urlinfo.setInfo(url);

        sname = name.getText().toString();
        slastname = lastname.getText().toString();
        susername = username.getText().toString();
        spassword = password.getText().toString();
        spassword2 = password2.getText().toString();

        bname = sname.isEmpty();
        blastname = slastname.isEmpty();
        busername = susername.isEmpty();
        bpassword = spassword.isEmpty();
        bpassword2 = spassword2.isEmpty();
        bequal = spassword.equals(spassword2);

        if(v.getId() == R.id.button3 || v.getId() == R.id.textView9 || v.getId() == R.id.textView10){

            if(!bname && !blastname && !busername && !bpassword && !bpassword2 && bequal){

                gson = new Gson();

                if (v.getId() == R.id.button3){
                    user = new User(susername,sname,slastname,spassword);
                    user.setEmail(email.getText().toString());
                    user.setCountry(country.getText().toString());
//                    passenger.setBirthdate(birthdate.getText().toString());
                    userjson = gson.toJson(user);
                    userinfo.setInfo(userjson);

                    try {
                        post.execute(urlinfo,userinfo,useranswer).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Log.i(TAG,"Passenger Registration.");
                    startActivity(intent);
                }

                if (v.getId() == R.id.textView9) {
                    passenger = new Passenger(susername,sname,slastname,spassword);
                    passenger.setEmail(email.getText().toString());
                    passenger.setCountry(country.getText().toString());
//                    passenger.setBirthdate(birthdate.getText().toString());
                    userjson = gson.toJson(passenger);
                    intent = new Intent(RegisterActivity.this, RegisterPaymentData.class);
                    intent.putExtra("Passenger",userjson);
                    Log.i(TAG, "Register Payment Data.");
                    startActivity(intent);
                }

                if (v.getId() == R.id.textView10) {
                    driver = new Driver(susername,sname,slastname,spassword);
                    driver.setEmail(email.getText().toString());
                    driver.setCountry(country.getText().toString());
//                    driver.setBirthdate(birthdate.getText().toString());
                    userjson = gson.toJson(driver);
                    intent = new Intent(RegisterActivity.this, RegisterAsDriverActivity.class);
                    intent.putExtra("Driver",userjson);
                    Log.i(TAG, "Register as Driver");
                    startActivity(intent);
                }

            } else {
                Log.e(TAG,"Error in Register Data");
            }
        }

        if (v.getId() == R.id.button5) {
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            Log.i(TAG,"Cancel Registration.");
            startActivity(intent);
        }

    }

}
