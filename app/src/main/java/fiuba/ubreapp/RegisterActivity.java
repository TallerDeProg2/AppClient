package fiuba.ubreapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**Comienzo de registro de un nuevo usuario. Por default se es pasajero.*/
public class RegisterActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "RegisterActivity";

    DatePickerDialog datePickerDialog;
    EditText date;
    String URL;
    Context context;
    ToastMessage tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText name;
        EditText lastname;
        EditText username;

        Gson gson;
        User user;
        String jtext;
        Bundle bundle;

        bundle = getIntent().getExtras();

        URL = bundle.getString("URL");

        Button nextButton = (Button) findViewById(R.id.button3);
        nextButton.setOnClickListener(this);

        RadioButton radioPassenger = (RadioButton) findViewById(R.id.radioButton7);

        radioPassenger.toggle();

        name = (EditText) findViewById(R.id.editText18);
        lastname = (EditText) findViewById(R.id.editText19);
        username = (EditText) findViewById(R.id.editText3);
        date = (EditText) findViewById(R.id.editText21);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                c.set(1990,Calendar.JANUARY,1);
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                    date.setText(year + "-" + (monthOfYear + 1) + "-"+ dayOfMonth + " 00:00:00 ART");

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        context = getApplicationContext();
        tm = new ToastMessage(context);

//        if (getIntent().hasExtra("Card")){
//            Log.i(TAG,"Bundle no vacio");
//            gson = new Gson();
//            bundle = getIntent().getExtras();
//            jtext = bundle.getString("Card");
//            passenger = gson.fromJson(jtext,Card.class);
//
//            name.setText(passenger.getName());
//            lastname.setText(passenger.getLastName());
//            username.setText(passenger.getUsername());
//        } else {
//            Log.i(TAG,"Bundle vacio");
//        }

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

        RadioButton radioPassenger = (RadioButton) findViewById(R.id.radioButton7);

        String sname,slastname,susername,spassword,spassword2,stype;
        Boolean bname,blastname,busername,bpassword,bpassword2,bequal,bpassenger;

        User user;
        Intent intent;
        Gson gson;
        String userjson;
        String url,endpoint;

        PostRestApi post = new PostRestApi();
//        String url = "http://demo1144105.mockable.io/Card/";

        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();
        int status;

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

        if(radioPassenger.isChecked()){
            stype = "passenger";
            bpassenger = true;
        } else {
            stype = "driver";
            bpassenger = false;
        }

        endpoint = "/users";
        url = URL + endpoint;

        urlinfo.setInfo(url);

        if(v.getId() == R.id.button3){

            if(!bname && !blastname && !busername && !bpassword && !bpassword2 && bequal){

                gson = new Gson();

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Registering User...");
                progressDialog.show();

                user = new User(susername,sname,slastname,spassword,stype);
                user.setEmail(email.getText().toString());
                user.setCountry(country.getText().toString());
                user.setBirthdate(birthdate.getText().toString());
                userjson = gson.toJson(user);
                userinfo.setInfo(userjson);

                try {
                    post.execute(urlinfo,userinfo,useranswer).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = useranswer.getStatus();

                progressDialog.dismiss();

                Log.i(TAG,"Status Post: "+status);

                switch (status) {
                    case 201:
                        if(bpassenger)
                            intent = new Intent(RegisterActivity.this, RegisterPaymentData.class);
                        else
                            intent = new Intent(RegisterActivity.this, RegisterPaymentDataDriver.class);

                        intent.putExtra("URL",URL);

                        Log.i(TAG,"User Registration as "+stype);
                        startActivity(intent);
                        break;
                    case 400:
                        tm.show(useranswer.getInfo());
                        break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                    case 401:
                        tm.show(useranswer.getInfo());
                        break; //Unauthorized
                    case 500:
                        tm.show(useranswer.getInfo());
                        break; //Unexpected Error
                }

            } else {
                if(bname)
                    name.setError("Name can't be blank");
                if(blastname)
                    lastname.setError("Lastname can't be blank");
                if(busername)
                    username.setError("Username can't be blank");
                if(bpassword)
                    password.setError("Password can't be blank");
                if(bpassword2)
                    password2.setError("Password can't be blank");
                if(!bequal)
                    password2.setError("Passwords aren't equals");
                Log.e(TAG,"Error in Register Data");
            }
        }

    }

}
