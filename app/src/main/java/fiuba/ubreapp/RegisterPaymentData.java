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

public class RegisterPaymentData extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "PaymentDataActivity";
    private User user;
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

        Bundle bundle = getIntent().getExtras();
        bundletext=bundle.getString("User");
        gson = new Gson();
        user = gson.fromJson(bundletext,User.class);
    }

    @Override
    public void onClick (View v) {

//        Button acceptButton = (Button) findViewById(R.id.button4);
//        Button cancelButton = (Button) findViewById(R.id.button6);
        EditText name = (EditText) findViewById(R.id.editText8);
        EditText cardnumber = (EditText) findViewById(R.id.editText10);
        EditText month = (EditText) findViewById(R.id.editText12);
        EditText year = (EditText) findViewById(R.id.editText11);

        String sname,scardnumber,smonth,syear;
        Boolean bname,bcardnumber,bmonth,byear;

        String userjson;
//
//        acceptButton.setText("Accept");
//        cancelButton.setText("Cancel");

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

                user.setNameCard(sname);
                user.setNumberCard(scardnumber);
                user.setExpireMonthCard(smonth);
                user.setExpireYearCard(syear);

                gson = new Gson();
                userjson = gson.toJson(user);

                intent = new Intent(RegisterPaymentData.this, ResultActivity2.class);
                intent.putExtra("User",userjson);
                Log.i(TAG,"User Registration.");
                startActivity(intent);

            } else {
                Log.e(TAG,"Error in Register Payment Data");
            }
        }

        if (v.getId() == R.id.button6) {
            intent = new Intent(RegisterPaymentData.this, RegisterActivity.class);
            intent.putExtra("User",bundletext);
            Log.i(TAG,"Cancel Registration Payment Data.");
            startActivity(intent);
        }

    }
}
