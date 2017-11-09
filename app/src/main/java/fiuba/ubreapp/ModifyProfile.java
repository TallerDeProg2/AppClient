package fiuba.ubreapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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

    DatePickerDialog datePickerDialog;
    EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_profile);

        bundle = getIntent().getExtras();

        gson = new Gson();

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        Button changeButton = (Button) findViewById(R.id.button10);
        changeButton.setOnClickListener(this);

        EditText name = (EditText) findViewById(R.id.editText4);
        EditText lastname = (EditText) findViewById(R.id.editText5);
        EditText country = (EditText) findViewById(R.id.editText6);
        EditText email = (EditText) findViewById(R.id.editText24);
        date = (EditText) findViewById(R.id.editText25);

        name.setText(user.getFirstname());
        lastname.setText(user.getLastName());
        country.setText(user.getCountry());
        email.setText(user.getEmail());
        date.setText(user.getBirthdate());

        if(user.getType() == "Passenger")
            asdriver = false;
        else
            asdriver = true;


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
                datePickerDialog = new DatePickerDialog(ModifyProfile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

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
        sdate = date.getText().toString();

        bname = sname.equals(user.getFirstname());
        blastname = slastname.equals(user.getLastName());
        bcountry = scountry.equals(user.getCountry());
        bemail = semail.equals(user.getEmail());
        bdate = sdate.equals(user.getBirthdate());

        isempty = sname.isEmpty() || slastname.isEmpty() || scountry.isEmpty() || semail.isEmpty() || sdate.isEmpty();

        String url = "http://demo1144105.mockable.io";
        String parameters;
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();

        int status;

        intent = new Intent(ModifyProfile.this, MapActivity.class);

        if((!bname || !blastname || !bcountry || !bemail || !bdate) && !isempty){
            user.setFirstname(sname);
            user.setLastname(slastname);
            user.setEmail(semail);
            user.setCountry(scountry);
            user.setBirthdate(sdate);

            put = new PutRestApi();

            if(!asdriver)
                parameters = "/Passenger/" + user.getId() + "/";
            else
                parameters = "/Driver/" + user.getId() + "/";

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
                if(sname.isEmpty())
                    name.setError("Name can't be blank");
                if(slastname.isEmpty())
                    lastname.setError("Lastname can't be blank");
                if(scountry.isEmpty())
                    country.setError("Country can't be blank");
                if(semail.isEmpty())
                    email.setError("Email can't be blank");
            } else {
                Log.i(TAG,"No Changes");
                startActivity(intent);
            }
        }
    }
}
