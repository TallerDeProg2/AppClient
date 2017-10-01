package fiuba.ubreapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText name;
        EditText lastname;
        EditText email;

        Gson gson;
        User user;
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
        email = (EditText) findViewById(R.id.editText5);

        if (getIntent().hasExtra("User")){
            Log.i(TAG,"Bundle no vacio");
            gson = new Gson();
            bundle = getIntent().getExtras();
            jtext = bundle.getString("User");
            user = gson.fromJson(jtext,User.class);

            name.setText(user.getName());
            lastname.setText(user.getLastName());
            email.setText(user.getEmail());
        } else {
            Log.i(TAG,"Bundle vacio");
        }

    }

    @Override
    public void onClick (View v) {

        Button nextButton = (Button) findViewById(R.id.button3);
        Button cancelButton = (Button) findViewById(R.id.button5);
        EditText name = (EditText) findViewById(R.id.editText3);
        EditText lastname = (EditText) findViewById(R.id.editText4);
        EditText email = (EditText) findViewById(R.id.editText5);
        EditText password = (EditText) findViewById(R.id.editText6);
        EditText password2 = (EditText) findViewById(R.id.editText7);

        String sname,slastname,semail,spassword,spassword2;
        Boolean bname,blastname,bemail,bpassword,bpassword2,bequal;

        User user;
        Intent intent;
        Gson gson;
        String userjson;

        nextButton.setText("Next");
        cancelButton.setText("Cancel");

        sname = name.getText().toString();
        slastname = lastname.getText().toString();
        semail = email.getText().toString();
        spassword = password.getText().toString();
        spassword2 = password2.getText().toString();

        bname = sname.isEmpty();
        blastname = slastname.isEmpty();
        bemail = semail.isEmpty();
        bpassword = spassword.isEmpty();
        bpassword2 = spassword2.isEmpty();
        bequal = spassword.equals(spassword2);

        if(v.getId() == R.id.button3 || v.getId() == R.id.textView9 || v.getId() == R.id.textView10){

            if(!bname && !blastname && !bemail && !bpassword && !bpassword2 && bequal){
                user = new User(sname,slastname,semail,spassword);
                gson = new Gson();
                userjson = gson.toJson(user);

                if (v.getId() == R.id.button3){
                    intent = new Intent(RegisterActivity.this, ResultActivity2.class);
                    intent.putExtra("User",userjson);
                    Log.i(TAG,"User Registration.");
                    startActivity(intent);
                }

                if (v.getId() == R.id.textView9) {
                    intent = new Intent(RegisterActivity.this, RegisterPaymentData.class);
                    intent.putExtra("User",userjson);
                    Log.i(TAG, "Register Payment Data.");
                    startActivity(intent);
                }

                if (v.getId() == R.id.textView10) {
                    intent = new Intent(RegisterActivity.this, ResultActivity.class);
                    intent.putExtra("User",userjson);
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
