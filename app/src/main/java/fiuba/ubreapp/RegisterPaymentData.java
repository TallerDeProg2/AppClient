package fiuba.ubreapp;

import android.app.ProgressDialog;
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
    private Card card;
    private User user;
    private Gson gson;
    private Intent intent;
    private String bundletext;
    private String URL;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_payment_data);

        Button acceptButton = findViewById(R.id.button4);
        acceptButton.setOnClickListener(this);

        Button skipButton = findViewById(R.id.button6);
        skipButton.setOnClickListener(this);

        RadioButton radioVisa = findViewById(R.id.radioButton4);

        radioVisa.toggle();

        Bundle bundle = getIntent().getExtras();
        bundletext=bundle.getString("User");
        URL = bundle.getString("URL");
        gson = new Gson();
        user = gson.fromJson(bundletext,User.class);
        card = new Card();
    }

    @Override
    public void onClick (View v) {

        EditText name = findViewById(R.id.editText8);
        EditText cardnumber = findViewById(R.id.editText10);
        EditText month = findViewById(R.id.editText12);
        EditText year = findViewById(R.id.editText11);
        EditText ccvv = findViewById(R.id.editText34);

        RadioButton radioVisa = findViewById(R.id.radioButton4);
        RadioButton radioMastercard = findViewById(R.id.radioButton5);
        RadioButton radioAmericanExpress = findViewById(R.id.radioButton6);

        String sname,scardnumber,smonth,syear,sccvv;
        Boolean bname,bcardnumber,bmonth,byear,bccvv;

        sname = name.getText().toString();
        scardnumber = cardnumber.getText().toString();
        smonth = month.getText().toString();
        syear = year.getText().toString();
        sccvv = ccvv.getText().toString();

        bname = sname.isEmpty();
        bcardnumber = scardnumber.isEmpty();
        bmonth = smonth.isEmpty();
        byear = syear.isEmpty();
        bccvv = sccvv.isEmpty();

        if(v.getId() == R.id.button4){

            if(!bname && !bcardnumber && !bmonth && !byear && !bccvv){

//                progressDialog = new ProgressDialog(RegisterPaymentData.this,
//                        R.style.Theme_AppCompat_DayNight_Dialog);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Registering Card...");
//                progressDialog.show();

                card.setNumber(scardnumber);
                card.setExpireMonth(smonth);
                card.setExpireYear(syear);
                card.setCcvv(sccvv);

                if(radioVisa.isChecked())
                    card.setType(radioVisa.getText().toString());
                else
                    if(radioMastercard.isChecked())
                        card.setType(radioMastercard.getText().toString());
                    else
                        card.setType(radioAmericanExpress.getText().toString());

                sendInformation(gson.toJson(card));

            } else {
                if(bname)
                    name.setError("Name can't be blank");
                if(bcardnumber)
                    cardnumber.setError("Card Number can't be blank");
                if(bmonth)
                    month.setError("Month can't be blank");
                if(byear)
                    year.setError("Year can't be blank");
                if(bccvv)
                    ccvv.setError("CCVV can't be blank");
                Log.e(TAG,"Error in Register Payment Data.");
            }
        }

        if (v.getId() == R.id.button6) {
            intent = new Intent(RegisterPaymentData.this, LoginActivity.class);
            intent.putExtra("URL",URL);
            Log.i(TAG,"Card Registration");
            startActivity(intent);
        }

    }

    private void sendInformation(String info){
        PostRestApi post = new PostRestApi();
        Info urlinfo = new Info();
        Info cardinfo = new Info();
        Info answerinfo = new Info();
        Info tokeninfo = new Info();
        int status;
        String endpoint;

        endpoint = "/passengers/"+user.getId()+"/card";
        urlinfo.setInfo(URL + endpoint);
        cardinfo.setInfo(info);
//        tokeninfo.setInfo(user.getToken());

        try {
            post.execute(urlinfo,cardinfo,answerinfo).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        status = answerinfo.getStatus();
        Log.i(TAG,"Status: " + String.valueOf(status));
//        progressDialog.dismiss();

        switch (status) {
            case 201:
                intent = new Intent(RegisterPaymentData.this, LoginActivity.class);
                intent.putExtra("URL",URL);
                Log.i(TAG,"Card Registration");
                startActivity(intent);
                break;
            case 400:
                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
            case 401:
                break; //Unauthorized
            case 500:
                break; //Unexpected Error
            default:
                break;
        }

    }
}
