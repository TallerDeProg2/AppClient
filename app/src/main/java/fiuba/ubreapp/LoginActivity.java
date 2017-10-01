package fiuba.ubreapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.Utility;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

//Pantalla de Logeo.
public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button acceptButton = (Button) findViewById(R.id.button2);
        acceptButton.setOnClickListener(this);

        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        addLoginButton();
    }

    //Agrego boton Log In de Facebook
    private void addLoginButton() {
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends",
                "public_profile", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            //Success Log In Facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
                String text = "User ID: "
                        + loginResult.getAccessToken().getUserId()
                        + "\n" +
                        "Auth Token: "
                        + loginResult.getAccessToken().getToken();

                intent.putExtra("Result", text);

                Log.i(TAG,"Success Log In");

                startActivity(intent);
            }

            //Cancel Log In Facebook
            @Override
            public void onCancel() {
                Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
                intent.putExtra("Result","Login attempt canceled.");
                Log.i(TAG,"Success Cancel Log In");
                startActivity(intent);

            }

            //Error Log In Facebook
            @Override
            public void onError(FacebookException error) {

                Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
                intent.putExtra("Result","Login attempt failed.");
                Log.e(TAG,"Error");
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Acciones al cliquear boton de Log In o registro de nuevo usuario
    @Override
    public void onClick (View v) {

        Button acceptButton = (Button) findViewById(R.id.button2);
        EditText user = (EditText) findViewById(R.id.editText);
        EditText password = (EditText) findViewById(R.id.editText2);

        acceptButton.setText("Log In");

        if (v.getId() == R.id.button2) {
            Intent intent = new Intent(LoginActivity.this, ResultActivity.class);
            String text = "User ID: "
                    + user.getText().toString()
                    + "\n" +
                    "Password: "
                    + password.getText().toString();
            intent.putExtra("Result", text);
            Log.i(TAG,"User Email: "+ text);
            startActivity(intent);
        }

        if (v.getId() == R.id.textView){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Log.i(TAG,"Go to Register");
            startActivity(intent);
        }
    }

}