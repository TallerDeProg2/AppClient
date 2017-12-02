package fiuba.ubreapp;

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

public class ModifyCard extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "ModifyCard";

    private Bundle bundle;
    private Card card;
    private User user;
    private String cardjson,userjson;
    private Gson gson;

    private String URL;

    EditText name;
    EditText number;
    EditText year;
    EditText month;
    RadioButton visa;
    RadioButton mastercard;
    RadioButton americanexpress;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_card);

        bundle = getIntent().getExtras();

        gson = new Gson();

        cardjson = bundle.getString("Card");
        card = gson.fromJson(cardjson,Card.class);

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        URL = bundle.getString("User");

        name = (EditText) findViewById(R.id.editText8);
        number = (EditText) findViewById(R.id.editText10);
        year = (EditText) findViewById(R.id.editText11);
        month = (EditText) findViewById(R.id.editText12);
        visa = (RadioButton) findViewById(R.id.radioButton4);
        mastercard = (RadioButton) findViewById(R.id.radioButton5);
        americanexpress = (RadioButton) findViewById(R.id.radioButton6);
        change = (Button) findViewById(R.id.button4);

        name.setText(card.getName());
        number.setText(card.getNumber());
        year.setText(card.getExpireYearCard());
        month.setText(card.getExpireMonthCard());

        if(card.getType().equals("visa"))
            visa.toggle();
        else
            if(card.getType().equals("mastercard"))
                mastercard.toggle();
            else
                americanexpress.toggle();

        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String sname,smonth,syear,snumber,stype;
        boolean bname,bmonth,byear,bnumber;
        boolean bename,bemonth, beyear, benumber, betype;
        boolean nonempty, nonequal;

        Info urlinfo,infocard,infoanswer,infotoken;

        String url,endpoint;

        Card card1;

        PutRestApi put;

        Intent intent;

        int status;

        if(v.getId() == R.id.button4){
            sname = name.getText().toString();
            smonth = month.getText().toString();
            syear = year.getText().toString();
            snumber = number.getText().toString();

            bname = sname.isEmpty();
            bmonth = smonth.isEmpty();
            byear = syear.isEmpty();
            bnumber = snumber.isEmpty();

            bename = sname.equals(card.getName());
            bemonth = smonth.equals(card.getExpireMonthCard());
            beyear = syear.equals(card.getExpireYearCard());
            benumber = snumber.equals(card.getNumber());

            if(visa.isChecked())
                stype = "visa";
            else
                if(mastercard.isChecked())
                    stype = "mastercard";
                else
                    stype = "americanexpress";

            betype = stype.equals(card.getType());

            nonempty = !bname && !bmonth && !byear && !bnumber;
            nonequal = !bename || !bemonth || !beyear || !benumber || !betype;

            intent = new Intent(ModifyCard.this,MapActivity.class);
            intent.putExtra("Type",user.getType());
            intent.putExtra("User", userjson);
            intent.putExtra("URL",URL);


            if(nonempty && nonequal){
                card1 = new Card();
                urlinfo = new Info();
                infocard = new Info();
                infoanswer = new Info();
                infotoken = new Info();
                put = new PutRestApi();

                card1.setName(sname);
                card1.setExpireMonth(smonth);
                card1.setExpireYear(syear);
                card1.setNumber(snumber);
                card1.setType(stype);

                endpoint = "";

                url = URL + endpoint;

                urlinfo.setInfo(url);

                infocard.setInfo(gson.toJson(card1));
                infotoken.setInfo(user.getToken());

                try {
                    put.execute(urlinfo,infocard,infoanswer,infotoken).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = infoanswer.getStatus();

                switch (status) {
                    case 200:
                        intent.putExtra("Card",infoanswer.getInfo());
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
                    intent.putExtra("Card",cardjson);
                    startActivity(intent);
                } else {
                    if(bname)
                        name.setError("Name can't be blank.");
                    if(bnumber)
                        number.setError("Number can't be blank.");
                    if(bmonth)
                        month.setError("Month can't be blank.");
                    if(byear)
                        year.setError("Year can't be blank.");
                }
            }
        }

    }
}
