package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class DataCard extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "DataCard";

    private Bundle bundle;
    private Card card;
    private User user;
    private String cardjson,userjson,type,carjson;
    private Gson gson;

    private String URL;

    EditText name;
    EditText number;
    EditText year;
    EditText month;
    EditText ccvv;
    RadioButton visa;
    RadioButton mastercard;
    RadioButton americanexpress;
    Button editaccept;
    Boolean edit;
    Context context;
    ToastMessage tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_card);

        edit = false;

        bundle = getIntent().getExtras();

        gson = new Gson();

        cardjson = bundle.getString("Card");
        card = gson.fromJson(cardjson,Card.class);

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        URL = bundle.getString("URL");

        type = bundle.getString("Type");

        if(type.equals("driver"))
            carjson = bundle.getString("Car");

        name = findViewById(R.id.editText8);
        number = findViewById(R.id.editText10);
        year = findViewById(R.id.editText11);
        month = findViewById(R.id.editText12);
        visa = findViewById(R.id.radioButton4);
        mastercard = findViewById(R.id.radioButton5);
        americanexpress = findViewById(R.id.radioButton6);
        editaccept = findViewById(R.id.button4);
        ccvv = findViewById(R.id.editText34);

        name.setText(user.getFirstname() + " " + user.getLastName());
        number.setText(card.getNumber());
        year.setText(card.getExpireYearCard());
        month.setText(card.getExpireMonthCard());
        ccvv.setText(card.getCcvv());

        if(card.getType().equals("visa"))
            visa.toggle();
        else
            if(card.getType().equals("mastercard"))
                mastercard.toggle();
            else
                americanexpress.toggle();

        editaccept.setOnClickListener(this);

        name.setFocusable(false);
        number.setFocusable(false);
        month.setFocusable(false);
        year.setFocusable(false);
        ccvv.setFocusable(false);

        context = getApplicationContext();
        tm = new ToastMessage(context);
    }

    @Override
    public void onClick(View v) {

        String smonth,syear,snumber,stype,sccvv;
        boolean bmonth,byear,bnumber,bccvv;
        boolean bemonth, beyear, benumber, betype,beccvv;
        boolean nonempty, nonequal;

        Info urlinfo,infocard,infoanswer,infotoken;

        String url,endpoint;

        Card card1;

        PutRestApi put;

        Intent intent;

        int status;

        intent = new Intent(DataCard.this,MapActivity.class);
        intent.putExtra("Type",user.getType());
        intent.putExtra("User", userjson);
        intent.putExtra("URL",URL);
        if(type.equals("driver"))
            intent.putExtra("Car",carjson);

        if(v.getId() == R.id.button4){

            if(edit){
                smonth = month.getText().toString();
                syear = year.getText().toString();
                snumber = number.getText().toString();
                sccvv = ccvv.getText().toString();

                bmonth = smonth.isEmpty();
                byear = syear.isEmpty();
                bnumber = snumber.isEmpty();
                bccvv = sccvv.isEmpty();

                bemonth = smonth.equals(card.getExpireMonthCard());
                beyear = syear.equals(card.getExpireYearCard());
                benumber = snumber.equals(card.getNumber());
                beccvv = sccvv.equals(card.getCcvv());

                if(visa.isChecked())
                    stype = "visa";
                else
                if(mastercard.isChecked())
                    stype = "mastercard";
                else
                    stype = "americanexpress";

                betype = stype.equals(card.getType());

                nonempty = !bmonth && !byear && !bnumber && !bccvv;
                nonequal = !bemonth || !beyear || !benumber || !betype || !beccvv;

                if(nonempty && nonequal){
                    card1 = new Card();
                    urlinfo = new Info();
                    infocard = new Info();
                    infoanswer = new Info();
                    infotoken = new Info();
                    put = new PutRestApi();

                    card1.setExpireMonth(smonth);
                    card1.setExpireYear(syear);
                    card1.setNumber(snumber);
                    card1.setType(stype);
                    card1.setCcvv(sccvv);

                    if(type.equals("passenger"))
                        endpoint = "/passengers/"+user.getId()+"/card";
                    else
                        endpoint = "/drivers/"+user.getId()+"/card";

                    url = URL + endpoint;

                    urlinfo.setInfo(url);

                    infocard.setInfo(gson.toJson(card1));

                    infotoken.setInfo(user.getToken());

                    Log.i(TAG,infocard.getInfo());

                    try {
                        put.execute(urlinfo,infocard,infoanswer,infotoken).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    status = infoanswer.getStatus();

                    Log.i(TAG, "Status Change Card: " + String.valueOf(status));

                    switch (status) {
                        case 200:
                            intent.putExtra("Card",infoanswer.getInfo());
                            Log.i(TAG,"Modification Success");
                            startActivity(intent);
                            break;
                        case 400:
                            tm.show(infoanswer.getInfo());
                            break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                        case 404:
                            tm.show(infoanswer.getInfo());
                            break; //No existe recurso solicitado
                        case 500:
                            tm.show(infoanswer.getInfo());
                            break; //Unexpected Error
                        default:
                            tm.show(infoanswer.getInfo());
                            break;
                    }
                } else {
                    if(!nonequal){
                        intent.putExtra("Card",cardjson);
                        startActivity(intent);
                    } else {

                        if(bnumber)
                            number.setError("Number can't be blank.");
                        if(bmonth)
                            month.setError("Month can't be blank.");
                        if(byear)
                            year.setError("Year can't be blank.");
                        if(bccvv)
                            ccvv.setError("CCVV can't be blank.");
                    }
                }
            } else {
                number.setFocusableInTouchMode(true);
                month.setFocusableInTouchMode(true);
                year.setFocusableInTouchMode(true);
                ccvv.setFocusableInTouchMode(true);
                number.setFocusable(true);
                month.setFocusable(true);
                year.setFocusable(true);
                ccvv.setFocusable(true);
                editaccept.setText("Accept");
                edit = true;
            }
        }
    }
}
