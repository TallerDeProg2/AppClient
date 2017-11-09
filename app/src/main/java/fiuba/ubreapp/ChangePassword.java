package fiuba.ubreapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChangePassword";

    Gson gson;
    User user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        String jtext;
        Bundle bundle;

        Button changeButton = (Button) findViewById(R.id.button5);
        changeButton.setOnClickListener(this);

        gson = new Gson();
        bundle = getIntent().getExtras();
        jtext = bundle.getString("User");

        user = gson.fromJson(jtext,User.class);
        password = user.getPassword();
    }

    @Override
    public void onClick(View v) {

        EditText actualpassword = (EditText) findViewById(R.id.editText27);
        EditText newpassword = (EditText) findViewById(R.id.editText28);
        EditText repeatpassword = (EditText) findViewById(R.id.editText29);

        String sap,snp,srp;
        Boolean bap,bnp,brp,bequal;

        PutRestApi put = new PutRestApi();
        String url = "http://demo1144105.mockable.io/Card/";
        Info urlinfo = new Info();
        Info userinfo = new Info();
        Info useranswer = new Info();
        int status;

        Intent intent;

        sap = actualpassword.getText().toString();
        snp = newpassword.getText().toString();
        srp = repeatpassword.getText().toString();

        bap = sap.isEmpty();
        bnp = snp.isEmpty();
        brp = srp.isEmpty();

        bequal = snp.equals(srp);

        if(v.getId() == R.id.button5){
            if(!bap && !bnp && !brp){
                if(sap.equals(password)){
                    if(!bequal)
                        repeatpassword.setError("New Passwords aren't equals.");
                    else{
                        urlinfo.setInfo(url);
                        user.setPassword(snp);
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
                                intent = new Intent(ChangePassword.this, MapActivity.class);
                                Log.i(TAG,"Password Changed");
                                startActivity(intent);
                                break;
                            case 400:
                                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                            case 401:
                                break; //Unauthorized
                            case 500:
                                break; //Unexpected Error
                        }


                    }
                } else {
                    actualpassword.setError("Incorrect Password");
                }
            } else {
                if(bap)
                    actualpassword.setError("Password can't be blank");
                if(bnp)
                    newpassword.setError("Password can't be blank");
                if(brp)
                    repeatpassword.setError("Password can't be blank");
            }
        }

    }
}
