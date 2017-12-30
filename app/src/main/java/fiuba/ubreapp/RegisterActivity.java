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

import com.firebase.client.Firebase;
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

        Firebase.setAndroidContext(this);

        Bundle bundle;

        bundle = getIntent().getExtras();

        URL = bundle.getString("URL");

        Button nextButton = findViewById(R.id.button3);
        nextButton.setOnClickListener(this);

        RadioButton radioPassenger = findViewById(R.id.radioButton7);

        radioPassenger.toggle();

        date = findViewById(R.id.editText21);

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

    }

    @Override
    public void onClick (View v) {

        EditText username = findViewById(R.id.editText3);
        EditText email = findViewById(R.id.editText7);
        EditText name = findViewById(R.id.editText18);
        EditText lastname = findViewById(R.id.editText19);
        EditText country = findViewById(R.id.editText20);
        EditText birthdate = findViewById(R.id.editText21);
        EditText password = findViewById(R.id.editText22);
        EditText password2 = findViewById(R.id.editText23);

        RadioButton radioPassenger = findViewById(R.id.radioButton7);

        String sname,slastname,susername,spassword,spassword2,stype;
        Boolean bname,blastname,busername,bpassword,bpassword2,bequal,bpassenger;

        User user;
        Intent intent;
        Gson gson;
        String userjson;
        String url,endpoint;

        PostRestApi post = new PostRestApi();

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
                        if(bpassenger){
                            RegisterInFirebase(susername,spassword);    
                            intent = new Intent(RegisterActivity.this, RegisterPaymentData.class);
                            intent.putExtra("Type","passenger");
                        } else {
                            intent = new Intent(RegisterActivity.this, RegisterPaymentDataDriver.class);
                            intent.putExtra("Type","driver");
                        }

                        intent.putExtra("User",useranswer.getInfo());
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

    public void RegisterInFirebase(String user,String password){
        Firebase reference = new Firebase("https://ubre-7bd12.firebaseio.com/users");
        reference.child(user).child("password").setValue(password);
    }

}
