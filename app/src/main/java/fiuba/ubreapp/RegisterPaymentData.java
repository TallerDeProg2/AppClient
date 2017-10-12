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

/**Agregado de los datos de pago.*/
public class RegisterPaymentData extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "PaymentDataActivity";
    private Passenger passenger;
    private Gson gson;
    private Intent intent;
    private String bundletext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_payment_data);

        Button acceptButton = (Button) findViewById(R.id.button4);
        acceptButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.button6);
        cancelButton.setOnClickListener(this);

        RadioButton radioVisa = (RadioButton) findViewById(R.id.radioButton4);

        radioVisa.toggle();

        Bundle bundle = getIntent().getExtras();
        bundletext=bundle.getString("Passenger");
        gson = new Gson();
        passenger = gson.fromJson(bundletext,Passenger.class);
    }

    @Override
    public void onClick (View v) {

        EditText name = (EditText) findViewById(R.id.editText8);
        EditText cardnumber = (EditText) findViewById(R.id.editText10);
        EditText month = (EditText) findViewById(R.id.editText12);
        EditText year = (EditText) findViewById(R.id.editText11);

        RadioButton radioVisa = (RadioButton) findViewById(R.id.radioButton4);
        RadioButton radioMastercard = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton radioAmericanExpress = (RadioButton) findViewById(R.id.radioButton6);

        String sname,scardnumber,smonth,syear;
        Boolean bname,bcardnumber,bmonth,byear;

        String userjson;

        PostRestApi post = new PostRestApi();
        String url = "http://demo1144105.mockable.io/Passenger/";
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();
        int status;

        urlinfo.setInfo(url);

        sname = name.getText().toString();
        scardnumber = cardnumber.getText().toString();
        smonth = month.getText().toString();
        syear = year.getText().toString();

        bname = sname.isEmpty();
        bcardnumber = scardnumber.isEmpty();
        bmonth = smonth.isEmpty();
        byear = syear.isEmpty();

        if(v.getId() == R.id.button4){

            if(!bname && !bcardnumber && !bmonth && !byear){

                passenger.setNameCard(sname);
                passenger.setNumberCard(scardnumber);
                passenger.setExpireMonthCard(smonth);
                passenger.setExpireYearCard(syear);

                if(radioVisa.isChecked())
                    passenger.setTypeCard(radioVisa.getText().toString());
                else
                    if(radioMastercard.isChecked())
                        passenger.setTypeCard(radioMastercard.getText().toString());
                    else
                        passenger.setTypeCard(radioAmericanExpress.getText().toString());

                gson = new Gson();
                userjson = gson.toJson(passenger);

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
                        intent = new Intent(RegisterPaymentData.this, LoginActivity.class);
                        Log.i(TAG,"Passenger Registration with Payment Data");
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
                Log.e(TAG,"Error in Register Payment Data");
            }
        }

        if (v.getId() == R.id.button6) {
            intent = new Intent(RegisterPaymentData.this, RegisterActivity.class);
            intent.putExtra("Passenger",bundletext);
            Log.i(TAG,"Cancel Registration Payment Data.");
            startActivity(intent);
        }

    }
}
